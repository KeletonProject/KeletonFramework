package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagDouble extends NBTObject {
	public NBTTagDouble(String name)
	{
		this(name, .0D);
	}
	
	public NBTTagDouble(String name, double value)
	{
		super(TYPECODE, TYPENAME, name);
		this.value = value;
	}
	
	@Override
	protected boolean read(DataInput input) throws IOException
	{
		this.value = input.readDouble();
		return true;
	}
	
	@Override
	public void write(DataOutput output) throws IOException
	{
		output.writeDouble(value);
	}
	
	@Override
	public Double getValue()
	{
		return value;
	}
	
	public void setValue(double value)
	{
		this.value = value;
	}
	
	protected double value;
	
	public static final String TYPENAME = "TAG_Double";
	
	public static final byte TYPECODE = 6;
}
