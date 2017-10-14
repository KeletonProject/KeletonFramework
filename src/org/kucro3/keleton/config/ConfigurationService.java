package org.kucro3.keleton.config;

import java.util.Optional;

public interface ConfigurationService {
	public Optional<ConfigurationOperator> getOperator(String name);
	
	public default boolean operatorAvailable(String name)
	{
		return getOperator(name).isPresent();
	}
}