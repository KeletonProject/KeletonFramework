package org.kucro3.keleton.world.home;

public interface HomeService {
    public static final String DEFAULT = "default";

    public HomeCollection getCollection(String identifier);

    public default HomeCollection getDefaultCollection()
    {
        return getCollection(DEFAULT);
    }

    public boolean available(String identifier);
}
