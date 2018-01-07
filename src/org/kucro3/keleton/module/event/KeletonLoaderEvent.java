package org.kucro3.keleton.module.event;

import org.kucro3.keleton.event.CancellableWithCause;
import org.kucro3.keleton.event.ProceduralEvent;
import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.KeletonModule;

import java.util.Set;

public interface KeletonLoaderEvent extends ProceduralEvent {
    public interface Pre extends KeletonLoaderEvent, ProceduralEvent.Pre, CancellableWithCause
    {
        public String getId();

        public Set<String> getDependencies();
    }

    public interface Ignored extends KeletonLoaderEvent, ProceduralEvent.Failed
    {
        public KeletonInstance getInstance();

        public String getMessage();
    }

    public interface Cancelled extends KeletonLoaderEvent, ProceduralEvent.Failed
    {
        public String getId();

        public Set<String> getDependencies();
    }

    public interface Discovered extends KeletonLoaderEvent, ProceduralEvent.Completed
    {
        public KeletonModule getModule();
    }
}
