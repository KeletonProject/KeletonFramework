package org.kucro3.keleton.world.warp;

import org.kucro3.annotation.CaseInsensitive;

public interface WarpService {
    public static final String DEFAULT = "default";

    public boolean available(@CaseInsensitive String identifier);

    public WarpCollection getCollection(@CaseInsensitive String identifier);

    public default WarpCollection getDefaultCollection()
    {
        return getCollection(DEFAULT);
    }
}
