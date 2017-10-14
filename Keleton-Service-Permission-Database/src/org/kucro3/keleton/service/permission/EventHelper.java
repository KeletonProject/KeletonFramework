package org.kucro3.keleton.service.permission;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.kucro3.keleton.cause.FromUniqueService;
import org.kucro3.keleton.permission.event.SubjectCollectionEvent;
import org.kucro3.keleton.permission.event.SubjectDataEvent;
import org.kucro3.keleton.permission.event.SubjectDataInheritanceEvent;
import org.kucro3.keleton.permission.event.SubjectDataOptionEvent;
import org.kucro3.keleton.permission.event.SubjectDataPermissionEvent;
import org.kucro3.keleton.permission.event.SubjectEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

public final class EventHelper {
	private EventHelper()
	{
	}
	
	static Cause fromHandler(UUID uid)
	{
		return Cause.builder().named("handler", (FromUniqueService) () -> uid).build();
	}
	
	public static Cause fromHandler(Cause cause, UUID uuid)
	{
		return cause.merge(fromHandler(uuid));
	}
	
	public static void fireSubjectCreateEvent(UUID uid, String identifier)
	{
		fireSubjectCreateEvent(fromHandler(uid), identifier);
	}
	
	public static void fireSubjectCreateEvent(Cause cause, String identifier)
	{
		Sponge.getEventManager().post(new SubjectCreateEventImpl(cause, identifier));
	}
	
	public static void fireSubjectCollectionCreateEvent(UUID uid, String identifier)
	{
		fireSubjectCollectionCreateEvent(fromHandler(uid), identifier);
	}
	
	public static void fireSubjectCollectionCreateEvent(Cause cause, String identifier)
	{
		Sponge.getEventManager().post(new SubjectCollectionCreateEventImpl(cause, identifier));
	}
	
	public static void fireSubjectDataPermissionClearEvent(UUID uid, Subject subject, SubjectData data,
			java.util.Set<Context> contexts)
	{
		fireSubjectDataPermissionClearEvent(fromHandler(uid), subject, data, contexts);
	}
	
	public static void fireSubjectDataPermissionClearEvent(Cause cause, Subject subject, SubjectData data,
			java.util.Set<Context> contexts)
	{
		Sponge.getEventManager().post(new SubjectDataPermissionClearEventImpl(cause, subject, data, contexts));
	}
	
	public static void fireSubjectDataPermissionClearAllEvent(UUID uid, Subject subject, SubjectData data)
	{
		fireSubjectDataPermissionClearEvent(uid, subject, data, null);
	}
	
	public static void fireSubjectDataPermissionClearAllEvent(Cause cause, Subject subject, SubjectData data)
	{
		fireSubjectDataPermissionClearEvent(cause, subject, data, null);
	}
	
	public static void fireSubjectDataPermissionSetEvent(UUID uid, Subject subject, SubjectData data,
			java.util.Set<Context> contexts, String permission, Tristate value)
	{
		fireSubjectDataPermissionSetEvent(uid, subject, data, contexts, permission, value);
	}
	
	public static void fireSubjectDataPermissionSetEvent(Cause cause, Subject subject, SubjectData data,
			java.util.Set<Context> contexts, String permission, Tristate value)
	{
		Sponge.getEventManager().post(new SubjectDataPermissionSetEventImpl(cause, subject, data, contexts, permission, value));
	}
	
	public static void fireSubjectDataOptionClearEvent(UUID uid, Subject subject, SubjectData data,
			java.util.Set<Context> contexts)
	{
		fireSubjectDataOptionClearEvent(fromHandler(uid), subject, data, contexts);
	}
	
	public static void fireSubjectDataOptionClearEvent(Cause cause, Subject subject, SubjectData data,
			java.util.Set<Context> contexts)
	{
		Sponge.getEventManager().post(new SubjectDataOptionClearEventImpl(cause, subject, data, contexts));
	}
	
	public static void fireSubjectDataOptionClearAllEvent(UUID uid, Subject subject, SubjectData data)
	{
		fireSubjectDataOptionClearEvent(uid, subject, data, null);
	}
	
	public static void fireSubjectDataOptionClearAllEvent(Cause cause, Subject subject, SubjectData data)
	{
		fireSubjectDataOptionClearEvent(cause, subject, data, null);
	}
	
	public static void fireSubjectDataOptionRemoveEvent(UUID uid, Subject subject, SubjectData data,
			java.util.Set<Context> contexts, String key)
	{
		fireSubjectDataOptionRemoveEvent(fromHandler(uid), subject, data, contexts, key);
	}
	
