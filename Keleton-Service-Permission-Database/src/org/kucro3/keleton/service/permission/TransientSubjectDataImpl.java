package org.kucro3.keleton.service.permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.kucro3.keleton.permission.EnhancedSubjectData;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.NodeTree;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.util.Tristate;

import com.google.common.collect.ImmutableMap;

public class TransientSubjectDataImpl implements EnhancedSubjectData {
	TransientSubjectDataImpl(PermissionServiceImpl service)
	{
		this.service = service;
	}
	
	@Override
	public boolean addParent(Set<Context> contexts, Subject parent) 
	{
		Optional<List<String>> optional;
		List<String> list = (optional = Optional.ofNullable(parents.get(contexts))).orElse(new ArrayList<>());
		
		list.add(parent.getIdentifier());
		
		if(!optional.isPresent())
			parents.put(contexts, list);
		
		return true;
	}

	@Override
	public boolean clearOptions() 
	{
		options.clear();
		return true;
	}

	@Override
	public boolean clearOptions(Set<Context> contexts)
	{
		options.remove(contexts);
		return true;
	}

	@Override
	public boolean clearParents() 
	{
		parents.clear();
		return true;
	}

	@Override
	public boolean clearParents(Set<Context> contexts) 
	{
		parents.remove(contexts);
		return true;
	}

	@Override
	public boolean clearPermissions() 
	{
		trees.clear();
		return true;
	}

	@Override
	public boolean clearPermissions(Set<Context> contexts) 
	{
		trees.remove(contexts);
		return true;
	}

	@Override
	public Map<Set<Context>, Map<String, String>> getAllOptions()
	{
		return Collections.unmodifiableMap(new HashMap<>(options));
	}

	@Override
	public Map<Set<Context>, List<Subject>> getAllParents() 
	{
		SubjectCollection collection = service.getGroupSubjects();
		Map<Set<Context>, List<Subject>> map = new HashMap<>();
		
		for(Map.Entry<Set<Context>, List<String>> entry : parents.entrySet())
		{
			List<Subject> subjects = new ArrayList<>();
			for(String parent : entry.getValue())
				subjects.add(collection.get(parent));
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
		Map<String, String> map;
		
		if((map = options.get(contexts)) == null)
			return Collections.emptyMap();
		
		return Collections.unmodifiableMap(map);
	}

	@Override
	public List<Subject> getParents(Set<Context> contexts) 
	{
		List<String> list;
		
		if((list = parents.get(contexts)) == null)
			return Collections.emptyList();
		
		List<Subject> subjects = new ArrayList<>();
		SubjectCollection collection = service.getGroupSubjects();
		
		for(String subject : list)
			subjects.add(collection.get(subject));
		
		return Collections.unmodifiableList(subjects);
	}

	@Override
	public Map<String, Boolean> getPermissions(Set<Context> contexts) 
	{
		NodeTree tree;
		
		if((tree = trees.get(contexts)) == null)
			return Collections.emptyMap();
		
		return Collections.unmodifiableMap(tree.asMap());
	}

	@Override
	public boolean removeParent(Set<Context> contexts, Subject parent)
	{
		List<String> list;
		
		if((list = parents.get(contexts)) == null)
			return true;
		
		list.remove(parent.getIdentifier());
		
		if(list.isEmpty())
			parents.remove(contexts);
		
		return true;
	}

	@Override
	public boolean setOption(Set<Context> contexts, String key, String value)
	{
		Optional<Map<String, String>> optional;
		Map<String, String> map = (optional = Optional.ofNullable(options.get(contexts))).orElse(new HashMap<>());
		
		map.put(key, value);
		
		if(!optional.isPresent())
			options.put(contexts, map);
		
		return true;
	}

	@Override
	public boolean setPermission(Set<Context> contexts, String permission, Tristate value) 
	{
		NodeTree tree;
		
		if((tree = trees.get(contexts)) == null)
			if(value.equals(Tristate.UNDEFINED))
				return true;
			else
				tree = NodeTree.of(ImmutableMap.of(permission, value.asBoolean()));
		else
			tree = Misc.merge(tree, permission, value);
		
		return true;
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
	public boolean isTransient()
	{
		return true;
	}
	
	final Map<Set<Context>, NodeTree> trees = new HashMap<>();
	
	final Map<Set<Context>, Map<String, String>> options = new HashMap<>();
	
	final Map<Set<Context>, List<String>> parents = new HashMap<>();
	
	final PermissionServiceImpl service;
}