package org.kucro3.keleton.world.warp.event;

import org.kucro3.keleton.world.warp.Warp;

public interface WarpSetEvent extends WarpEvent {
    public interface Completed extends WarpSetEvent, WarpEvent.Completed
    {
        public Warp getWarp();
    }

    public interface Failed extends WarpSetEvent, WarpEvent.Failed
    {
    }

    public interface Pre extends WarpSetEvent, WarpEvent.Pre
    {
    }
}
