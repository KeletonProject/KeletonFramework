package org.kucro3.keleton.config;

import java.util.Collection;
import java.util.Optional;

public interface ConfigurationSection<T> {
	public Configuration<T> from();
	
	public boolean hasValue(String name);
	
	public Optional<ConfigurationValueType<?>> getValueType(String name);
	
	public default Optional<String> getString(String name)
	{
		return get(name, String.class);
	}
	
	public default Optional<Boolean> getBoolean(String name)
	{
		return get(name, Boolean.class);
	}
	
	public default Optional<Byte> getByte(String name)
	{
		return get(name, Byte.class);
	}
	
	public default Optional<Short> getShort(String name)
	{
		return get(name, Short.class);
	}
	
	public default Optional<Integer> getInt(String name)
	{
		return get(name, Integer.class);
	}
	
	public default Optional<Long> getLong(String name)
	{
		return get(name, Long.class);
	}
	
	public default Optional<Float> getFloat(String name)
	{
		return get(name, Float.class);
	}
	
	public default Optional<Double> getDouble(String name)
	{
		return get(name, Double.class);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public default Optional<ConfigurationSection<T>> getSection(String name)
	{
		return (Optional) get(name, ConfigurationSection.class);
	}
	
	public <E> Collection<ConfigurationEntry<E>> entries();
	
	public <E> Optional<E> get(String name, Class<E> type);
	
	public <E> Optional<ConfigurationEntry<E>> getEntry(String name, Class<E> type);
	
	public <E> Optional<ConfigurationEntry<E>> getEntry(String name);
	
	public default boolean putString(String name, String value)
	{
		return put(name, String.class, value);
	}
	
	public default boolean putBoolean(String name, boolean value)
	{
		return put(name, Boolean.class, value);
	}
	
	public default boolean putByte(String name, byte value)
	{
		return put(name, Byte.class, value);
	}
	
	public default boolean putShort(String name, short value)
	{
		return put(name, Short.class, value);
	}
	
	public default boolean putInt(String name, int value)
	{
		return put(name, Integer.class, value);
	}
	
	public default boolean putLong(String name, long value)
	{
		return put(name, Long.class, value);
	}
	
	public default boolean putFloat(String name, float value)
	{
		return put(name, Float.class, value);
	}
	
	public default boolean putDouble(String name, double value)
	{
		return put(name, Double.class, value);
	}
	
	public default boolean putSection(String name, ConfigurationSection<T> section)
	{
		return put(name, ConfigurationSection.class, section);
	}
	
	public <E> boolean put(String name, Class<E> type, E value);
	
	public void clear();
	
	public Optional<String> getName();
	
	public Optional<ConfigurationSection<T>> getParent();
	
	public default boolean hasParent()
	{
		return getParent().isPresent();
	}
}
