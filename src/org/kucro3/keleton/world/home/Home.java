package org.kucro3.keleton.world.home;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.world.Located;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

public interface Home extends Located {
    public @CaseInsensitive String getName();

    public UUID getOwnerUniqueId();

    public default Optional<Player> getOwner()
    {
        return Sponge.getServer().getPlayer(getOwnerUniqueId());
    }

    public boolean vaild();

    public Location<World> getLocation();

    public boolean teleport(Entity entity, Cause cause);
}
