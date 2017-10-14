package org.kucro3.keleton.impl.config;

import org.kucro3.keleton.config.Configuration;
import org.kucro3.keleton.config.ConfigurationOperator;
import org.kucro3.keleton.config.ConfigurationSection;

public interface ConfigurationImpl {
	static class ForKlink<T> implements Configuration<T>
	{
		ForKlink(ConfigurationOperatorImpl.ForKlink from, Class<T> sourceType, T source)
		{
			this.from = from;
			this.source = source;
			this.sourceType = sourceType;
		}
		
		void setSection(ConfigurationSectionImpl.ForKlink<T> section)
		{
			this.section = section;
		}
		
		@Override
		public ConfigurationOperator from() 
		{
			return from;
		}

		@Override
		public ConfigurationSection<T> getSection() 
		{
			return section;
		}

		@Override
		public T getSource() 
		{
			return source;
		}

		@Override
		public Class<T> getSourceType() 
		{
			return sourceType;
		}
		
		private final T source;
		
		private final Class<T> sourceType;
		
		private ConfigurationSectionImpl.ForKlink<T> section;
		
		private final ConfigurationOperatorImpl.ForKlink from;
	}
}
