package org.kucro3.keleton.config;

public interface ConfigurationEntry<E> {
	public E get();
	
	public void put(E value);
	
	public ConfigurationValueType<E> getType();
	
	public String getName();
	
	public boolean setName(String name, boolean replace);
}
