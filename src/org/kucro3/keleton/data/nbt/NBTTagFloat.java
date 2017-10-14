package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagFloat extends NBTObject {
	public NBTTagFloat(String name)
	{
		this(name, .0F);
	}
	
	public NBTTagFloat(String name, float value)
	{
		super(TYPECODE, TYPENAME, name);
		this.value = value;
	}
	
	@Override
	protected boolean read(DataInput input) throws IOException
	{
		this.value = input.readFloat();
		return true;
	}
	
	@Override
	protected void write(DataOutput output) throws IOException
	{
		output.writeFloat(value);
	}
	
	@Override
	public Float getValue()
	{
		return value;
	}
	
	public void setValue(float value)
	{
		this.value = value;
	}
	
	protected float value;
	
	public static final String TYPENAME = "TAG_Float";
	
	public static final byte TYPECODE = 5;
}
