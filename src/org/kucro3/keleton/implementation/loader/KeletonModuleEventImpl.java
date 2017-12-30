package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonLoadedModule;
import org.kucro3.keleton.implementation.event.KeletonModuleEvent;
import org.spongepowered.api.event.cause.Cause;

public abstract class KeletonModuleEventImpl implements KeletonModuleEvent {
    KeletonModuleEventImpl(Cause cause, KeletonLoadedModule module)
    {
        this.cause = cause;
        this.module = module;
    }

    @Override
    public KeletonLoadedModule getModule()
    {
        return this.module;
    }

    @Override
    public Cause getCause()
    {
        return this.cause;
    }

    private final KeletonLoadedModule module;

    private final Cause cause;

    public static class Load extends KeletonModuleEventImpl implements KeletonModuleEvent.Load
    {
        Load(Cause cause, KeletonLoadedModule module)
        {
            super(cause, module);
        }
    }

    public static class Enable extends KeletonModuleEventImpl implements KeletonModuleEvent.Enable
    {
        Enable(Cause cause, KeletonLoadedModule module)
        {
            super(cause, module);
        }
    }

    public static class Disable extends KeletonModuleEventImpl implements KeletonModuleEvent.Disable
    {
        Disable(Cause cause, KeletonLoadedModule module)
        {
            super(cause, module);
        }
    }
}