	public static void fireSubjectDataOptionRemoveEvent(Cause cause, Subject subject, SubjectData data,
			java.util.Set<Context> contexts, String key)
	{
		Sponge.getEventManager().post(new SubjectDataOptionRemoveEventImpl(cause, subject, data, contexts, key));
	}
	
	public static void fireSubjectDataOptionSetEvent(UUID uid, Subject subject, SubjectData data,
			java.util.Set<Context> contexts, String key, String value)
	{
		fireSubjectDataOptionSetEvent(fromHandler(uid), subject, data, contexts, key, value);
	}
	
	public static void fireSubjectDataOptionSetEvent(Cause cause, Subject subject, SubjectData data,
			java.util.Set<Context> contexts, String key, String value)
	{
		Sponge.getEventManager().post(new SubjectDataOptionSetEventImpl(cause, subject, data, contexts, key, value));
	}
	
	public static void fireSubjectDataInheritanceClearEvent(UUID uid, Subject subject, SubjectData data,
			java.util.Set<Context> contexts)
	{
		fireSubjectDataInheritanceClearEvent(fromHandler(uid), subject, data, contexts);
	}
	
	public static void fireSubjectDataInheritanceClearEvent(Cause cause, Subject subject, SubjectData data,
			java.util.Set<Context> contexts)
	{
		Sponge.getEventManager().post(new SubjectDataInheritanceClearEventImpl(cause, subject, data, contexts));
	}
	
	public static void fireSubjectDataInheritanceClearAllEvent(UUID uid, Subject subject, SubjectData data)
	{
		fireSubjectDataInheritanceClearEvent(uid, subject, data, null);
	}
	
	public static void fireSubjectDataInheritanceClearAllEvent(Cause cause, Subject subject, SubjectData data)
	{
		fireSubjectDataInheritanceClearEvent(cause, subject, data, null);
	}
	
	public static void fireSubjectDataInheritanceAddEvent(UUID uid, Subject subject, SubjectData data,
			java.util.Set<Context> contexts, Subject parent)
	{
		fireSubjectDataInheritanceAddEvent(fromHandler(uid), subject, data, contexts, parent);
	}
	
	public static void fireSubjectDataInheritanceAddEvent(Cause cause, Subject subject, SubjectData data,
			java.util.Set<Context> contexts, Subject parent)
	{
		Sponge.getEventManager().post(new SubjectDataInheritanceAddEventImpl(cause, subject, data, contexts, parent));
	}
	
	public static void fireSubjectDataInheritanceRemoveEvent(UUID uid, Subject subject, SubjectData data,
			java.util.Set<Context> contexts, Subject parent)
	{
		fireSubjectDataInheritanceRemoveEvent(fromHandler(uid), subject, data, contexts, parent);
	}
	
	public static void fireSubjectDataInheritanceRemoveEvent(Cause cause, Subject subject, SubjectData data,
			java.util.Set<Context> contexts, Subject parent)
	{
		Sponge.getEventManager().post(new SubjectDataInheritanceRemoveEventImpl(cause, subject, data, contexts, parent));
	}
	
	static abstract class AbstractSubjectEvent implements SubjectEvent
	{
		AbstractSubjectEvent(Cause cause)
		{
			this.cause = cause;
		}
		
		@Override
		public Cause getCause() 
		{
			return cause;
		}
		
		final Cause cause;
	}
	
	static class AbstractSubjectDataEvent extends AbstractSubjectEvent implements SubjectDataEvent
	{
		AbstractSubjectDataEvent(Cause cause, Subject subject, SubjectData data, java.util.Set<Context> contexts)
		{
			super(cause);
			this.subject = subject;
			this.data = data;
			this.contexts = Optional.ofNullable(contexts);
		}

		@Override
		public Subject getSubject()
		{
			return subject;
		}

		@Override
		public SubjectData getSubjectData() 
		{
			return data;
		}
		
		@Override
		public Optional<java.util.Set<Context>> getContexts()
		{
			return contexts;
		}
		
		final Optional<java.util.Set<Context>> contexts;
		
		final Subject subject;
		
		final SubjectData data;
	}
	
	static class SubjectCreateEventImpl extends AbstractSubjectEvent implements SubjectEvent.Create
	{
		SubjectCreateEventImpl(Cause cause, String identifier) 
		{
			super(cause);
			this.identifier = identifier;
		}

		@Override
		public String getSubjectIdentifier()
		{
			return identifier;
		}
		
		final String identifier;
	}
	
