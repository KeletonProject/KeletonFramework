package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByteArray extends NBTObject {
	public NBTTagByteArray(String name)
	{
		this(name, null);
	}
	
	public NBTTagByteArray(String name, byte[] value)
	{
		super(TYPECODE, TYPENAME, name);
		this.value = value;
	}
	
	@Override
	protected boolean read(DataInput input) throws IOException 
	{
		byte[] value = new byte[input.readInt()];
		input.readFully(value);
		this.value = value;
		return true;
	}
	
	@Override
	protected void write(DataOutput output) throws IOException
	{
		output.writeInt(value.length);
		output.write(value);
	}

	@Override
	public byte[] getValue() 
	{
		return value;
	}
	
	public void setValue(byte[] value)
	{
		this.value = value;
	}
	
	public static final String TYPENAME = "TAG_Byte_Array";
	
	public static final byte TYPECODE = 7;
	
	protected byte[] value;
}
