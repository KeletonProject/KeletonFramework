package org.kucro3.keleton.impl.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.kucro3.keleton.config.Configuration;
import org.kucro3.keleton.config.ConfigurationDeserializer;
import org.kucro3.keleton.config.ConfigurationEntry;
import org.kucro3.keleton.config.ConfigurationException;
import org.kucro3.keleton.config.ConfigurationOperator;
import org.kucro3.keleton.config.ConfigurationSerializer;
import org.kucro3.keleton.config.ConfigurationValueType;
import org.kucro3.keleton.impl.config.klink.CustomType;
import org.kucro3.klink.Executable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.SequenceUtil;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.syntax.Sequence;

@SuppressWarnings("unchecked")
public interface ConfigurationOperatorImpl {
	static class ForKlink implements ConfigurationOperator
	{
		ForKlink(Klink engine)
		{
			this.engine = engine;
			this.map = new HashMap<>();
			this.readers = new HashMap<>();
			this.writers = new HashMap<>();
			ConfigurationSectionTypeImpl.ForKlink.bind(this);
			Reader.bind(this);
			Writer.bind(this);
		}
		
		<T> void bindCustom(CustomType<T> custom)
		{
			ConfigurationSectionTypeImpl.ForKlink.bindCustom(this, custom);
		}

		@Override
		public <T> Optional<ConfigurationDeserializer<T>> getDeserializer(Class<T> type)
		{
			return Optional.ofNullable((ConfigurationDeserializer<T>) readers.get(type));
		}
		
		@Override
		public <T> Optional<ConfigurationValueType<T>> getType(Class<T> type) 
		{
			return Optional.ofNullable((ConfigurationValueType<T>) map.get(type));
		}
		
		@Override
		public <T> Optional<ConfigurationSerializer<T>> getSerializer(Class<T> type) 
		{
			return Optional.ofNullable((ConfigurationSerializer<T>) writers.get(type));
		}
		
		final Klink engine;
		
		final Map<Class<?>, ConfigurationValueType<?>> map;
		
		final Map<Class<?>, ConfigurationDeserializer<?>> readers;
		
		final Map<Class<?>, ConfigurationSerializer<?>> writers;
		
		static abstract class Reader<T> implements ConfigurationDeserializer<T>
		{
			Reader(ConfigurationOperatorImpl.ForKlink owner, Class<T> type)
			{
				this.owner = owner;
				this.type = type;
			}
			
			static <T> ConfigurationSectionImpl.ForKlink<T> readMainSection(ConfigurationOperatorImpl.ForKlink owner, 
					ConfigurationImpl.ForKlink<T> config, Sequence seq) throws ConfigurationException 
			{			
				try {
					Executable exec = owner.engine.compile(seq);
					
					ConfigurationSectionImpl.ForKlink<T> main
							= new ConfigurationSectionImpl.ForKlink<>(config);
					
					String oldEnv = owner.engine.currentEnv().getName();
					String newEnv = config.getSource().toString();
					
					owner.engine.createEnv(newEnv);
					owner.engine.currentEnv(newEnv);
					owner.engine.currentEnv().getRegisters().OR[0] = config;
					owner.engine.currentEnv().getRegisters().OR[1] = main;
					owner.engine.currentEnv().setVars(main.vars);
					exec.execute(owner.engine);
					owner.engine.currentEnv(oldEnv);
					
					config.setSection(main);
					
					return main;
				} catch (ScriptException e) {
					if(e.getCause() instanceof IOException)
						throw new ConfigurationException.IO((IOException) e.getCause());
					throw e;
				}
			}
			
			static class FileReader extends Reader<File>
			{
				FileReader(ForKlink owner)
				{
					super(owner, File.class);
				}

				@Override
				public Configuration<File> deserializeFrom(File file) throws ConfigurationException 
				{
					try {
						BufferedInputStream is = null;
						try {
							is = new BufferedInputStream(new FileInputStream(file));
							ConfigurationImpl.ForKlink<File> config
								= new ConfigurationImpl.ForKlink<File>(owner, File.class, file);
							readMainSection(owner, config, SequenceUtil.readFrom(is));
							return config;
						} finally {
							if(is != null)
								is.close();
						}
					} catch (IOException e) {
						throw new ConfigurationException.IO(e);
					}
				}
				
