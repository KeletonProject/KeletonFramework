package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NBTTagList extends NBTObject {
	public NBTTagList(String name)
	{
		this(name, null);
	}
	
	public <T extends NBTObject> NBTTagList(String name, Class<T> type)
	{
		this(name, type, new ArrayList<>());
	}
	
	public <T extends NBTObject> NBTTagList(String name, Class<T> type, List<NBTObject> value)
	{
		super(TYPECODE, TYPENAME, name);
		this.type = type;
		this.value = value;
	}

	@Override
	protected boolean read(DataInput input) throws IOException 
	{
		NBTObject tag;
		this.type = NBTService.getType(input.readByte());
		NBTObjectConstructor constructor = NBTService.getConstructor(type);
		int length = input.readInt();
		this.value = new ArrayList<NBTObject>(length);
		for(int i = 0; i < length; i++)
		{
			this.value.add(tag = constructor.construct(null));
			tag.read(input);
		}
		return true;
	}
	
	@Override
	protected void write(DataOutput output) throws IOException
	{
		output.writeByte(NBTService.getTypeCode(type));
		output.writeInt(value.size());
		for(NBTObject tag : value)
			tag.write(output);
	}

	@Override
	public List<? extends NBTObject> getValue()
	{
		return value;
	}
	
	public Class<? extends NBTObject> getType()
	{
		return type;
	}
	
	public boolean addTag(NBTObject obj)
	{
		if(type == null)
			return false;
		if(obj.getTypeCode() != NBTService.getTypeCode(type))
			return false;
		value.add(obj);
		return true;
	}
	
	protected List<NBTObject> value;
	
	protected Class<? extends NBTObject> type;
	
	public static final String TYPENAME = "TAG_List";
	
	public static final byte TYPECODE = 9;
}
