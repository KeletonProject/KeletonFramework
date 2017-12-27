package org.kucro3.keleton.world.home;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.world.home.exception.HomeException;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface HomeCollection {
    public CompletableFuture<Optional<Home>> getHome(UUID owner, @CaseInsensitive String name, World world) throws HomeException;

    public CompletableFuture<Optional<Home>> getHome(UUID owner, @CaseInsensitive String name) throws HomeException;

    public CompletableFuture<Boolean> hasHome(UUID owner, @CaseInsensitive String name) throws HomeException;

    public CompletableFuture<Boolean> hasHome(UUID owner, @CaseInsensitive String name, World world) throws HomeException;

    public CompletableFuture<Boolean> deleteHome(UUID owner, @CaseInsensitive String name, Cause cause) throws HomeException;

    public CompletableFuture<Boolean> deleteHome(UUID owner, @CaseInsensitive String name, World world, Cause cause) throws HomeException;

    public CompletableFuture<Boolean> clearHomes(Cause cause) throws HomeException;

    public CompletableFuture<Boolean> clearHomes(World world, Cause cause) throws HomeException;

    public CompletableFuture<Boolean> clearHomes(UUID owner, Cause cause) throws HomeException;

    public CompletableFuture<Map<String, Home>> getHomes(UUID owner) throws HomeException;

    public CompletableFuture<Integer> homeCount(UUID owner) throws HomeException;

    public CompletableFuture<Home> setHome(UUID owner, @CaseInsensitive String name, Location<World> location, Cause cause) throws HomeException;

    public default CompletableFuture<Void> preload(UUID owner)
    {
        return CompletableFuture.completedFuture(null);
    }
}
