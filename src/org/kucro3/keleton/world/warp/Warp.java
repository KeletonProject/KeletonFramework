package org.kucro3.keleton.world.warp;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.world.Located;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface Warp extends Located {
    public @CaseInsensitive String getName();

    public Location<World> getLocation();

    public boolean teleport(Entity entity, Cause cause);
}
