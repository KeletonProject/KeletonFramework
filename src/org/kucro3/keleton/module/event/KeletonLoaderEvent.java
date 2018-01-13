package org.kucro3.keleton.module.event;

import org.kucro3.keleton.event.CancellableWithCause;
import org.kucro3.keleton.event.ProceduralEvent;
import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.Module;

import java.util.Optional;

public interface KeletonLoaderEvent extends ProceduralEvent {
    public Module getInfo();

    public interface Pre extends KeletonLoaderEvent, ProceduralEvent.Pre, CancellableWithCause
    {
    }

    public interface Ignored extends KeletonLoaderEvent, ProceduralEvent.Failed
    {
        public String getMessage();
    }

    public interface Failed extends KeletonLoaderEvent, ProceduralEvent.Failed
    {
        public Optional<Exception> getException();
    }

    public interface Cancelled extends KeletonLoaderEvent, ProceduralEvent.Cancelled
    {
    }

    public interface Discovered extends KeletonLoaderEvent, ProceduralEvent.Completed
    {
        public KeletonModule getModule();
    }
}
