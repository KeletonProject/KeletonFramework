package org.kucro3.keleton.world.warp.event;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.event.CancellableWithCause;
import org.kucro3.keleton.event.ProceduralEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface WarpEvent extends ProceduralEvent {
    public @CaseInsensitive String getName();

    public Location<World> getLocation();

    public interface Pre extends WarpEvent, CancellableWithCause
    {
    }

    public interface Completed extends WarpEvent
    {
    }

    public interface Failed extends WarpEvent
    {
    }
}
