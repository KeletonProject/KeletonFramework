package org.kucro3.keleton.keyring;

import java.util.Optional;

public interface Pool<T> {
	public void remove(Key<T> key);
	
	public void put(Key<T> key, T value);
	
	public Optional<T> get(Key<T> key);
}