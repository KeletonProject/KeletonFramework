package org.kucro3.keleton.config;

import java.util.Optional;

public interface Configuration<T> {
	public T getSource();
	
	public Class<T> getSourceType();
	
	public ConfigurationOperator from();
	
	public ConfigurationSection<T> getSection();
	
	public default boolean save() throws ConfigurationException
	{
		return saveTo(getSourceType(), getSource());
	}
	
	public default boolean saveTo(Class<T> type, T container) throws ConfigurationException
	{
		Optional<ConfigurationSerializer<T>> serializer = from().getSerializer(type);
		if(!serializer.isPresent())
			throw new ConfigurationException.DestinationNotSupported(type.getCanonicalName());
		serializer.get().serializeTo(this, container);
		return true;
	}
}
