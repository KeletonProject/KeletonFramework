package org.kucro3.keleton.implementation.event;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.spongepowered.api.event.Event;

public interface KeletonModuleEvent extends Event {
    public KeletonInstance getInstance();

    public KeletonModule getModule();

    public interface Enable extends KeletonModuleEvent
    {
    }

    public interface Disable extends KeletonModuleEvent
    {
    }

    public interface Load extends KeletonModuleEvent
    {
    }
}
