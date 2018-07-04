package org.kucro3.keleton.data.nbt.common.leveldat;

import org.kucro3.keleton.data.nbt.NBTService;
import org.kucro3.keleton.data.nbt.NBTTagCompound;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class LevelDataService {
    private LevelDataService()
    {
    }

    public static LevelData translateFrom(File file) throws IOException
    {
        return new LevelData((NBTTagCompound) NBTService.translateFrom(new GZIPInputStream(new FileInputStream(file))));
    }

    public static LevelData translateFrom(byte[] byts) throws IOException
    {
        return new LevelData((NBTTagCompound) NBTService.translateFrom(new GZIPInputStream(new ByteArrayInputStream(byts))));
    }

    public static LevelData translateFrom(InputStream inputStream) throws IOException
    {
        return new LevelData((NBTTagCompound) NBTService.translateFrom(new GZIPInputStream(inputStream)));
    }
}
