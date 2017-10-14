package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTObject {
	public NBTTagInt(String name)
	{
		this(name, 0);
	}
	
	public NBTTagInt(String name, int value)
	{
		super(TYPECODE, TYPENAME, name);
		this.value = value;
	}
	
	@Override
	protected boolean read(DataInput input) throws IOException
	{
		this.value = input.readInt();
		return true;
	}
	
	@Override
	protected void write(DataOutput output) throws IOException
	{
		output.writeInt(value);
	}
	
	@Override
	public Integer getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	protected int value;
	
	public static final String TYPENAME = "Tag_Int";
	
	public static final byte TYPECODE = 3;
}
