package org.kucro3.keleton.world.home;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.KeyToken;

import java.util.Optional;

public interface HomeService {
    public static final KeyToken<HomeService> TOKEN = KeyToken.of(Key.of("HomeService", HomeService.class, HomeService.class));

    public static final String DEFAULT = "default";

    public Optional<HomeCollection> getCollection(@CaseInsensitive String identifier);

    public default Optional<HomeCollection> getDefaultCollection()
    {
        return getCollection(DEFAULT);
    }

    public boolean available(@CaseInsensitive String identifier);
}
