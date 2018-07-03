package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLongArray extends NBTObject {
    public NBTTagLongArray(String name)
    {
        super(TYPECODE, TYPENAME, name);
    }

    public NBTTagLongArray(String name, long[] value)
    {
        this(name);
        this.value = value;
    }

    @Override
    protected void write(DataOutput output) throws IOException
    {
        output.writeInt(value.length);
        for(long v : value)
            output.writeLong(v);
    }

    @Override
    protected boolean read(DataInput input) throws IOException
    {
        int size = input.readInt();
        long[] values = new long[size];
        for(int i = 0; i < values.length; i++)
            values[i] = input.readLong();
        this.value = values;
        return true;
    }

    @Override
    public long[] getValue()
    {
        return value;
    }

    public void setValue(long[] value)
    {
        this.value = value;
    }

    protected long[] value;

    public static final String TYPENAME = "TAG_Long_Array";

    public static final byte TYPECODE = 12;
}
