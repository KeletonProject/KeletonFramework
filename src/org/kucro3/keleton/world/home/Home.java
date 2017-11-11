package org.kucro3.keleton.world.home;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public interface Home {
    public String getName();

    public String getOwnerName();

    public default Optional<Player> getOwner()
    {
        return Sponge.getServer().getPlayer(getOwnerName());
    }

    public Location<World> getLocation();
}
