package org.kucro3.keleton.world.home;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.KeyToken;
import org.kucro3.keleton.world.Located;
import org.kucro3.keleton.world.home.exception.HomeException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

public interface Home extends Located {
    public @CaseInsensitive String getName() throws HomeException;

    public UUID getOwnerUniqueId() throws HomeException;

    public default Optional<Player> getOwner() throws HomeException
    {
        return Sponge.getServer().getPlayer(getOwnerUniqueId());
    }

    public boolean valid();

    public Location<World> getLocation() throws HomeException;

    public HomeCollection getCollection();

    public boolean teleport(Entity entity, Cause cause) throws HomeException;
}
