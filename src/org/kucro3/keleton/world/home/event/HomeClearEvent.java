package org.kucro3.keleton.world.home.event;

import org.kucro3.keleton.event.ProceduralEvent;
import org.spongepowered.api.world.World;

import java.util.UUID;

public interface HomeClearEvent extends ProceduralEvent {
    public interface ByWorld
    {


        World getWorld();
    }

    public interface ByOwner
    {


        UUID getOwnerUniqueId();
    }
}
