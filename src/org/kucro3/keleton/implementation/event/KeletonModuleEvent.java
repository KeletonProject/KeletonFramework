package org.kucro3.keleton.implementation.event;

import org.kucro3.keleton.event.CancellableWithCause;
import org.kucro3.keleton.event.ProceduralEvent;
import org.kucro3.keleton.implementation.KeletonModule;

import java.util.Optional;

public interface KeletonModuleEvent extends ProceduralEvent {
    public KeletonModule getModule();

    public interface StateTransformation extends KeletonModuleEvent, ProceduralEvent
    {
        public KeletonModule.State from();

        public KeletonModule.State to();

        public interface Pre extends StateTransformation, ProceduralEvent.Pre, CancellableWithCause
        {
        }

        public interface Cancelled extends StateTransformation, ProceduralEvent.Failed
        {
        }

        public interface Ignored extends StateTransformation, ProceduralEvent.Failed
        {
            public String getMessage();
        }

        public interface Failed extends StateTransformation, ProceduralEvent.Failed
        {
            public Throwable getException();
        }

        public interface Transformed extends StateTransformation, ProceduralEvent.Completed
        {
        }
    }

    public interface Recovery extends KeletonModuleEvent, ProceduralEvent
    {
        public interface Pre extends Recovery, ProceduralEvent.Pre, CancellableWithCause
        {
        }

        public interface Cancelled extends Recovery, ProceduralEvent.Failed
        {
        }

        public interface Failed extends Recovery, ProceduralEvent.Failed
        {
            public Optional<Throwable> getException();
        }

        public interface Recovered extends Recovery, ProceduralEvent.Completed
        {
            public KeletonModule.State recoveredTo();
        }

        public KeletonModule.State expected();
    }
}
