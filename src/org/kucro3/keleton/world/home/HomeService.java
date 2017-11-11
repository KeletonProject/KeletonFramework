package org.kucro3.keleton.world.home;

import org.kucro3.annotation.CaseInsensitive;

public interface HomeService {
    public static final String DEFAULT = "default";

    public HomeCollection getCollection(@CaseInsensitive String identifier);

    public default HomeCollection getDefaultCollection()
    {
        return getCollection(DEFAULT);
    }

    public boolean available(@CaseInsensitive String identifier);
}