	static abstract class AbstractSubjectOperateEvent extends AbstractSubjectEvent implements SubjectEvent.Operate
	{
		AbstractSubjectOperateEvent(Cause cause, Subject subject)
		{
			super(cause);
			this.subject = subject;
		}

		@Override
		public Subject getSubject() 
		{
			return subject;
		}
		
		final Subject subject;
	}
	
	static class SubjectDataPermissionClearEventImpl extends AbstractSubjectDataEvent implements SubjectDataPermissionEvent.Clear
	{
		SubjectDataPermissionClearEventImpl(Cause cause, Subject subject, SubjectData data,
				java.util.Set<Context> contexts)
		{
			super(cause, subject, data, contexts);
		}
	}
	
	static class SubjectDataPermissionSetEventImpl extends AbstractSubjectDataEvent implements SubjectDataPermissionEvent.Set
	{
		SubjectDataPermissionSetEventImpl(Cause cause, Subject subject, SubjectData data,
				java.util.Set<Context> contexts, String permission, Tristate value)
		{
			super(cause, subject, data, contexts);
			this.permission = permission;
			this.value = value;
		}

		@Override
		public String getPermission() 
		{
			return permission;
		}

		@Override
		public Tristate getValue() 
		{
			return value;
		}
		
		final String permission;
		
		final Tristate value;
	}
	
	static class SubjectDataOptionClearEventImpl extends AbstractSubjectDataEvent implements SubjectDataOptionEvent.Clear
	{
		SubjectDataOptionClearEventImpl(Cause cause, Subject subject, SubjectData data,
				java.util.Set<Context> contexts)
		{
			super(cause, subject, data, contexts);
		}
	}
	
	static class SubjectDataOptionRemoveEventImpl extends AbstractSubjectDataEvent implements SubjectDataOptionEvent.Remove
	{
		SubjectDataOptionRemoveEventImpl(Cause cause, Subject subject, SubjectData data,
				java.util.Set<Context> contexts, String key) 
		{
			super(cause, subject, data, contexts);
			this.key = key;
		}

		@Override
		public String getKey()
		{
			return key;
		}
		
		final String key;
	}
	
	static class SubjectDataOptionSetEventImpl extends AbstractSubjectDataEvent implements SubjectDataOptionEvent.Set
	{
		SubjectDataOptionSetEventImpl(Cause cause, Subject subject, SubjectData data,
				java.util.Set<Context> contexts, String key, String value)
		{
			super(cause, subject, data, contexts);
			this.key = key;
			this.value = value;
		}

		@Override
		public String getKey() 
		{
			return key;
		}

		@Override
		public String getValue()
		{
			return value;
		}
		
		final String key;
		
		final String value;
	}
	
	static class SubjectDataInheritanceClearEventImpl extends AbstractSubjectDataEvent implements SubjectDataInheritanceEvent.Clear
	{
		SubjectDataInheritanceClearEventImpl(Cause cause, Subject subject, SubjectData data, Set<Context> contexts)
		{
			super(cause, subject, data, contexts);
		}
	}
	
	static class SubjectDataInheritanceAddEventImpl extends AbstractSubjectDataEvent implements SubjectDataInheritanceEvent.Add
	{
		SubjectDataInheritanceAddEventImpl(Cause cause, Subject subject, SubjectData data, Set<Context> contexts,
				Subject parent)
		{
			super(cause, subject, data, contexts);
			this.parent = parent;
		}

		@Override
		public Subject getAddedParent() 
		{
			return parent;
		}
		
		final Subject parent;
	}
	
	static class SubjectDataInheritanceRemoveEventImpl extends AbstractSubjectDataEvent implements SubjectDataInheritanceEvent.Remove
	{

		SubjectDataInheritanceRemoveEventImpl(Cause cause, Subject subject, SubjectData data, Set<Context> contexts,
				Subject parent)
		{
			super(cause, subject, data, contexts);
			this.parent = parent;
		}

		@Override
		public Subject getRemovedParent() 
		{
			return parent;
		}
		
		final Subject parent;
	}
	
	static abstract class AbstractSubjectCollectionEvent extends AbstractSubjectEvent implements SubjectCollectionEvent
	{
		AbstractSubjectCollectionEvent(Cause cause) 
		{
			super(cause);
		}
	}
	
	static class SubjectCollectionCreateEventImpl extends AbstractSubjectCollectionEvent implements SubjectCollectionEvent.Create
	{
		SubjectCollectionCreateEventImpl(Cause cause, String identifier)
		{
			super(cause);
			this.identifier = identifier;
		}

		@Override
		public String getCollectionIdentifier() 
		{
			return identifier;
		}
		
		final String identifier;
	}
}
