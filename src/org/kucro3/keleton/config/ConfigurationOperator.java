package org.kucro3.keleton.config;

import java.util.Optional;

public interface ConfigurationOperator {
	public default boolean containsType(Class<?> type)
	{
		return getType(type).isPresent();
	}
	
	public <T> Optional<ConfigurationValueType<T>> getType(Class<T> type);
	
	public default <T> Configuration<T> readConfiguration(Class<T> type, T source) throws ConfigurationException
	{
		Optional<ConfigurationDeserializer<T>> optionalReader = getDeserializer(type);
		if(!optionalReader.isPresent())
			throw new ConfigurationException.SourceNotSupported(type.getCanonicalName());
		return optionalReader.get().deserializeFrom(source);
	}
	
	public <T> Optional<ConfigurationDeserializer<T>> getDeserializer(Class<T> type);
	
	public default <T> boolean containsDeserializer(Class<T> type)
	{
		return getDeserializer(type).isPresent();
	}
	
	public <T> Optional<ConfigurationSerializer<T>> getSerializer(Class<T> type);
	
	public default <T> boolean containsSerializer(Class<T> type)
	{
		return getSerializer(type).isPresent();
	}
}