				static void bind(ForKlink operator)
				{
					operator.readers.put(File.class, new FileReader(operator));
				}
			}
			
			static class InputStreamReader extends Reader<InputStream>
			{
				InputStreamReader(ConfigurationOperatorImpl.ForKlink owner)
				{
					super(owner, InputStream.class);
				}

				@Override
				public Configuration<InputStream> deserializeFrom(InputStream is) throws ConfigurationException 
				{
					ConfigurationImpl.ForKlink<InputStream> config
							= new ConfigurationImpl.ForKlink<InputStream>(owner, InputStream.class, is);
					readMainSection(owner, config, SequenceUtil.readFrom(is));
					return config;
				}
				
				static void bind(ForKlink operator)
				{
					operator.readers.put(InputStream.class, new InputStreamReader(operator));
				}
			}
			
			static void bind(ForKlink operator)
			{
				InputStreamReader.bind(operator);
				FileReader.bind(operator);
			}
			
			@Override
			public ConfigurationOperator worksFor()
			{
				return owner;
			}
			
			@Override
			public Class<T> getSourceType()
			{
				return type;
			}
			
			final ConfigurationOperatorImpl.ForKlink owner;
			
			private final Class<T> type;
		}
		
		static abstract class Writer<T> implements ConfigurationSerializer<T>
		{
			Writer(ConfigurationOperatorImpl.ForKlink owner, Class<T> type)
			{
				this.owner = owner;
				this.type = type;
			}
			
			static <T> void writeSection(ConfigurationSectionImpl.ForKlink<T> section, OutputStream os) throws ConfigurationException
			{
				writeSection(section, os, "");
			}
			
			static <T> void writeSection(ConfigurationSectionImpl.ForKlink<T> section, OutputStream os, String prefix) throws ConfigurationException
			{
				StringBuilder builder = new StringBuilder(prefix);
				for(ConfigurationEntry<?> entry : section.entries())
				{
					String name = entry.getName();
					String type = entry.getType().getName();
					ConfigurationSectionTypeImpl.ForKlink<Object> handle 
						= (org.kucro3.keleton.impl.config.ConfigurationSectionTypeImpl.ForKlink<Object>) entry.getType();
					String value = handle.decompile(entry.get());
					
					builder.append("#")
						.append(" ")
						.append("$")
						.append(name)
						.append(" ")
						.append(type)
						.append(" ")
						.append(value)
						.append(" ")
						.append(";")
						.append("\n");
				}
				String str = builder.toString();
				try {
					os.write(str.getBytes());
				} catch (IOException e) {
					throw new ConfigurationException.IO(e);
				}
			}
			
			static class OutputStreamWriter extends Writer<OutputStream>
			{
				OutputStreamWriter(ForKlink owner)
				{
					super(owner, OutputStream.class);
				}

				@Override
				public void serializeTo(Configuration<OutputStream> config, OutputStream os)
						throws ConfigurationException {
					writeSection((ConfigurationSectionImpl.ForKlink<OutputStream>) config.getSection(), os);
				}
				
				static void bind(ForKlink operator)
				{
					operator.writers.put(OutputStream.class, new OutputStreamWriter(operator));
				}
			}
			
			static class FileWriter extends Writer<File>
			{
				FileWriter(ForKlink owner) 
				{
					super(owner, File.class);
				}

				@Override
				public void serializeTo(Configuration<File> config, File file) throws ConfigurationException 
				{
					try {
						if(!file.exists() || !file.isFile())
							file.createNewFile();
						BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
						writeSection((ConfigurationSectionImpl.ForKlink<File>) config.getSection(), os);
						os.flush();
						os.close();
					} catch (IOException e) {
						throw new ConfigurationException.IO(e);
					}
				}
				
				static void bind(ForKlink operator)
				{
					operator.writers.put(File.class, new FileWriter(operator));
				}
			}
			
			static void bind(ForKlink operator)
			{
				OutputStreamWriter.bind(operator);
				FileWriter.bind(operator);
			}
			
			@Override
			public ConfigurationOperator worksFor() 
			{
				return owner;
			}

			@Override
			public Class<T> getDestinationType() 
			{
				return type;
			}
			
			private final ConfigurationOperatorImpl.ForKlink owner;

			private final Class<T> type;
		}
	}
}
