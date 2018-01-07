package org.kucro3.keleton.api;

import java.util.Map;
import java.util.Optional;

public interface APIMethodNamespace {
    public default Optional<APIMethodHandle> getExported(String name)
    {
        return Optional.ofNullable(getExported().get(name));
    }

    public default boolean isExported(String name)
    {
        return getExported().containsKey(name);
    }

    public Map<String, APIMethodHandle> getExported();
}
