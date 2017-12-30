package org.kucro3.keleton.implementation.event;

import org.kucro3.keleton.implementation.KeletonLoadedModule;
import org.spongepowered.api.event.Event;

public interface KeletonModuleEvent extends Event {
    public KeletonLoadedModule getModule();

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