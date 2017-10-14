package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NBTTagCompound extends NBTObject {
	public NBTTagCompound(String name)
	{
		this(name, new HashMap<>());
	}
	
	public NBTTagCompound(String name, Map<String, NBTObject> value)
	{
		super(TYPECODE, TYPENAME, name);
		this.value = value;
	}
	
	@Override
	protected boolean read(DataInput input) throws IOException
	{
		NBTObject object;
		while((object = NBTObject.readHead(input)) != null)
		{
			put(object);
			object.read(input);
		}
		return true;
	}
	
	@Override
	protected void write(DataOutput output) throws IOException
	{
		for(NBTObject obj : value.values())
		{
			obj.writeHead(output);
			obj.write(output);
		}
		output.writeByte(0);
	}
	
	@Override
	public Map<String, NBTObject> getValue()
	{
		return value;
	}
	
	public void put(NBTObject obj)
	{
		value.put(obj.getName(), obj);
	}
	
	public NBTObject remove(String name)
	{
		return value.remove(name);
	}
	
	public NBTObject get(String name)
	{
		return value.get(name);
	}
	
	protected final Map<String, NBTObject> value;
	
	public static final String TYPENAME = "TAG_Compound";
	
	public static final byte TYPECODE = 10;
}
