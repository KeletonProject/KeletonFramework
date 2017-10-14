package org.kucro3.keleton.impl.config;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.kucro3.keleton.config.Configuration;
import org.kucro3.keleton.config.ConfigurationEntry;
import org.kucro3.keleton.config.ConfigurationSection;
import org.kucro3.keleton.config.ConfigurationValueType;
import org.kucro3.keleton.impl.config.klink.TypeRedirected;
import org.kucro3.keleton.impl.config.klink.TypeWeaken;
import org.kucro3.klink.MappedVariables;
import org.kucro3.klink.Variables.Variable;

@SuppressWarnings({ "rawtypes", "unchecked" })
interface ConfigurationSectionImpl {
	static class ForKlink<T> implements ConfigurationSection<T>
	{
		// root
		ForKlink(Configuration<T> source)
		{
			this(null, source, null);
		}
		
		ForKlink(String name, Configuration<T> source, ConfigurationSection<T> parent)
		{
			this.source = source;
			this.name = name;
			this.parent = parent;
		}
		
		@Override
		public void clear()
		{
			vars.clearVars();
		}

		@Override
		public Configuration<T> from() 
		{
			return source;
		}
		
		@Override
		public <E> Optional<E> get(String name, Class<E> type) 
		{
			Optional<Variable> optionalVar = vars.getVar(name);
			if(!optionalVar.isPresent())
				return Optional.empty();
			
			Typed<?> unknownTyped = (Typed<?>) optionalVar.get();
			if((!unknownTyped.getType().getType().equals(type)) || (!unknownTyped.defined()))
				return Optional.empty();
			
			Typed<E> typed = (Typed<E>) unknownTyped;
			return typed.getType().tryCast(typed.get());
		}

		@Override
		public Optional<String> getName()
		{
			return Optional.ofNullable(name);
		}

		@Override
		public Optional<ConfigurationSection<T>> getParent() 
		{
			return Optional.ofNullable(parent);
		}

		@Override
		public Optional<ConfigurationValueType<?>> getValueType(String name)
		{
			Optional<Variable> origin = vars.getVar(name);
			if(!origin.isPresent())
				return Optional.empty();
			Typed<?> typed = (Typed<?>) origin.get();
			return Optional.of(typed.getType());
		}

		@Override
		public boolean hasValue(String name) 
		{
			return vars.hasVar(name);
		}

		@Override
		public <E> boolean put(String name, Class<E> type, E value)
		{
			Optional<ConfigurationValueType<E>> optionalType = source.from().getType(type);
			if(!optionalType.isPresent())
				return false;
			
			Typed<?> typed;
			Optional<Variable> var = vars.getVar(name);
			if(var.isPresent())
				typed = (Typed<?>) var.get();
			else
				vars.putVar(typed = new Typed<E>(name));
			
			ConfigurationValueType<E> sectionType = optionalType.get();
			((Typed<E>) typed).type = sectionType;
			((Typed<E>) typed).obj = value;
				
			return true;
		}
		
		@Override
		public <E> Collection<ConfigurationEntry<E>> entries() 
		{
			return (Collection) vars.getVars();
		}

		@Override
		public <E> Optional<ConfigurationEntry<E>> getEntry(String name, Class<E> type) 
		{
			Optional<Variable> var = vars.getVar(name);
			if(!var.isPresent())
				return Optional.empty();
			ConfigurationEntry<?> entry = (ConfigurationEntry<?>) var.get();
			if(!entry.getType().getType().equals(type))
				return Optional.empty();
			return Optional.of((ConfigurationEntry<E>) entry);
		}
		
		@Override
		public <E> Optional<ConfigurationEntry<E>> getEntry(String name) 
		{
			Optional<Variable> var = vars.getVar(name);
			if(!var.isPresent())
				return Optional.empty();
			return Optional.of((ConfigurationEntry<E>) var.get());
		}
		
		private final ConfigurationSection<T> parent;
		
		private final String name;
		
		private final Configuration<T> source;
		
		final MappedVariables vars = new MappedVariables(Typed::new)
			{
				@Override
				public void forceVar(Variable var)
				{
					Objects.requireNonNull(var);
					if(!(var instanceof ForKlink<?>.Typed<?>))
						throw new IllegalStateException("Not puttable");
					super.forceVar(var);
				}
				
				@Override
				public void putVar(Variable var)
				{
					Objects.requireNonNull(var);
					if(!(var instanceof ForKlink<?>.Typed<?>))
						throw new IllegalStateException("Not puttable");
					super.putVar(var);
				}
			};
		
		class Typed<E> implements ConfigurationEntry<E>, Variable
		{
			Typed(String name)
			{
				this.name = name;
			}
			
			Object getRaw()
			{
				return obj;
			}
			
			final E cast()
			{
				return (E) getRaw();
			}
			
			public ConfigurationValueType<E> getType()
			{
				return type;
			}
			
			@Override
			public E get() 
			{
				return obj;
			}

			@Override
			public void set(Object object)
			{
				if(object instanceof TypeWeaken)
					if(defined())
						put(type.tryCast(((TypeWeaken) object).getValue()).orElseThrow(() -> new IllegalStateException("Class cast (" + name + ")")));
					else
						throw new IllegalStateException("Type not defined (" + name + ")");
				else
				{
					Class<?> type;
					Optional<ConfigurationValueType<?>> sectionType;
					if(object instanceof TypeRedirected<?>)
					{
						type = ((TypeRedirected<E>) object).getType();
						object = ((TypeRedirected<E>) object).getValue();
					}
					else
						type = object.getClass();
					
					sectionType = (Optional) from().from().getType(type);
					if(!sectionType.isPresent())
						throw new IllegalStateException("Type " + type.getCanonicalName() + " not supported (" + name + ")");
					
					this.type = (ConfigurationValueType<E>) sectionType.get();
					this.obj = (E) object;
				}
			}

			@Override
			public String getName() 
			{
				return name;
			}

			@Override
			public void put(E val) 
			{
				this.obj = val;
			}

			@Override
			public boolean setName(String name, boolean replace)
			{
				if(this.name.equals(name))
					return true;
				
				if((!replace) && vars.hasVar(name))
					return false;
					
				vars.removeVar(this.name);
				this.name = name;
				vars.forceVar(this);
				
				return true;
			}
			
			boolean defined()
			{
				return type != null;
			}
			
			private String name;
			
			private E obj;
			
			private ConfigurationValueType<E> type;
		}
	}
}
