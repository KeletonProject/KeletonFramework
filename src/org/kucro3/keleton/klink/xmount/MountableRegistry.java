package org.kucro3.keleton.klink.xmount;

import java.util.Optional;

public interface MountableRegistry {
    public boolean hasMoutable(String name);

    public Optional<LoadedMounter> getMountable(String name);

    public boolean putMountable(String name, Mounter mounter);
}
