package org.kucro3.keleton.config;

public interface ConfigurationSerializer<T> extends ConfigurationOperatorOwned {
	void serializeTo(Configuration<T> config, T dest) throws ConfigurationException;
	
	Class<T> getDestinationType();
}
