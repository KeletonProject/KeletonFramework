package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTObject {
	public NBTTagByte(String name)
	{
		this(name, (byte)0);
	}
	
	public NBTTagByte(String name, byte value)
	{
		super(TYPECODE, TYPENAME, name);
		this.value = value;
	}
	
	@Override
	public Byte getValue()
	{
		return value;
	}
	
	public void setValue(byte value)
	{
		this.value = value;
	}
	
	@Override
	protected void write(DataOutput output) throws IOException
	{
		output.writeByte(value);
	}
	
	@Override
	protected boolean read(DataInput input) throws IOException
	{
		value = input.readByte();
		return true;
	}
	
	protected byte value;
	
	public static final String TYPENAME = "TAG_Byte";
	
	public static final byte TYPECODE = 1;
}
