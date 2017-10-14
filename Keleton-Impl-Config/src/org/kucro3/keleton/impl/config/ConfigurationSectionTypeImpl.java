package org.kucro3.keleton.impl.config;

import java.util.Optional;

import org.kucro3.keleton.config.ConfigurationEntry;
import org.kucro3.keleton.config.ConfigurationOperator;
import org.kucro3.keleton.config.ConfigurationSection;
import org.kucro3.keleton.config.ConfigurationValueType;
import org.kucro3.keleton.impl.config.klink.CustomType;
import org.kucro3.keleton.impl.config.klink.TypeRedirected;
import org.kucro3.klink.Ref;
import org.kucro3.klink.Snapshot;
import org.kucro3.klink.Util;
import org.kucro3.klink.Variables;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

interface ConfigurationSectionTypeImpl {
	static abstract class ForKlink<T> implements ConfigurationValueType<T>
	{
		ForKlink(ConfigurationOperatorImpl.ForKlink operator, Class<T> type, java.lang.String name)
		{
			this.operator = operator;
			this.type = type;
			this.name = name;
		}
		
		@Override
		public ConfigurationOperator worksFor() 
		{
			return operator;
		}
		
		@Override
		public Class<T> getType()
		{
			return type;
		}
		
		@Override
		public java.lang.String getName()
		{
			return name;
		}
		
		public abstract java.lang.String decompile(T val);
		
		static void bind(ConfigurationOperatorImpl.ForKlink operator)
		{
			String.bind(operator);
			Boolean.bind(operator);
			Byte.bind(operator);
			Double.bind(operator);
			Float.bind(operator);
			Integer.bind(operator);
			Long.bind(operator);
			Short.bind(operator);
			Section.bind(operator);
		}
		
		static <E> void bindCustom(ConfigurationOperatorImpl.ForKlink operator, CustomType<E> custom)
		{
			Custom.bind(operator, custom);
		}
		
		private final Class<T> type;
		
		private final ConfigurationOperatorImpl.ForKlink operator;
		
		private final java.lang.String name;
		
		private static class Custom<T> extends ForKlink<T> implements ExpressionCompiler.Level3
		{
			Custom(org.kucro3.keleton.impl.config.ConfigurationOperatorImpl.ForKlink operator, CustomType<T> custom)
			{
				super(operator, custom.getType(), custom.getIdentifier());
				this.custom = custom;
			}

			@Override
			public Optional<T> tryCast(Object obj) 
			{
				return custom.getCaster().cast(obj);
			}
			
			@Override
			public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock,
					Snapshot snapshot) {
				return custom.getCompiler().compile(lib, refs, seq, codeBlock, snapshot);
			}
			
			static <T> void bind(ConfigurationOperatorImpl.ForKlink operator, CustomType<T> custom)
			{
				Custom<T> instance = new Custom<T>(operator, custom);
				operator.engine.getExpressions().putExpression(new Expression(custom.getIdentifier(), instance));
				operator.map.put(custom.getType(), instance);
			}
			
			@Override
			public java.lang.String decompile(T val) 
			{
				return custom.getDecompiler().decompile(val);
			}
			
			private final CustomType<T> custom;
		}
		
		private static class String extends ForKlink<java.lang.String> implements ExpressionCompiler.Level1
		{
			String(org.kucro3.keleton.impl.config.ConfigurationOperatorImpl.ForKlink operator)
			{
				super(operator, java.lang.String.class, "string");
			}

			@Override
			public Optional<java.lang.String> tryCast(Object obj) 
			{
				return obj instanceof java.lang.String ? Optional.of((java.lang.String) obj) : Optional.empty();
			}

			@Override
			public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq) 
			{
				final java.lang.String val = seq.leftToString();
				return (sys, env) -> {for(Ref ref : refs) ref.force(env, val);};
			}
			
			static void bind(ConfigurationOperatorImpl.ForKlink operator)
			{
				String instance = new String(operator);
				operator.engine.getExpressions().putExpression(new Expression("string", instance));
				operator.map.put(java.lang.String.class, instance);
			}

