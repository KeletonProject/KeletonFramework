package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTObject {
	public NBTTagString(String name)
	{
		this(name, null);
	}
	
	public NBTTagString(String name, String value)
	{
		super(TYPECODE, TYPENAME, name);
		this.value = value;
	}
	
	@Override
	protected boolean read(DataInput input) throws IOException
	{
		this.value = NBTObject.readString(input);
		return true;
	}
	
	@Override
	public void write(DataOutput output) throws IOException
	{
		NBTObject.writeString(output, value);
	}
	
	@Override
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	protected String value;
	
	public static final String TYPENAME = "TAG_String";
	
	public static final byte TYPECODE = 8;
}
