package org.kucro3.keleton.config;

import java.util.Optional;

public interface ConfigurationValueType<T> extends ConfigurationOperatorOwned {
	public Class<T> getType();
	
	public Optional<T> tryCast(Object obj);
	
	public String getName();
}
