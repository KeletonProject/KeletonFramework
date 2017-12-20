package org.kucro3.keleton.world.home;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.world.home.exception.HomeException;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface HomeCollection {
    public Optional<Home> getHome(@CaseInsensitive String name, World world) throws HomeException;

    public boolean hasHome(@CaseInsensitive String name) throws HomeException;

    public boolean hasHome(@CaseInsensitive String name, World world) throws HomeException;

    public boolean deleteHome(@CaseInsensitive String name) throws HomeException;

    public boolean deleteHome(@CaseInsensitive String name, World world) throws HomeException;

    public boolean clearHomes() throws HomeException;

    public boolean clearHomes(World world) throws HomeException;

    public Collection<Home> getAllHomes() throws HomeException;

    public Collection<Home> getHomes(World world) throws HomeException;

    public Collection<Home> getHomes(String name) throws HomeException;

    public default int homeCount() throws HomeException
    {
        return getAllHomes().size();
    }

    public Home setHome(UUID owner, @CaseInsensitive String name, Location<World> location) throws HomeException;
}
