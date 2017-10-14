package org.kucro3.keleton.keyring;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ObjectContainer {
	public ObjectContainer()
	{
	}
	
	public <T> Optional<T> get(Key<?> key, Class<T> type)
	{
		Objects.requireNonNull(key);
		Objects.requireNonNull(type);
		return get(Key.checkType(key, type));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> Optional<T> get(Key<T> key)
	{
		Objects.requireNonNull(key);
		return (Optional) Optional.ofNullable(map.get(key.getHandle()));
	}
	
	public boolean contains(Key<?> key)
	{
		return map.containsKey(key.getHandle());
	}
	
	public <T> void put(Key<T> key, T value)
	{
		map.put(key.getHandle(), value);
	}
	
	public <T> void put(Key<?> key, Class<T> type, T value)
	{
		put(Key.checkType(key, type), value);
	}
	
	public <T> Optional<T> remove(Key<?> key, Class<T> type)
	{
		return remove(Key.checkType(key, type));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> Optional<T> remove(Key<T> key)
	{
		return (Optional) Optional.ofNullable(map.remove(key.getHandle()));
	}
	
	private final Map<Object, Object> map = new HashMap<>();
}