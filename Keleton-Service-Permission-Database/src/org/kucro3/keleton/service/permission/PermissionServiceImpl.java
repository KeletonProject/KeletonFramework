package org.kucro3.keleton.service.permission;

import static org.kucro3.keleton.service.permission.Misc.Naming.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.kucro3.keleton.UniqueService;
import org.kucro3.keleton.cause.FromUniqueService;
import org.kucro3.keleton.permission.EnhancedPermissionService;
import org.kucro3.keleton.permission.event.SubjectCollectionEvent;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Named;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionDescription.Builder;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;

public class PermissionServiceImpl implements EnhancedPermissionService, UniqueService {
	PermissionServiceImpl(DatabaseConnection connection) throws SQLException
	{
		this.db = connection;
		this.calculators = new LinkedList<>();
		this.collections = new ConcurrentHashMap<>();
		this.ensureTable();
		this.loadAll();
		this.defaults = new SubjectImpl(this, null, const_table("default"), "", () -> null);
	}
	
	@Override
	public SubjectImpl getDefaults() 
	{
		return defaults;
	}

	@Override
	public Optional<PermissionDescription> getDescription(String permission)
	{
		return Optional.empty();
	}
	
	@Override
	public Collection<PermissionDescription> getDescriptions() 
	{
		return Collections.emptyList();
	}

	@Override
	public SubjectCollection getGroupSubjects()
	{
		return getSubjects(PermissionService.SUBJECTS_GROUP);
	}

	@Override
	public Map<String, SubjectCollection> getKnownSubjects() 
	{
		return Collections.unmodifiableMap(new HashMap<>(collections));
	}

	@Override
	public SubjectCollection getSubjects(String identifier)
	{
		SubjectCollectionImpl collection;
		if((collection = collections.get(identifier)) != null)
			return collection;
		try {
			collections.put(identifier, collection = new SubjectCollectionImpl(this, db, identifier)._ENABLE_());
			EventHelper.fireSubjectCollectionCreateEvent(uid, identifier);
		} catch (SQLException e) {
			// unexpected
			throw new IllegalStateException("SQL Failure", e);
		}
		return collection;
	}

	@Override
	public SubjectCollection getUserSubjects() 
	{
		return getSubjects(PermissionService.SUBJECTS_USER);
	}

	@Override
	public Optional<Builder> newDescriptionBuilder(Object plugin)
	{
		return Optional.empty();
	}

	@Override
	public void registerContextCalculator(ContextCalculator<Subject> calculator) 
	{
		calculators.addLast(calculator);
	}
	
	synchronized void ensureTable() throws SQLException
	{
		ensureTable(db, const_table("default"), "default");
	}
	
	synchronized void loadAll() throws SQLException
	{
		Set<SubjectCollectionImpl> collections = SubjectCollectionImpl.fromTables(this, db);
		for(SubjectCollectionImpl collection : collections)
			this.collections.put(collection.getIdentifier(), collection._ENABLE_());
	}
	
	Set<Context> accumulate(Subject subject)
	{
		return accumulate(subject, new HashSet<>());
	}
	
	Set<Context> accumulate(Subject subject, Set<Context> contexts)
	{
		for(ContextCalculator<Subject> calculator : calculators)
			calculator.accumulateContexts(subject, contexts);
		return contexts;
	}
	
	boolean matches(Subject subject, Set<Context> contexts)
	{
		for(ContextCalculator<Subject> calculator : calculators)
			for(Context context : contexts)
				if(!calculator.matches(context, subject))
					return false;
		return true;
	}
	
	static void ensureTable(DatabaseConnection db, String rawTableName, String identifier) throws SQLException
	{
		db.execute("CREATE TABLE IF NOT EXISTS " + rawTableName + " "
				 + "("
				 + "UID varchar(255) NOT NULL,"
				 + "KEY varchar(max) NOT NULL,"
				 + "VALUE text,"
				 + "CONTEXT text,"
				 + "CONTEXT_HASH decimal(38),"
				 + "TYPE varchar(2) NOT NULL,"
				 + "UNIQUE (KEY, CONTEXT_HASH)"
				 + ") DEFAULT CHARSET=UTF8;");
		
		Optional<ResultSet> result =
				db.execute("SELECT * FROM " + rawTableName + " WHERE UID='" + attribute("name") + "' AND TYPE='" + attribute() + "';");
		if((!result.isPresent()) || (!result.get().next()))
			db.execute("INSERT INTO " + rawTableName + " "
					 + "(UID, KEY, TYPE) VALUES ('" + attribute("name") + "','" + identifier + "','" + attribute() + "');");
	}
	
	public PermissionServiceImpl _DISABLE_()
	{
		Sponge.getEventManager().unregisterListeners(this);
		this.defaults._DISABLE_();
		return this;
	}
	
	public PermissionServiceImpl _ENABLE_()
	{
		Sponge.getEventManager().registerListeners(SpongeMain.getInstance(), this);
		this.defaults._ENABLE_();
		return this;
	}
	
	@Listener
	public void _SYNC_onSubjectCollectionCreate(SubjectCollectionEvent.Create event, @Named("handler") FromUniqueService handler)
	{
		if(isSelf(handler))
			return;
		
		try {
			collections.put(event.getCollectionIdentifier(),
					new SubjectCollectionImpl(this, db, event.getCollectionIdentifier(), Misc.nosql() | Misc.sync())._ENABLE_());
		} catch (SQLException e) {
			// unused
			throw new IllegalStateException("Should not reach here", e);
		}
	}
	
	boolean isSelf(FromUniqueService handler)
	{
		return handler.getUniqueId().equals(uid);
	}

	@Override
	public UUID getUniqueId()
	{
		return this.uid;
	}
	
	final SubjectImpl defaults;
	
	final Map<String, SubjectCollectionImpl> collections;
	
	final DatabaseConnection db;
	
	final LinkedList<ContextCalculator<Subject>> calculators;
	
	final UUID uid = UUID.randomUUID();
}
