package org.kucro3.keleton.keyring;

import java.util.Optional;

public class ObjectService {
	private ObjectService()
	{
	}
	
	public static <T> Optional<T> get(Key<?> key, Class<T> type)
	{
		return container.get(key, type);
	}
	
	public static <T> Optional<T> get(Key<T> key)
	{
		return container.get(key);
	}
	
	public static boolean contains(Key<?> key)
	{
		return container.contains(key);
	}
	
	public static <T> void put(Key<T> key, T value)
	{
		container.put(key, value);
	}
	
	public static <T> void put(Key<?> key, Class<T> type, T value)
	{
		container.put(key, type, value);
	}
	
	public static <T> Optional<T> remove(Key<?> key, Class<T> type)
	{
		return container.remove(key, type);
	}
	
	public static <T> Optional<T> remove(Key<T> key)
	{
		return container.remove(key);
	}
	
	static final ObjectContainer container = new ObjectContainer();
}