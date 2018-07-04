package org.kucro3.keleton.data.nbt.common.leveldat;

import org.kucro3.keleton.data.nbt.NBTTagCompound;

public class LevelData {
    public LevelData(NBTTagCompound tag)
    {
        this.tag = tag;
    }

    public NBTTagCompound getTag()
    {
        return tag;
    }

    protected final NBTTagCompound tag;
}
