package org.kucro3.keleton.module.event;

import org.kucro3.keleton.event.CancellableWithCause;
import org.kucro3.keleton.event.ProceduralEvent;
import org.kucro3.keleton.module.KeletonModule;

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

        public interface BadDependency extends StateTransformation, ProceduralEvent.Failed
        {
            public KeletonModule getDependency();
        }
    }

    public interface Recovery extends KeletonModuleEvent, ProceduralEvent
    {
        public Throwable getException();

        public interface Pre extends Recovery, ProceduralEvent.Pre, CancellableWithCause
        {
        }

        public interface Cancelled extends Recovery, ProceduralEvent.Failed
        {
        }

        public interface Failed extends Recovery, ProceduralEvent.Failed
        {
            public Throwable getRecoveryException();
        }

        public interface Recovered extends Recovery, ProceduralEvent.Completed
        {
            public KeletonModule.State recoveredTo();
        }

        public KeletonModule.State expected();
    }
}
