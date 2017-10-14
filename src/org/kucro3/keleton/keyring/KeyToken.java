package org.kucro3.keleton.keyring;

import java.util.Optional;
import java.util.function.Supplier;

public class KeyToken<T> {
	public static <T> KeyToken<T> of(Key<T> key)
	{
		return of(key, ObjectService.container);
	}
	
	public static <T> KeyToken<T> of(Key<T> key, ObjectContainer container)
	{
		return new KeyToken<T>(key, container);
	}
	
	KeyToken(Key<T> key, ObjectContainer container)
	{
		this.key = key;
		this.container = container;
	}
	
	public Key<T> getKey()
	{
		return key;
	}
	
	public ObjectContainer getContainer()
	{
		return container;
	}
	
	public Optional<T> get()
	{
		return container.get(key);
	}

	public <X extends Throwable> T get(Supplier<X> exceptionSupplier) throws X
	{
		return get().orElseThrow(exceptionSupplier);
	}

	public void put(T value)
	{
		container.put(key, value);
	}

	private final Key<T> key;
	
	private final ObjectContainer container;
}
