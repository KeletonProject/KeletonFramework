package org.kucro3.keleton.emulated;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Optional;

public interface EmulatedHandle {
    public EmulatedHandle[] listHandles();

    public EmulatedHandle[] listHandles(HandleFilter filter);

    public boolean makeDirectory();

    public boolean isDirectory();

    public boolean exists();

    public String getName();

    public String getPath();

    public URL toURL() throws IOException;

    public default Optional<String> getParent()
    {
        Optional<EmulatedHandle> optional = getParentHandle();
        if(optional.isPresent())
            return Optional.of(optional.get().getPath());
        return Optional.empty();
    }

    public Optional<EmulatedHandle> getParentHandle();

    public default boolean hasParentHandle()
    {
        return getParentHandle().isPresent();
    }

    public boolean canDelete();

    public boolean canRead();

    public boolean canWrite();

    public boolean delete();

    public Optional<InputStream> openInput() throws IOException;

    public Optional<OutputStream> openOutput() throws IOException;

    public void create() throws IOException;

    public static interface HandleFilter
    {
        boolean filter(EmulatedHandle handle);
    }
}
