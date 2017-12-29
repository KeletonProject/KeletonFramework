package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.implementation.event.KeletonModuleEvent;
import org.spongepowered.api.event.cause.Cause;

public abstract class KeletonModuleEventImpl implements KeletonModuleEvent {
    KeletonModuleEventImpl(Cause cause, KeletonInstance instance, KeletonModule module)
    {
        this.cause = cause;
        this.instance = instance;
        this.module = module;
    }

    @Override
    public KeletonInstance getInstance()
    {
        return this.instance;
    }

    @Override
    public KeletonModule getModule()
    {
        return this.module;
    }

    @Override
    public Cause getCause()
    {
        return this.cause;
    }

    private final KeletonInstance instance;

    private final KeletonModule module;

    private final Cause cause;

    public static class Load extends KeletonModuleEventImpl implements KeletonModuleEvent.Load
    {
        Load(Cause cause, KeletonInstance instance, KeletonModule module)
        {
            super(cause, instance, module);
        }
    }

    public static class Enable extends KeletonModuleEventImpl implements KeletonModuleEvent.Enable
    {
        Enable(Cause cause, KeletonInstance instance, KeletonModule module)
        {
            super(cause, instance, module);
        }
    }

    public static class Disable extends KeletonModuleEventImpl implements KeletonModuleEvent.Disable
    {
        Disable(Cause cause, KeletonInstance instance, KeletonModule module)
        {
            super(cause, instance, module);
        }
    }
}
