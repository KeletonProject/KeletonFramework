package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class NBTObject {
	protected NBTObject(byte typecode, String typename, String name)
	{
		this.name = name;
		this.typename = typename;
		this.typecode = typecode;
	}
	
	public String getName()
	{
		return name;
	}
	
	public final String getTypeName()
	{
		return typename;
	}
	
	public final byte getTypeCode()
	{
		return typecode;
	}
	
	protected final void writeHead(DataOutput output) throws IOException
	{
		output.writeByte(typecode);
		writeString(output, name);
	}
	
	protected abstract void write(DataOutput output) throws IOException;
	
	protected abstract boolean read(DataInput input) throws IOException;
	
	protected static void writeString(DataOutput output, String str) throws IOException
	{
		byte[] byts = str.getBytes(CHARSET);
		output.writeShort(byts.length);
		output.write(byts);
	}
	
	protected static String readString(DataInput input) throws IOException
	{
		int size = input.readShort();
		byte[] byts = new byte[size];
		input.readFully(byts);
		return new String(byts, CHARSET);
	}
	
	protected static NBTObject readHead(DataInput input) throws IOException
	{
		byte code = input.readByte();
		if(code == 0)
			return null;
		return NBTService.getConstructor(NBTService.getType(code)).construct(readString(input));
	}
	
	public abstract Object getValue();
	
	private final byte typecode;
	
	private final String typename;
	
	protected final String name;
	
	public static final Charset CHARSET = Charset.forName("UTF-8");
}
