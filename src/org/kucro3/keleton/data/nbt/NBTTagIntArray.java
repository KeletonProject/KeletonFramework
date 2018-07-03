package org.kucro3.keleton.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagIntArray extends NBTObject {
    public NBTTagIntArray(String name)
    {
        super(TYPECODE, TYPENAME, name);
    }

    public NBTTagIntArray(String name, int[] value)
    {
        this(name);
        this.value = value;
    }

    @Override
    protected void write(DataOutput output) throws IOException
    {
        output.writeInt(value.length);
        for(int v : value)
            output.writeInt(v);
    }

    @Override
    protected boolean read(DataInput input) throws IOException
    {
        int size = input.readInt();
        int[] values = new int[size];
        for(int i = 0; i < values.length; i++)
            values[i] = input.readInt();
        this.value = values;
        return true;
    }

    @Override
    public int[] getValue()
    {
        return value;
    }

    public void setValue(int[] value)
    {
        this.value = value;
    }

    protected int[] value;

    public static final String TYPENAME = "TAG_Int_Array";

    public static final byte TYPECODE = 11;
}
