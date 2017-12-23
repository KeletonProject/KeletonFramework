package org.kucro3.keleton.world.home;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.world.home.exception.HomeException;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface HomeCollection {
    public Optional<Home> getHome(UUID owner, @CaseInsensitive String name, World world) throws HomeException;

    public boolean hasHome(UUID owner, @CaseInsensitive String name) throws HomeException;

    public boolean hasHome(UUID owner, @CaseInsensitive String name, World world) throws HomeException;

    public boolean deleteHome(UUID owner, @CaseInsensitive String name) throws HomeException;

    public boolean clearHomes() throws HomeException;

    public boolean clearHomes(World world) throws HomeException;

    public boolean clearHomes(UUID owner) throws HomeException;

    public Collection<Home> getAllHomes() throws HomeException;

    public Collection<Home> getHomes(World world) throws HomeException;

    public Collection<Home> getHomes(UUID owner) throws HomeException;

    public default int homeCount() throws HomeException
    {
        return getAllHomes().size();
    }

    public default int homeCount(UUID owner) throws HomeException
    {
        return getHomes(owner).size();
    }

    public Home setHome(UUID owner, @CaseInsensitive String name, Location<World> location) throws HomeException;
}