			@Override
			public java.lang.String decompile(java.lang.String val) 
			{
				return val;
			}
		}
		
		private static class Boolean extends ForKlink<java.lang.Boolean> implements ExpressionCompiler.Level1
		{
			Boolean(org.kucro3.keleton.impl.config.ConfigurationOperatorImpl.ForKlink operator)
			{
				super(operator, java.lang.Boolean.class, "boolean");
			}

			@Override
			public Optional<java.lang.Boolean> tryCast(Object obj) 
			{
				return obj instanceof java.lang.Boolean ? Optional.of((java.lang.Boolean) obj) : Optional.empty();
			}

			@Override
			public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq) 
			{
				final java.lang.Boolean val = Util.parseBoolean(seq.next());
				return (sys, env) -> {for(Ref ref : refs) ref.force(env, val);};
			}
			
			static void bind(ConfigurationOperatorImpl.ForKlink operator)
			{
				Boolean instance = new Boolean(operator);
				operator.engine.getExpressions().putExpression(new Expression("boolean", instance));
				operator.map.put(java.lang.Boolean.class, instance);
			}

			@Override
			public java.lang.String decompile(java.lang.Boolean val) 
			{
				return val.toString();
			}
		}
	
		private static class Byte extends ForKlink<java.lang.Byte> implements ExpressionCompiler.Level1
		{
			Byte(org.kucro3.keleton.impl.config.ConfigurationOperatorImpl.ForKlink operator) 
			{
				super(operator, java.lang.Byte.class, "byte");
			}

			@Override
			public Optional<java.lang.Byte> tryCast(Object obj) 
			{
				return obj instanceof java.lang.Byte ? Optional.of((java.lang.Byte) obj) : Optional.empty();
			}

			@Override
			public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq)
			{
				final java.lang.Byte val = Util.parseByte(seq.next());
				return (sys, env) -> {for(Ref ref : refs) ref.force(env, val);};
			}
			
			static void bind(ConfigurationOperatorImpl.ForKlink operator)
			{
				Byte instance = new Byte(operator);
				operator.engine.getExpressions().putExpression(new Expression("byte", instance));
				operator.map.put(java.lang.Byte.class, instance);
			}

			@Override
			public java.lang.String decompile(java.lang.Byte val) 
			{
				return val.toString();
			}
		}
	
		private static class Double extends ForKlink<java.lang.Double> implements ExpressionCompiler.Level1
		{
			Double(org.kucro3.keleton.impl.config.ConfigurationOperatorImpl.ForKlink operator)
			{
				super(operator, java.lang.Double.class, "double");
			}

			@Override
			public Optional<java.lang.Double> tryCast(Object obj) 
			{
				return obj instanceof java.lang.Double ? Optional.of((java.lang.Double) obj) : Optional.empty();
			}

			@Override
			public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq) 
			{
				final java.lang.Double val = Util.parseDouble(seq.next());
				return (sys, env) -> {for(Ref ref : refs) ref.force(env, val);};
			}
			
			static void bind(ConfigurationOperatorImpl.ForKlink operator)
			{
				Double instance = new Double(operator);
				operator.engine.getExpressions().putExpression(new Expression("double", instance));
				operator.map.put(java.lang.Double.class, instance);
			}

			@Override
			public java.lang.String decompile(java.lang.Double val) 
			{
				return val.toString();
			}
		}
	
		private static class Float extends ForKlink<java.lang.Float> implements ExpressionCompiler.Level1
		{
			Float(org.kucro3.keleton.impl.config.ConfigurationOperatorImpl.ForKlink operator) 
			{
				super(operator, java.lang.Float.class, "float");
			}

			@Override
			public Optional<java.lang.Float> tryCast(Object obj) 
			{
				return obj instanceof java.lang.Float ? Optional.of((java.lang.Float) obj) : Optional.empty();
			}

			@Override
			public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq) 
			{
				final java.lang.Float val = Util.parseFloat(seq.next());
				return (sys, env) -> {for(Ref ref : refs) ref.force(env, val);};
			}
			
			static void bind(ConfigurationOperatorImpl.ForKlink operator)
			{
				Float instance = new Float(operator);
				operator.engine.getExpressions().putExpression(new Expression("float", instance));
				operator.map.put(java.lang.Float.class, instance);
			}

			@Override
			public java.lang.String decompile(java.lang.Float val) 
			{
				return val.toString();
			}
		}
	
		private static class Integer extends ForKlink<java.lang.Integer> implements ExpressionCompiler.Level1
		{
			Integer(org.kucro3.keleton.impl.config.ConfigurationOperatorImpl.ForKlink operator) 
			{
				super(operator, java.lang.Integer.class, "int");
			}

			@Override
			public Optional<java.lang.Integer> tryCast(Object obj) 
			{
				return obj instanceof java.lang.Integer ? Optional.of((java.lang.Integer) obj) : Optional.empty();
			}

			@Override
			public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq) 
			{
				final java.lang.Integer val = Util.parseInt(seq.next());
				return (sys, env) -> {for(Ref ref : refs) ref.force(env, val);};
			}
			
			static void bind(ConfigurationOperatorImpl.ForKlink operator)
			{
				Integer instance = new Integer(operator);
				operator.engine.getExpressions().putExpression(new Expression("int", instance));
				operator.map.put(java.lang.Integer.class, instance);
			}

			@Override
			public java.lang.String decompile(java.lang.Integer val) 
			{
				return val.toString();
			}
		}
		
		private static class Long extends ForKlink<java.lang.Long> implements ExpressionCompiler.Level1
		{
			Long(org.kucro3.keleton.impl.config.ConfigurationOperatorImpl.ForKlink operator) 
			{
				super(operator, java.lang.Long.class, "long");
			}

			@Override
			public Optional<java.lang.Long> tryCast(Object obj) 
			{
				return obj instanceof java.lang.Long ? Optional.of((java.lang.Long) obj) : Optional.empty();
			}

			@Override
			public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq)
			{
				final java.lang.Long val = Util.parseLong(seq.next());
				return (sys, env) -> {for(Ref ref : refs) ref.force(env, val);};
			}
			
			static void bind(ConfigurationOperatorImpl.ForKlink operator)
			{
				Long instance = new Long(operator);
				operator.engine.getExpressions().putExpression(new Expression("long", instance));
				operator.map.put(java.lang.Long.class, instance);
			}

			@Override
			public java.lang.String decompile(java.lang.Long val)
			{
				return val.toString();
			}
		}
		
		private static class Short extends ForKlink<java.lang.Short> implements ExpressionCompiler.Level1
		{
			Short(org.kucro3.keleton.impl.config.ConfigurationOperatorImpl.ForKlink operator)
			{
				super(operator, java.lang.Short.class, "short");
			}

			@Override
			public Optional<java.lang.Short> tryCast(Object obj) 
			{
				return obj instanceof java.lang.Short ? Optional.of((java.lang.Short) obj) : Optional.empty();
			}

			@Override
			public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq) 
			{
				final java.lang.Short val = Util.parseShort(seq.next());
				return (sys, env) -> {for(Ref ref : refs) ref.force(env, val);};
			}
			
			static void bind(ConfigurationOperatorImpl.ForKlink operator)
			{
				Short instance = new Short(operator);
				operator.engine.getExpressions().putExpression(new Expression("short", instance));
				operator.map.put(java.lang.Short.class, instance);
			}

			@Override
			public java.lang.String decompile(java.lang.Short val) 
			{
				return val.toString();
			}
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static class Section extends ForKlink<ConfigurationSection> implements ExpressionCompiler.Level2
		{
			Section(org.kucro3.keleton.impl.config.ConfigurationOperatorImpl.ForKlink operator)
			{
				super(operator, ConfigurationSection.class, "section");
			}

			@Override
			public Optional<ConfigurationSection> tryCast(Object obj)
			{
				return obj instanceof ConfigurationSection ? Optional.of((ConfigurationSection) obj) : Optional.empty();
			}

			@Override
			public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock) 
			{
				java.lang.String name;
				
				if(refs.length == 0)
					return (sys, env) -> {};
				else if(refs.length != 1)
					name = seq.next();
				else if(seq.hasNext())
					name = seq.next();
				else if((name = refs[0].getName()) == null)
					throw new ScriptException("Section name undefined");
				
				final java.lang.String sectionName = name;
				
				return (sys, env) -> {
					ConfigurationImpl.ForKlink config 
							= (ConfigurationImpl.ForKlink) env.getRegisters().OR[0];
					
					ConfigurationSectionImpl.ForKlink parent
							= (ConfigurationSectionImpl.ForKlink) env.getRegisters().OR[1];
					
					ConfigurationSectionImpl.ForKlink section
							= new ConfigurationSectionImpl.ForKlink(sectionName, config, parent);
					
					env.getRegisters().OR[1] = section;
					Variables var = env.getVars();
					
					env.setVars(section.vars);
					codeBlock.execute(sys);
					
					env.getRegisters().OR[1] = parent;
					env.setVars(var);
					
					for(Ref ref : refs) 
						ref.force(env, new TypeRedirected(ConfigurationSection.class, section));
				};
			}
			
			static void bind(ConfigurationOperatorImpl.ForKlink operator)
			{
				Section instance = new Section(operator);
				operator.engine.getExpressions().putExpression(new Expression("section", instance));
				operator.map.put(ConfigurationSection.class, instance);
			}

			@Override
			public java.lang.String decompile(ConfigurationSection val)
			{
				int count = 0;
				StringBuilder prefsb = new StringBuilder(), sb = new StringBuilder();
				java.lang.String prefix;
				
				ConfigurationSection current = val;
				while((current = (ConfigurationSection) current.getParent().orElse(null)) != null)
					count++;
				
				for(int i = 0; i < count; i++)
					prefsb.append("\t");
				prefix = prefsb.toString();
				
				sb.append(":").append("\n");
				
				for(ConfigurationEntry entry : ((ConfigurationSection<?>) val).entries())
				{
					java.lang.String name = entry.getName();
					java.lang.String type = entry.getType().getName();
					ConfigurationSectionTypeImpl.ForKlink<Object> handle 
						= (org.kucro3.keleton.impl.config.ConfigurationSectionTypeImpl.ForKlink<Object>) entry.getType();
					java.lang.String value = handle.decompile(entry.get());
					
					sb.append(prefix)
					.append("#")
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
				
				return sb.toString();
			}
		}
	}
}
