package org.kucro3.keleton.world.warp;

import org.kucro3.annotation.CaseInsensitive;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;

public interface WarpCollection {
    public boolean hasWarp(@CaseInsensitive String name);

    public boolean deleteWarp(@CaseInsensitive String name);

    public Warp setWarp(@CaseInsensitive String name, Location<World> location);

    public Collection<Warp> getAllWarps();

    public default int warpCount()
    {
        return getAllWarps().size();
    }

    public boolean clearWarps();

    public Optional<Warp> getWarp(@CaseInsensitive String name);
}
