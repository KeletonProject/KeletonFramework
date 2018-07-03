package org.kucro3.keleton.data.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NBTService {
	public static String getTypeName(Class<? extends NBTObject> clz)
	{
		return (String) MAPPED_NAMES.get(clz);
	}
	
	public static byte getTypeCode(Class<? extends NBTObject> clz)
	{
		return (Byte) MAPPED_CODES.get(clz);
	}
	
	public static Class<? extends NBTObject> getType(byte code)
	{
		return (Class<? extends NBTObject>) MAPPED_CODES.get(code);
	}
	
	public static Class<? extends NBTObject> getType(String name)
	{
		return (Class<? extends NBTObject>) MAPPED_NAMES.get(name);
	}
	
	public static NBTObjectConstructor getConstructor(Class<? extends NBTObject> clz)
	{
		return MAPPED_CONSTRUCTORS.get(clz);
	}
	
	public static NBTObject translateFrom(byte[] byts) throws IOException
	{
		return translateFrom(new ByteArrayInputStream(byts));
	}
	
	public static NBTObject translateFrom(File file) throws IOException
	{
		return translateFrom(new FileInputStream(file));
	}
	
	public static NBTObject translateFrom(InputStream input) throws IOException
	{
		try (DataInputStream dis = new DataInputStream(input)) {
			NBTObject obj = NBTObject.readHead(dis);
			if(obj == null)
				return null;
			obj.read(dis);
			return obj;
		}
	}
	
	public static byte[] translateToBytes(NBTObject obj) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writeTo(obj, baos);
		return baos.toByteArray();
	}
	
	public static void writeTo(NBTObject obj, File file) throws IOException
	{
		writeTo(obj, new FileOutputStream(file));
	}
	
	public static void writeTo(NBTObject obj, OutputStream output) throws IOException
	{
		try (DataOutputStream dos = new DataOutputStream(output)) {
			obj.writeHead(dos);
			obj.write(dos);
		}
	}
	
	private static final Map<Object, Object> MAPPED_NAMES =
			new HashMap<Object, Object>()
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3462153307924369174L;
		
		{
			put(NBTTagByte.class, NBTTagByte.TYPENAME);
			put(NBTTagByteArray.class, NBTTagByte.TYPENAME);
			put(NBTTagCompound.class, NBTTagCompound.TYPENAME);
			put(NBTTagDouble.class, NBTTagDouble.TYPENAME);
			put(NBTTagFloat.class, NBTTagFloat.TYPENAME);
			put(NBTTagInt.class, NBTTagInt.TYPENAME);
			put(NBTTagList.class, NBTTagList.TYPENAME);
			put(NBTTagLong.class, NBTTagLong.TYPENAME);
			put(NBTTagShort.class, NBTTagShort.TYPENAME);
			put(NBTTagString.class, NBTTagString.TYPENAME);
			put(NBTTagIntArray.class, NBTTagIntArray.TYPENAME);
			put(NBTTagLongArray.class, NBTTagLongArray.TYPENAME);
			
			put(NBTTagByte.TYPENAME, NBTTagByte.class);
			put(NBTTagByteArray.TYPENAME, NBTTagByteArray.class);
			put(NBTTagCompound.TYPENAME, NBTTagCompound.class);
			put(NBTTagDouble.TYPENAME, NBTTagDouble.class);
			put(NBTTagFloat.TYPENAME, NBTTagFloat.class);
			put(NBTTagInt.TYPENAME, NBTTagInt.class);
			put(NBTTagList.TYPENAME, NBTTagList.class);
			put(NBTTagLong.TYPENAME, NBTTagLong.class);
			put(NBTTagShort.TYPENAME, NBTTagShort.class);
			put(NBTTagString.TYPENAME, NBTTagString.class);
			put(NBTTagIntArray.TYPENAME, NBTTagIntArray.class);
			put(NBTTagLongArray.TYPENAME, NBTTagLongArray.class);
		}
	};
	
	private static final Map<Object, Object> MAPPED_CODES =
			new HashMap<Object, Object>()
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -5101486966436247172L;

		{
			put(NBTTagByte.class, NBTTagByte.TYPECODE);
			put(NBTTagByteArray.class, NBTTagByteArray.TYPECODE);
			put(NBTTagCompound.class, NBTTagCompound.TYPECODE);
			put(NBTTagDouble.class, NBTTagDouble.TYPECODE);
			put(NBTTagFloat.class, NBTTagFloat.TYPECODE);
			put(NBTTagInt.class, NBTTagInt.TYPECODE);
			put(NBTTagList.class, NBTTagList.TYPECODE);
			put(NBTTagLong.class, NBTTagLong.TYPECODE);
			put(NBTTagShort.class, NBTTagShort.TYPECODE);
			put(NBTTagString.class, NBTTagString.TYPECODE);
			put(NBTTagIntArray.class, NBTTagIntArray.TYPECODE);
			put(NBTTagLongArray.class, NBTTagLongArray.TYPECODE);
			
			put(NBTTagByte.TYPECODE, NBTTagByte.class);
			put(NBTTagByteArray.TYPECODE, NBTTagByteArray.class);
			put(NBTTagCompound.TYPECODE, NBTTagCompound.class);
			put(NBTTagDouble.TYPECODE, NBTTagDouble.class);
			put(NBTTagFloat.TYPECODE, NBTTagFloat.class);
			put(NBTTagInt.TYPECODE, NBTTagInt.class);
			put(NBTTagList.TYPECODE, NBTTagList.class);
			put(NBTTagLong.TYPECODE, NBTTagLong.class);
			put(NBTTagShort.TYPECODE, NBTTagShort.class);
			put(NBTTagString.TYPECODE, NBTTagString.class);
			put(NBTTagIntArray.TYPECODE, NBTTagIntArray.class);
			put(NBTTagLongArray.TYPECODE, NBTTagLongArray.class);
		}
	};
	
	private static final Map<Class<? extends NBTObject>, NBTObjectConstructor> MAPPED_CONSTRUCTORS =
			new HashMap<Class<? extends NBTObject>, NBTObjectConstructor>()
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6187318256426068226L;

		{
			put(NBTTagByte.class, NBTTagByte::new);
			put(NBTTagByteArray.class, NBTTagByteArray::new);
			put(NBTTagCompound.class, NBTTagCompound::new);
			put(NBTTagDouble.class, NBTTagDouble::new);
			put(NBTTagFloat.class, NBTTagFloat::new);
			put(NBTTagInt.class, NBTTagInt::new);
			put(NBTTagList.class, NBTTagList::new);
			put(NBTTagLong.class, NBTTagLong::new);
			put(NBTTagShort.class, NBTTagShort::new);
			put(NBTTagString.class, NBTTagString::new);
			put(NBTTagIntArray.class, NBTTagIntArray::new);
			put(NBTTagLongArray.class, NBTTagLongArray::new);
		}
	};
}
