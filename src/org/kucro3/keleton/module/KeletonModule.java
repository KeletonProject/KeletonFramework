package org.kucro3.keleton.module;

import org.kucro3.keleton.emulated.EmulatedHandle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.Set;

public interface KeletonModule {
    public String getId();

    public Set<String> getDependencies();

    public KeletonInstance getInstance();

    public boolean supportDisabling();

    public State getState();

    public EmulatedHandle getSource();

    public URL getResourceURL();

    public default Optional<InputStream> getResource(String path) throws IOException
    {
        URL url = getResourceURL();
        url = new URL(url, path);

        URLConnection connection = url.openConnection();

        if(connection == null)
            return Optional.empty();

        connection.connect();
        return Optional.of(connection.getInputStream());
    }

    public default Optional<InputStream> getResourceSilently(String path)
    {
        try {
            return getResource(path);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public boolean load();

    public boolean enable();

    public boolean disable();

    public boolean destroy();

    public void escapeState(State state);

    enum State {
        MOUNTED(0x00),
        LOADED(0x01),
        ENABLED(0x02),
        DISABLED(0x04),
        DESTROYED(0x08),
        FAILED(0x10);

        private State(int code)
        {
            this.code = code;
        }

        public int code()
        {
            return code;
        }

        private final int code;
    }
}
