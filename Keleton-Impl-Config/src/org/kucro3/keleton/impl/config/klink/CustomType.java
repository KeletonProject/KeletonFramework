package org.kucro3.keleton.impl.config.klink;

import java.util.Objects;
import java.util.Optional;

import org.kucro3.klink.expression.ExpressionCompiler;

public class CustomType<T> {
	public CustomType(String identifier, Class<T> type, Caster<T> caster, ExpressionCompiler compiler, Decompiler<T> decompiler)
	{
		this.identifier = Objects.requireNonNull(identifier);
		this.type = Objects.requireNonNull(type);
		this.caster = Objects.requireNonNull(caster);
		this.compiler = Objects.requireNonNull(compiler);
		this.decompiler = Objects.requireNonNull(decompiler);
	}
	
	public String getIdentifier()
	{
		return identifier;
	}
	
	public Class<T> getType()
	{
		return type;
	}
	
	public Caster<T> getCaster()
	{
		return caster;
	}
	
	public ExpressionCompiler getCompiler()
	{
		return compiler;
	}
	
	public Decompiler<T> getDecompiler()
	{
		return decompiler;
	}
	
	private final Class<T> type;
	
	private final Caster<T> caster;
	
	private final ExpressionCompiler compiler;
	
	private final Decompiler<T> decompiler;
	
	private final String identifier;
	
	public interface Decompiler<T>
	{
		public String decompile(T val);
	}
	
	public interface Caster<T>
	{
		public Optional<T> cast(Object obj);
	}
}
