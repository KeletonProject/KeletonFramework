package org.kucro3.keleton.service.permission;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.kucro3.keleton.cause.FromUniqueService;
import org.kucro3.keleton.permission.EnhancedSubjectData;
import org.kucro3.keleton.permission.event.SubjectDataInheritanceEvent;
import org.kucro3.keleton.permission.event.SubjectDataOptionEvent;
import org.kucro3.keleton.permission.event.SubjectDataPermissionEvent;
import org.kucro3.util.Reference;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Named;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.NodeTree;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import static org.kucro3.keleton.service.permission.Misc.Naming.*;

@SuppressWarnings("unchecked")
public class SubjectDataImpl implements EnhancedSubjectData {
	SubjectDataImpl(SubjectImpl owner, String table, String identifier, 
			Map<Set<Context>, Map<String, Boolean>> initialPermissions,
			Map<Set<Context>, Map<String, String>> initialOptions,
			Map<Set<Context>, List<String>> initialParents)
	{
		this.owner = owner;
		this.identifier = identifier;
		this.table = table;
		this.trees = new ConcurrentHashMap<>();
		this.options = new ConcurrentHashMap<>(initialOptions);
		this.parents = new ConcurrentHashMap<>(initialParents);
		
		for(Map.Entry<Set<Context>, Map<String, Boolean>> entry : initialPermissions.entrySet())
			this.trees.put(entry.getKey(), NodeTree.of(entry.getValue()));
	}
	
	SubjectDataImpl(SubjectImpl owner, String table, String identifier)
	{
		this(owner, table, identifier, Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
	}
	
	//@Override
	static String table(String table)
	{
		return table;
	}
	
	static SubjectDataImpl fromTable(SubjectImpl owner, String table, String identifier) throws SQLException
	{
		Map<Set<Context>, Map<String, Boolean>> initialPermissions = new HashMap<>();
		Map<Set<Context>, Map<String, String>> initialOptions = new HashMap<>();
		Map<Set<Context>, List<String>> initialParents = new HashMap<>();
		Map<Integer, Set<Context>> cache = new HashMap<>();
		
		final Reference<ResultSet> ref = new Reference<>();
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("SELECT * FROM " + table(table) + " WHERE UID=?");
			statement.setString(1, data(identifier));
			ref.set(statement.executeQuery());
		});
		ResultSet result = ref.get();
		if(result != null)
			while(result.next())
			{
				String type = result.getString("TYPE");
				
				Set<Context> contexts;
				int hash = result.getInt("CONTEXT_HASH");
				if(hash == 0)
					contexts = SubjectData.GLOBAL_CONTEXT;
				else if((contexts = cache.get(hash)) == null)
					cache.put(hash, contexts = Misc.deserialize(result.getString("CONTEXT")));
				
				if(isPermissionData(type))
				{
					String perm = identifier(result.getString("KEY"));
					Boolean value = result.getString("VALUE").equals(TRUE);
					
					Map<String, Boolean> permMap;
					if((permMap = initialPermissions.get(contexts)) == null)
						initialPermissions.put(contexts, permMap = new HashMap<>());
					
					permMap.put(perm, value);
				}
				else if(isOptionData(type))
				{
					String key = identifier(result.getString("KEY"));
					String value = result.getString("VALUE");
					
					Map<String, String> optMap;
					if((optMap = initialOptions.get(contexts)) == null)
						initialOptions.put(contexts, optMap = new HashMap<>());
					
					optMap.put(key, value);
				}
				else if(isParentData(type))
				{
					String parent = identifier(result.getString("KEY"));
					
					List<String> parents;
					if((parents = initialParents.get(contexts)) == null)
						initialParents.put(contexts, parents = new ArrayList<>());
					
					parents.add(parent);
				}
			}
		
