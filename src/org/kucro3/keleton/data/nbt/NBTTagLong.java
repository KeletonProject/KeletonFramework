package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTObject {
	public NBTTagLong(String name)
	{
		this(name, 0);
	}
	
	public NBTTagLong(String name, long value)
	{
		super(TYPECODE, TYPENAME, name);
		this.value = value;
	}
	
	@Override
	protected boolean read(DataInput input) throws IOException
	{
		this.value = input.readLong();
		return true;
	}
	
	@Override
	protected void write(DataOutput output) throws IOException
	{
		output.writeLong(value);
	}
	
	@Override
	public Long getValue()
	{
		return value;
	}
	
	public void setValue(long value)
	{
		this.value = value;
	}
	
	protected long value;
	
	public static final String TYPENAME = "Tag_Long";
	
	public static final byte TYPECODE = 4;
}
