package org.kucro3.keleton.service.permission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.kucro3.keleton.auth.AuthUtil;
import org.kucro3.keleton.cause.FromUniqueService;
import org.kucro3.keleton.permission.event.SubjectEvent;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Named;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.util.Tristate;

import static org.kucro3.keleton.service.permission.Misc.Naming.*;

public class SubjectCollectionImpl implements SubjectCollection {
	SubjectCollectionImpl(PermissionServiceImpl owner, DatabaseConnection db, String identifier)
		throws SQLException
	{
		this(owner, db, identifier, 0);
	}
	
	SubjectCollectionImpl(PermissionServiceImpl owner, DatabaseConnection db, String identifier,
			int option) throws SQLException
	{
		this.owner = owner;
		this.db = db;
		this.identifier = identifier;
		this.tableName = AuthUtil.toUUID(identifier).toString().replace('-', '_');
		this.subjects = new ConcurrentHashMap<>();
		
		if(!Misc.nosql(option))
		{
			if(!Misc.ensured(option)) 
				this.ensureTable();
			this.loadAll();
		}
		
		this.initializeDefaults();
	}
	
	@Override
	public Subject get(String identifier)
	{
		SubjectImpl subject = subjects.get(identifier);
		if(subject != null)
			return subject;
		try {
			subjects.put(identifier, subject = new SubjectImpl(owner, this, table(tableName), identifier,
					Misc.fromIdentifier(identifier), Misc.nosql())._ENABLE_());
			EventHelper.fireSubjectCreateEvent(owner.uid, identifier);
		} catch (SQLException e) {
			// unused
			throw new IllegalStateException("Should not reach here", e);
		}
		return subject;
	}

	@Override
	public Iterable<Subject> getAllSubjects() 
	{
		Set<Subject> subjects = new HashSet<>();
		for(Subject subject : this.subjects.values())
			subjects.add(subject);
		subjects.add(defaults);
		return Collections.unmodifiableCollection(subjects);
	}

	@Override
	public Map<Subject, Boolean> getAllWithPermission(String permission) 
	{
		Collection<SubjectImpl> subjects = this.subjects.values();
		Map<Subject, Boolean> map = new HashMap<>();
		Tristate value;
		for(Subject subject : subjects)
			if(!(value = subject.getPermissionValue(subject.getActiveContexts(), permission)).equals(Tristate.UNDEFINED))
				map.put(subject, value.asBoolean());
		return Collections.unmodifiableMap(map);
	}

	@Override
	public Map<Subject, Boolean> getAllWithPermission(Set<Context> contexts, String permission) 
	{
		Collection<SubjectImpl> subjects = this.subjects.values();
		Map<Subject, Boolean> map = new HashMap<>();
		Tristate value;
		for(Subject subject : subjects)
			if(!(value = subject.getPermissionValue(contexts, permission)).equals(Tristate.UNDEFINED))
				map.put(subject, value.asBoolean());
		return Collections.unmodifiableMap(map);
	}

	@Override
	public SubjectImpl getDefaults() 
	{
		return defaults;
	}

	@Override
	public String getIdentifier()
	{
		return this.identifier;
	}

	@Override
	public boolean hasRegistered(String identifier) 
	{
		return subjects.containsKey(identifier);
	}
	
	static synchronized Set<SubjectCollectionImpl> fromTables(PermissionServiceImpl owner, DatabaseConnection db) throws SQLException
	{
		HashSet<SubjectCollectionImpl> set = new HashSet<>();
		
		Optional<ResultSet> optional = db.execute("SHOW TABLES;");
		ResultSet result = optional.orElse(null);
		if(optional.isPresent())
			while(result.next())
			{
				String rawTableName = result.getString("TABLE_NAME");
				if(!isTable(rawTableName))
					continue;
				Optional<ResultSet> optionalInTable = db.execute("SELECT * FROM " + rawTableName
															  + " WHERE UID='" + attribute("name") + "' AND TYPE='" + attribute() + "';");
				ResultSet resultInTable;
				if((!optional.isPresent()) || (!(resultInTable = optionalInTable.get()).next()))
					continue;
				else
				{
					String identifier = resultInTable.getString("KEY");
					set.add(new SubjectCollectionImpl(owner, db, identifier));
				}
			}
		
		return Collections.unmodifiableSet(set);
	}

	synchronized void ensureTable() throws SQLException
	{
		PermissionServiceImpl.ensureTable(db, table(tableName), identifier);
	}
	
	synchronized void loadAll() throws SQLException
	{
		Optional<ResultSet> optional = db.execute("SELECT DISTINCT UID FROM " + table(tableName) + ";");
		ResultSet result = optional.orElse(null);
		String uid;
		if(optional.isPresent())
			while(result.next()) 
			{
				uid = result.getString("UID");
				
				if(!isData(uid))
					continue;
				
				if(identifier(uid).isEmpty())
				{
					defaults = new SubjectImpl(owner, this, table(tableName), "", () -> null);
					continue;
				}

				final String identifier = identifier(uid);
				subjects.put(identifier, new SubjectImpl(owner, this, table(tableName), identifier,
						Misc.fromIdentifier(identifier))._ENABLE_());
			}
	}
	
	void initializeDefaults()
	{
		if(defaults == null)
			try {
				defaults = new SubjectImpl(owner, this, table(tableName), "", () -> null, Misc.nosql());
			} catch (SQLException e) {
				// unused
				throw new IllegalStateException("Should not reach here", e);
			}
	}
	
	public SubjectCollectionImpl _ENABLE_()
	{
		Sponge.getEventManager().registerListeners(SpongeMain.getInstance(), this);
		defaults._ENABLE_();
		return this;
	}
	
	public SubjectCollectionImpl _DISABLE_()
	{
		Sponge.getEventManager().unregisterListeners(this);
		defaults._DISABLE_();
		return this;
	}
	
	@Listener
	public void _SYNC_onSubjectCreate(SubjectEvent.Create event, @Named("handler") FromUniqueService handler)
	{
		if(owner.isSelf(handler))
			return;
		
		try {
			subjects.put(event.getSubjectIdentifier(), new SubjectImpl(owner, this, table(tableName), identifier, 
					Misc.fromIdentifier(identifier) , Misc.nosql() | Misc.sync())._ENABLE_());
		} catch (SQLException e) {
			// unused
			throw new IllegalStateException("Should not reach here", e);
		}
	}
	
	private SubjectImpl defaults;
	
	final Map<String, SubjectImpl> subjects;
	
	final DatabaseConnection db;
	
	private final String identifier;
	
	private final String tableName;
	
	final PermissionServiceImpl owner;
}
