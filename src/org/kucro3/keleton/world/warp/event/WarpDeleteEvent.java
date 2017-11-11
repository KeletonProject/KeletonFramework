package org.kucro3.keleton.world.warp.event;

import org.kucro3.keleton.world.warp.Warp;

public interface WarpDeleteEvent extends WarpEvent {
    public interface Completed extends WarpDeleteEvent, WarpEvent.Completed
    {
    }

    public interface Failed extends WarpDeleteEvent, WarpEvent.Failed
    {
    }

    public interface Pre extends WarpDeleteEvent, WarpEvent.Pre
    {
        public Warp getWarp();
    }
}
