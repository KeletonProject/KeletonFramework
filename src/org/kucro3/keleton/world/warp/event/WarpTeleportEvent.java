package org.kucro3.keleton.world.warp.event;

import org.kucro3.keleton.world.TeleportationResult;
import org.kucro3.keleton.world.warp.Warp;
import org.spongepowered.api.entity.Entity;

public interface WarpTeleportEvent extends WarpEvent {
    public Warp getWarp();

    public Entity getEntity();

    public interface Completed extends WarpTeleportEvent, WarpEvent.Completed
    {
        public TeleportationResult getResult();
    }

    public interface Failed extends WarpTeleportEvent, WarpEvent.Failed
    {
    }

    public interface Pre extends WarpTeleportEvent, WarpEvent.Pre
    {
    }
}
