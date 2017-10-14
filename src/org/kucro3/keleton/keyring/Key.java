package org.kucro3.keleton.keyring;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Key<T> implements Typed {
	Key(String name, Object handle, Class<T> type)
	{
		this.name = name;
		this.handle = handle;
		this.type = type;
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public Class<T> type()
	{
		return type;
	}
	
	public Object getHandle()
	{
		return handle;
	}
	
	public static Key<Object> of(String name)
	{
		return of(name, name);
	}
	
	public static Key<Object> of(String name, Object handle)
	{
		return of(name, handle, Object.class);
	}
	
	public static <T> Key<T> of(String name, Object handle, Class<T> type)
	{
		Objects.requireNonNull(name);
		Objects.requireNonNull(type);
		if(handle == null)
			handle = name;
		Key<?> cr = map.get(handle);
		Key<T> key;
		if(cr == null)
			map.put(handle, key = new Key<>(name, handle, type));
		else
			key = checkType(cr, type);
		return key;
	}
	
	public static Optional<Key<?>> get(Object handle)
	{
		Objects.requireNonNull(handle);
		return Optional.ofNullable(map.get(handle));
	}
	
	public static <T> Optional<Key<T>> get(Object handle, Class<T> type)
	{
		Objects.requireNonNull(handle);
		Objects.requireNonNull(type);
		Optional<Key<?>> cr = Optional.ofNullable(map.get(handle));
		Key<T> key = null;
		if(cr.isPresent())
			key = checkType(cr.get(), type);
		return Optional.of(key);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Key<T> checkType(Key<?> key, Class<?> type)
	{
		if(key.type() != type)
			throw new ClassCastException(key.type().getCanonicalName() + " cannot be cast to " + type.getCanonicalName());
		return (Key<T>) key;
	}
	
	private static final Map<Object, Key<?>> map = new HashMap<>();
	
	private final Class<T> type;
	
	private final Object handle;
	
	private final String name;
}