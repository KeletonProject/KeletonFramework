package org.kucro3.keleton.impl.config.klink;

public class TypeRedirected<T> {
	public TypeRedirected(Class<T> type, T value)
	{
		this.type = type;
		this.value = value;
	}
	
	public Class<T> getType()
	{
		return type;
	}
	
	public T getValue()
	{
		return value;
	}
	
	private final Class<T> type;
	
	private final T value;
}
