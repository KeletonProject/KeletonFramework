package org.kucro3.keleton.klink.xmount;

import java.util.Optional;

public interface MountedRegistry {
    public boolean isMounted(String name);

    public Optional<LoadedMounter> getMounted(String name);
}
