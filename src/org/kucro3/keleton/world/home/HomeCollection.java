package org.kucro3.keleton.world.home;

import org.kucro3.annotation.CaseInsensitive;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface HomeCollection {
    public Optional<Home> getHome(@CaseInsensitive String name);

    public boolean hasHome(@CaseInsensitive String name);

    public boolean deleteHome(@CaseInsensitive String name);

    public boolean clearHomes();

    public Collection<Home> getAllHomes();

    public default int homeCount()
    {
        return getAllHomes().size();
    }

    public Home setHome(UUID owner, @CaseInsensitive String name, Location<World> location);
}
