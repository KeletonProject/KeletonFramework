package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTObject {
	public NBTTagShort(String name)
	{
		this(name, (short)0);
	}
	
	public NBTTagShort(String name, short value)
	{
		super(TYPECODE, TYPENAME, name);
		this.value = value;
	}
	
	@Override
	public Short getValue()
	{
		return value;
	}
	
	public void setValue(short value)
	{
		this.value = value;
	}
	
	@Override
	protected void write(DataOutput output) throws IOException
	{
		output.writeShort(value);
	}
	
	@Override
	protected boolean read(DataInput input) throws IOException
	{
		value = input.readShort();
		return true;
	}
	
	protected short value;
	
	public static final String TYPENAME = "TAG_Short";
	
	public static final byte TYPECODE = 2;
}
