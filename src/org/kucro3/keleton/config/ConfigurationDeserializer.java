package org.kucro3.keleton.config;

public interface ConfigurationDeserializer<T> extends ConfigurationOperatorOwned {
	public Configuration<T> deserializeFrom(T source) throws ConfigurationException;
	
	public Class<T> getSourceType();
}
