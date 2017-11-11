package org.kucro3.keleton.world.home;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;

public interface HomeCollection {
    public Optional<Home> getHome(String name);

    public boolean hasHome(String name);

    public boolean deleteHome(String name);

    public boolean clearHomes();

    public Collection<Home> getAllHomes();

    public default int homeCount()
    {
        return getAllHomes().size();
    }

    public Optional<Home> setHome(String name, Location<World> location);
}