		return new SubjectDataImpl(owner, table, identifier, initialPermissions, initialOptions, initialParents);
	}
	
	public Optional<NodeTree> getNodeTree(Set<Context> contexts)
	{
		return Optional.ofNullable(trees.get(contexts));
	}
	
	@Override
	public Tristate getPermission(Set<Context> contexts, String permission)
	{
		Optional<NodeTree> tree = getNodeTree(contexts);
		if(!tree.isPresent())
			return Tristate.UNDEFINED;
		else
			return tree.get().get(permission);
	}
	
	@Override
	public boolean addParent(Set<Context> contexts, Subject parent) 
	{
		return addParent(contexts, parent, 0);
	}
	
	final boolean addParent(Set<Context> contexts, Subject parent, int option)
	{
		String identifier = parent.getIdentifier();
		Optional<List<String>> optional;
		List<String> parentList = (optional = Optional.ofNullable(parents.get(contexts))).orElse(new ArrayList<>());
		
		if(!parentList.contains(identifier))
			try {
				insertParent(contexts, identifier);
				parentList.add(identifier);
				
				if(!optional.isPresent())
					parents.put(contexts, parentList);
				
				if(!Misc.sync(option))
					EventHelper.fireSubjectDataInheritanceAddEvent(owner.service.uid, owner, this, contexts, parent);
				
				return true;
			} catch (SQLException e) {
				return false;
			}
		else
			return false;
	}

	@Override
	public boolean clearOptions() 
	{
		return clearOptions(0);
	}
	
	final boolean clearOptions(int option)
	{
		try {
			if(!Misc.nosql(option))
				deleteOptions();
			options.clear();
			
			if(!Misc.sync(option))
				EventHelper.fireSubjectDataOptionClearAllEvent(owner.service.uid, owner, this);
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean clearOptions(Set<Context> contexts) 
	{
		return clearOptions(contexts, 0);
	}
	
	final boolean clearOptions(Set<Context> contexts, int option)
	{
		try {
			if(!Misc.nosql(option))
				deleteOptions(contexts);
			options.remove(contexts);
			
			if(!Misc.sync(option))
				EventHelper.fireSubjectDataOptionClearEvent(owner.service.uid, owner, this, contexts);
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean clearParents()
	{
		return clearParents(0);
	}
	
	final boolean clearParents(int option)
	{
		try {
			if(!Misc.nosql(option))
				deleteParents();
			parents.clear();
			
			if(!Misc.sync(option))
				EventHelper.fireSubjectDataInheritanceClearAllEvent(owner.service.uid, owner, this);
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean clearParents(Set<Context> contexts) 
	{
		return clearParents(0);
	}
	
	final boolean clearParents(Set<Context> contexts, int option)
	{
		try {
			if(!Misc.nosql(option))
				deleteParents(contexts);
			parents.remove(contexts);
			
			if(!Misc.sync(option))
				EventHelper.fireSubjectDataInheritanceClearEvent(owner.service.uid, owner, this, contexts);
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean clearPermissions() 
	{
		return clearPermissions(0);
	}
	
	final boolean clearPermissions(int option)
	{
		try {
			if(!Misc.nosql(option))
				deletePermissions();
			trees.clear();
			
			if(!Misc.sync(option))
				EventHelper.fireSubjectDataPermissionClearAllEvent(owner.service.uid, owner, this);
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean clearPermissions(Set<Context> contexts)
	{
		return clearPermissions(contexts, 0);
	}
	
	final boolean clearPermissions(Set<Context> contexts, int option)
	{
		try {
			if(!Misc.nosql(option))
				deletePermissions(contexts);
			trees.remove(contexts);
			
			if(!Misc.sync(option))
				EventHelper.fireSubjectDataPermissionClearEvent(owner.service.uid, owner, this, contexts);
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public Map<Set<Context>, Map<String, String>> getAllOptions() 
	{
		return Collections.unmodifiableMap(new HashMap<>(this.options));
	}

	@Override
	public Map<Set<Context>, List<Subject>> getAllParents()
	{
		Map<Set<Context>, List<Subject>> map = new HashMap<>();
		SubjectCollection collection = owner.service.getGroupSubjects();
		for(Map.Entry<Set<Context>, List<String>> entry : parents.entrySet())
		{
			List<Subject> list = new ArrayList<>();
			for(String identifier : entry.getValue())
				list.add(collection.get(identifier));
			map.put(entry.getKey(), list);
		}
		return Collections.unmodifiableMap(map);
	}

	@Override
	public Map<Set<Context>, Map<String, Boolean>> getAllPermissions() 
	{
		Map<Set<Context>, Map<String, Boolean>> map = new HashMap<>();
		for(Map.Entry<Set<Context>, NodeTree> entry : trees.entrySet())
			map.put(entry.getKey(), entry.getValue().asMap());
		return Collections.unmodifiableMap(map);
	}

	@Override
	public Map<String, String> getOptions(Set<Context> contexts)
	{
		return Collections.unmodifiableMap(new HashMap<>(Optional.of(options.get(contexts)).orElse(Collections.EMPTY_MAP)));
	}

	@Override
	public List<Subject> getParents(Set<Context> contexts) 
	{
		SubjectCollection collection = owner.service.getGroupSubjects();
		if(collection == null)
			return Collections.EMPTY_LIST;
		
		List<String> parents;
		if((parents = this.parents.get(contexts)) == null)
			return Collections.EMPTY_LIST;
		
		Subject subject;
		List<Subject> list = new ArrayList<>();
		for(String parent : parents)
			if((subject = collection.get(parent)) != null)
				list.add(subject);
		return Collections.unmodifiableList(list);
	}

	@Override
	public Map<String, Boolean> getPermissions(Set<Context> contexts)
	{
		Map<String, Boolean> map;
		NodeTree nodeTree;
		if((nodeTree = trees.get(contexts)) == null)
			map = Collections.EMPTY_MAP;
		else
			map = nodeTree.asMap();
		return map;
	}

	@Override
	public boolean removeParent(Set<Context> contexts, Subject parent) 
	{
		return removeParent(contexts, parent, 0);
	}
	
	final boolean removeParent(Set<Context> contexts, Subject parent, int option)
	{
		try {
			List<String> parentList = parents.get(contexts);
			
			if(parentList == null)
				return false;
			
			if(!parentList.contains(parent.getIdentifier()))
				return false;
			
			deleteParent(contexts, parent.getIdentifier());
			parentList.remove(parent.getIdentifier());
			
			if(parentList.isEmpty())
				parents.remove(contexts);
			
			if(!Misc.sync(option))
				EventHelper.fireSubjectDataInheritanceRemoveEvent(owner.service.uid, owner, this, contexts, parent);
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean setOption(Set<Context> contexts, String key, String value) 
	{
		return setOption(contexts, key, value, 0);
	}
	
	final boolean setOption(Set<Context> contexts, String key, String value, int option)
	{
		try {
			Optional<Map<String, String>> optional;
			Map<String, String> map = (optional = Optional.ofNullable(options.get(contexts))).orElse(new HashMap<>());
			
			if(!map.containsKey(key))
			{
				if(value == null)
					return false;
				
				if(!Misc.nosql(option))
					insertOption(contexts, key, value);
				map.put(key, value);
				
				if(!optional.isPresent())
					options.put(contexts, map);
				
				if(!Misc.sync(option))
					EventHelper.fireSubjectDataOptionSetEvent(owner.service.uid, owner, this, contexts, key, value);
					
				return true;
			}
			else if(value == null)
			{
				if(!Misc.nosql(option))
					deleteOption(contexts, key);
				map.remove(value);
				
				if(!Misc.sync(option))
					EventHelper.fireSubjectDataOptionRemoveEvent(owner.service.uid, owner, this, contexts, key);
				
				return true;
			}
			else
			{
				if(!Misc.nosql(option))
					updateOption(contexts, key, value);
				map.replace(key, value);
				
				if(!Misc.sync(option))
					EventHelper.fireSubjectDataOptionSetEvent(owner.service.uid, owner, this, contexts, key, value);
				
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean setPermission(Set<Context> contexts, String permission, Tristate value) 
	{
		return setPermission(contexts, permission, value, 0);
	}
	
	final boolean setPermission(Set<Context> contexts, String permission, Tristate value, int option)
	{
		NodeTree tree;
		
		if((tree = trees.get(contexts)) == null)
		{
			if(value.equals(Tristate.UNDEFINED))
				return false;
			
			tree = Misc.newNodeTree(permission, value.asBoolean());
			
			try {
				if(!Misc.nosql(option))
					insertPermission(contexts, permission, value.asBoolean());
				trees.put(contexts, tree);
				
				if(!Misc.sync(option))
					EventHelper.fireSubjectDataPermissionSetEvent(owner.service.uid, owner, this, contexts, permission, value);
				
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		else if(tree.get(permission).equals(value))
			return true;
		else
			if(value.equals(Tristate.UNDEFINED)) 
				try {
					if(!Misc.nosql(option))
						deletePermission(contexts, permission);
					trees.replace(contexts, Misc.merge(tree, permission, Tristate.UNDEFINED));
					
					if(!Misc.sync(option))
						EventHelper.fireSubjectDataPermissionSetEvent(owner.service.uid, owner, this, contexts, permission, value);
					
					return true;
				} catch (SQLException e) {
					return false;
			} 
			else
				try {
					if(!Misc.nosql(option))
						updatePermission(contexts, permission, value.asBoolean());
					trees.replace(contexts, Misc.merge(tree, permission, value));
					
					if(!Misc.sync(option))
						EventHelper.fireSubjectDataPermissionSetEvent(owner.service.uid, owner, this, contexts, permission, value);
					
					return true;
				} catch (SQLException e) {
					return false;
				}
	}
	
	synchronized void deleteParent(Set<Context> contexts, String parent) throws SQLException
	{
		final BigDecimal hash = Misc.hash128(Misc.serialize(contexts));
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("DELETE FROM " + table(table)
											 + " WHERE UID=?"
											 + "   AND KEY=?"
											 + "   AND VALUE=?"
											 + "   AND CONTEXT_HASH=?"
											 + "   AND TYPE=?");
			statement.setString(1, data(identifier));
			statement.setString(2, parent_data(parent));
			statement.setString(3, parent_data(parent));
			statement.setBigDecimal(4, hash);
			statement.setString(5, parent_data());
			statement.executeUpdate();
		});
	}
	
	@Override
	public boolean isTransient() 
	{
		return false;
	}
	
	synchronized void insertParent(Set<Context> contexts, String parent) throws SQLException
	{
		final String ctxs = Misc.serialize(contexts);
		final BigDecimal hash = Misc.hash128(ctxs);
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("INSERT INTO " + table(table)
											 + " (UID, KEY, VALUE, CONTEXT, CONTEXT_HASH, TYPE)"
											 + " VALUES (?, ?, ?, ?, ?, ?)");
			statement.setString(1, data(identifier));
			statement.setString(2, parent_data(parent));
			statement.setString(3, parent_data(parent));
			statement.setString(4, ctxs);
			statement.setBigDecimal(5, hash);
			statement.setString(6, parent_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void deleteOption(Set<Context> contexts, String key) throws SQLException
	{
		final BigDecimal hash = Misc.hash128(Misc.serialize(contexts));
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("DELETE FROM " + table(table)
											 + " WHERE UID=?"
											 + "   AND KEY=?"
											 + "   AND CONTEXT_HASH=?"
											 + "   AND TYPE=?");
			statement.setString(1, data(identifier));
			statement.setString(2, option_data(key));
			statement.setBigDecimal(3, hash);
			statement.setString(4, option_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void updateOption(Set<Context> contexts, String key, String value) throws SQLException
	{
		final BigDecimal hash = Misc.hash128(Misc.serialize(contexts));
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("UPDATE " + table(table) + " SET"
											 + " VALUE=?"
											 + " WHERE UID=?"
											 + "   AND KEY=?"
											 + "   CONTEXT_HASH=?"
											 + "   TYPE=?");
			statement.setString(1, value);
			statement.setString(2, data(identifier));
			statement.setString(3, option_data(key));
			statement.setBigDecimal(4, hash);
			statement.setString(5, option_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void insertOption(Set<Context> contexts, String key, String value) throws SQLException
	{
		final String ctxs = Misc.serialize(contexts);
		final BigDecimal hash = Misc.hash128(ctxs);
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("INSERT INTO " + table(table)
											 + " (UID, KEY, VALUE, CONTEXT, CONTEXT_HASH, TYPE)"
											 + " VALUES (?, ?, ?, ?, ?, ?)");
			statement.setString(1, data(identifier));
			statement.setString(2, option_data(key));
			statement.setString(3, value);
			statement.setString(4, ctxs);
			statement.setBigDecimal(5, hash);
			statement.setString(6, option_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void deletePermission(Set<Context> contexts, String permission) throws SQLException
	{
		final String ctxs = Misc.serialize(contexts);
		final BigDecimal hash = Misc.hash128(ctxs);
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("DELETE FROM " + table(table)
											 + " WHERE UID=?"
											 + "   AND KEY=?"
											 + "   AND CONTEXT_HASH=?"
											 + "   AND TYPE=?");
			statement.setString(1, data(identifier));
			statement.setString(2, permission_data(permission));
			statement.setBigDecimal(3, hash);
			statement.setString(4, permission_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void updatePermission(Set<Context> contexts, String permission, boolean value) throws SQLException
	{
		final String ctxs = Misc.serialize(contexts);
		final BigDecimal hash = Misc.hash128(ctxs);
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("UPDATE " + table(table) + " SET"
											 + " VALUE=?"
											 + " WHERE UID=?"
											 + "   AND KEY=?"
											 + "   AND CONTEXT_HASH=?"
											 + "   AND TYPE=?");
			statement.setString(1, value ? TRUE : FALSE);
			statement.setString(2, data(identifier));
			statement.setString(3, permission_data(permission));
			statement.setBigDecimal(4, hash);
			statement.setString(5, permission_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void insertPermission(Set<Context> contexts, String permission, boolean value) throws SQLException
	{
		final String ctxs = Misc.serialize(contexts);
		final BigDecimal hash = Misc.hash128(ctxs);
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("INSERT INTO " + table(table)
											 + " (UID, KEY, VALUE, CONTEXT, CONTEXT_HASH, TYPE)"
											 + " VALUES (?, ?, ?, ?, ?, ?)");
			statement.setString(1, data(identifier));
			statement.setString(2, permission_data(permission));
			statement.setString(3, value ? TRUE : FALSE);
			statement.setString(4, ctxs);
			statement.setBigDecimal(5, hash);
			statement.setString(6, permission_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void deleteParents(Set<Context> contexts) throws SQLException
	{
		final BigDecimal hash = Misc.hash128(Misc.serialize(contexts));
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("DELETE FROM " + table(table)
											 + " WHERE UID=?"
											 + "   AND CONTEXT_HASH=?"
											 + "   AND TYPE=?");
			statement.setString(1, data(identifier));
			statement.setBigDecimal(2, hash);
			statement.setString(3, parent_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void deleteParents() throws SQLException
	{
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("DELETE FROM " + table(table)
											 + " WHERE UID=?"
											 + "   AND TYPE=?");
			statement.setString(1, data(identifier));
			statement.setString(2, parent_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void deleteOptions(Set<Context> contexts) throws SQLException
	{
		final BigDecimal hash = Misc.hash128(Misc.serialize(contexts));
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("DELETE FROM " + table(table)
											 + " WHERE UID=?"
											 + "   AND CONTEXT_HASH=?"
											 + "   AND TYPE=?");
			statement.setString(1, data(identifier));
			statement.setBigDecimal(2, hash);
			statement.setString(3, option_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void deleteOptions() throws SQLException
	{
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("DELETE FROM " + table(table)
											 + " WHERE UID=?"
											 + "   AND TYPE=?");
			statement.setString(1, data(identifier));
			statement.setString(2, option_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void deletePermissions(Set<Context> contexts) throws SQLException
	{
		final BigDecimal hash = Misc.hash128(Misc.serialize(contexts));
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("DELETE FROM " + table(table)
											 + " WHERE UID=?"
											 + "   AND CONTEXT_HASH=?"
											 + "   AND TYPE=?");
			statement.setString(1, data(identifier));
			statement.setBigDecimal(2, hash);
			statement.setString(3, permission_data());
			statement.executeUpdate();
		});
	}
	
	synchronized void deletePermissions() throws SQLException
	{
		owner.service.db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("DELETE FROM " + table(table)
											 + " WHERE UID=?"
											 + "   AND TYPE=?");
			statement.setString(1, data(identifier));
			statement.setString(2, permission_data());
			statement.executeUpdate();
		});
	}
	
	@Listener
	public void _SYNC_onPermissionClear(SubjectDataPermissionEvent.Clear event, @Named("handler") FromUniqueService handler)
	{
		if(owner.service.isSelf(handler) || event.getSubjectData() != this)
			return;
		
		if(event.forAnyContext())
			this.clearPermissions(Misc.nosql());
		else
			this.clearPermissions(event.getContexts().get(), Misc.nosql() | Misc.sync());
	}
	
	@Listener
	public void _SYNC_onPermissionSet(SubjectDataPermissionEvent.Set event, @Named("handler") FromUniqueService handler)
	{
		if(owner.service.isSelf(handler) || event.getSubjectData() != this)
			return;
		
		this.setPermission(event.getContexts().get(), event.getPermission(), event.getValue(), Misc.nosql() | Misc.sync());
	}
	
	@Listener
	public void _SYNC_onOptionClear(SubjectDataOptionEvent.Clear event, @Named("handler") FromUniqueService handler)
	{
		if(owner.service.isSelf(handler) || event.getSubjectData() != this)
			return;
		
		if(event.forAnyContext())
			this.clearOptions(Misc.nosql());
		else
			this.clearOptions(event.getContexts().get(), Misc.nosql() | Misc.sync());
	}
	
	@Listener
	public void _SYNC_onOptionSet(SubjectDataOptionEvent.Set event, @Named("handler") FromUniqueService handler)
	{
		if(owner.service.isSelf(handler) || event.getSubjectData() != this)
			return;
		
		this.setOption(event.getContexts().get(), event.getKey(), event.getValue(), Misc.nosql() | Misc.sync());
	}
	
	@Listener
	public void _SYNC_onOptionRemove(SubjectDataOptionEvent.Remove event, @Named("handler") FromUniqueService handler)
	{
		if(owner.service.isSelf(handler) || event.getSubjectData() != this)
			return;
		
		this.setOption(event.getContexts().get(), event.getKey(), null, Misc.nosql() | Misc.sync());
	}
	
	@Listener
	public void _SYNC_onInheritanceClear(SubjectDataInheritanceEvent.Clear event, @Named("handler") FromUniqueService handler)
	{
		if(owner.service.isSelf(handler) || event.getSubjectData() != this)
			return;
		
		if(event.forAnyContext())
			this.clearParents(Misc.nosql());
		else
			this.clearParents(event.getContexts().get(), Misc.nosql() | Misc.sync());
	}
	
	@Listener
	public void _SYNC_onInheritanceAdd(SubjectDataInheritanceEvent.Add event, @Named("handler") FromUniqueService handler)
	{
		if(owner.service.isSelf(handler) || event.getSubjectData() != this)
			return;
		
		this.addParent(event.getContexts().get(), event.getAddedParent(), Misc.nosql() | Misc.sync());
	}
	
	@Listener
	public void _SYNC_onInheritanceRemove(SubjectDataInheritanceEvent.Remove event, @Named("handler") FromUniqueService handler)
	{
		if(owner.service.isSelf(handler) || event.getSubjectData() != this)
			return;
		
		this.removeParent(event.getContexts().get(), event.getRemovedParent(), Misc.nosql() | Misc.sync());
	}
	
	final String identifier;
	
	final Map<Set<Context>, NodeTree> trees;
	
	final Map<Set<Context>, Map<String, String>> options;
	
	final Map<Set<Context>, List<String>> parents;
	
	final SubjectImpl owner;
	
	final String table;
}
