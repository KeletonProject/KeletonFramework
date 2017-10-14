package org.kucro3.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Reference<T> implements Supplier<T>, Consumer<T> {
	public Reference(T value)
	{
		this.value = value;
	}
	
	public Reference()
	{
	}
	
	@Override
	public T get()
	{
		return value;
	}
	
	public void set(T value)
	{
		this.value = value;
	}
	
	@Override
	public void accept(T t) 
	{
		set(t);
	}
	
	private T value;
}
