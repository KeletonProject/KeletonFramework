package org.kucro3.keleton.impl.auth;

class Reference<T> {
	Reference()
	{
	}
	
	public Reference(T value)
	{
		this.value = value;
	}
	
	public T value;
}