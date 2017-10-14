package org.kucro3.keleton.service.permission;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.kucro3.keleton.permission.EnhancedSubject;
import org.kucro3.keleton.permission.EnhancedSubjectData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.util.Tristate;

@SuppressWarnings("unchecked")
public class SubjectImpl implements EnhancedSubject {
	SubjectImpl(PermissionServiceImpl service, 
			SubjectCollectionImpl owner, String table, String identifier, Supplier<? extends CommandSource> source) throws SQLException
	{
		this(service, owner, table, identifier, source, 0);
	}
	
	SubjectImpl(PermissionServiceImpl service, 
			SubjectCollectionImpl owner, String table, String identifier, Supplier<? extends CommandSource> source,
			int option) throws SQLException
	{
		this.service = service;
		this.identifier = identifier;
		this.source = source;
		this.owner = owner;
		this.data = Misc.nosql(option) ? new SubjectDataImpl(this, table, identifier) : SubjectDataImpl.fromTable(this, table, identifier);
		this.parents = new HashMap<>();
		this.transientData = new TransientSubjectDataImpl(service);
	}
	
	@Override
	public Set<Context> getActiveContexts()
	{
		return service.accumulate(this);
	}

	@Override
	public String getIdentifier() 
	{
		return this.identifier;
	}

	@Override
	public Optional<CommandSource> getCommandSource()
	{
		return Optional.ofNullable(source.get());
	}

	@Override
	public SubjectCollection getContainingCollection() 
	{
		return owner;
	}

	@Override
	public Optional<String> getOption(Set<Context> contexts, String key) 
	{
		Map<String, String> options = transientData.getOptions(contexts);
		String option;
		
		if((option = options.get(key)) != null)
			return Optional.of(option);
		
		options = data.getOptions(contexts);
		
		if((option = options.get(key)) != null)
			return Optional.of(option);
		
		return Optional.empty();
	}
	
	@Override
	public List<Subject> getParents(Set<Context> contexts)
	{
		List<Subject> subjects = parents.get(contexts);
		if(subjects == null)
			return Collections.EMPTY_LIST;
		List<Subject> copy = new ArrayList<>(subjects);
		return Collections.unmodifiableList(copy);
	}
	
	Tristate getOriginalPermissionValue(Set<Context> contexts, String permission)
	{
		EnhancedSubjectData subjectData = getSubjectData();
		EnhancedSubjectData transientData = getTransientSubjectData();
		
		Tristate value = transientData.getPermission(contexts, permission);
		if(value.equals(Tristate.UNDEFINED))
			return subjectData.getPermission(contexts, permission);
		return value;
	}

	@Override
	public Tristate getPermissionValue(Set<Context> contexts, String permission) 
	{
		if(!service.matches(this, contexts))
			return Tristate.FALSE;
		
		Tristate fromServiceDefault = service.getDefaults().getOriginalPermissionValue(contexts, permission);
		Tristate fromDefault = owner.getDefaults().getOriginalPermissionValue(contexts, permission);
		Tristate fromParents = Tristate.UNDEFINED;
		Tristate fromSelf = this.getOriginalPermissionValue(contexts, permission);
		
		Tristate value = Tristate.UNDEFINED;
		
		PERM_DEFAULT: {
			if(fromServiceDefault.equals(Tristate.UNDEFINED))
				break PERM_DEFAULT;
			
			value = fromServiceDefault;
		}
		
		DEFAULT: {
			if(fromDefault.equals(Tristate.UNDEFINED))
				break DEFAULT;
			
			value = fromDefault;
		}
		
		PARENTS: {
			for(Subject parent : getParents(contexts))
				fromParents.and(parent.getPermissionValue(contexts, permission));
			
			if(fromDefault.equals(Tristate.UNDEFINED))
				break PARENTS;
			
			value = fromParents;
		}
		
		SELF: {
			if(fromSelf.equals(Tristate.UNDEFINED))
				break SELF;
			
			value = fromSelf;
		}
		
		return value;
	}

	@Override
	public EnhancedSubjectData getSubjectData() 
	{
		return data;
	}

	@Override
	public EnhancedSubjectData getTransientSubjectData()
	{
		return transientData;
	}

	@Override
	public boolean isChildOf(Set<Context> contexts, Subject parent) 
	{
		List<Subject> subjects = parents.get(contexts);
		
		if(subjects == null)
			return false;
		
		return subjects.contains(parent);
	}
	
	public SubjectImpl _ENABLE_()
	{
		Sponge.getEventManager().registerListeners(SpongeMain.getInstance(), this.data);
		Sponge.getEventManager().registerListeners(SpongeMain.getInstance(), this.transientData);
		return this;
	}
	
	public SubjectImpl _DISABLE_()
	{
		Sponge.getEventManager().unregisterListeners(this.data);
		Sponge.getEventManager().unregisterListeners(this.transientData);
		return this;
	}
	
	private final Map<Set<Context>, List<Subject>> parents;
	
	private final SubjectDataImpl data;
	
	final String identifier;
	
	private final Supplier<? extends CommandSource> source;
	
	private final SubjectCollectionImpl owner;
	
	private final TransientSubjectDataImpl transientData;
	
	final PermissionServiceImpl service;
}
