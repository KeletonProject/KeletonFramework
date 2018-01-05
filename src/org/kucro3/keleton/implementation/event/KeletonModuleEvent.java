package org.kucro3.keleton.implementation.event;

import org.kucro3.keleton.event.CancellableWithCause;
import org.kucro3.keleton.event.ProceduralEvent;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.implementation.exception.KeletonModuleException;
import org.omg.CORBA.PERSIST_STORE;
import org.spongepowered.api.event.Event;

public interface KeletonModuleEvent extends ProceduralEvent {
    public KeletonModule getModule();

    public interface StateTransformation extends KeletonModuleEvent
    {
        public KeletonModule.State from();

        public KeletonModule.State to();

        public interface Pre extends StateTransformation, ProceduralEvent.Pre, CancellableWithCause
        {
        }

        public interface Ignored extends StateTransformation, ProceduralEvent.Failed
        {
        }

        public interface Transformed extends StateTransformation, ProceduralEvent.Completed
        {
        }
    }
}
