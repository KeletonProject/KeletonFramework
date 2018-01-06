package org.kucro3.keleton.module.loader;

import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.event.KeletonModuleEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;

import java.util.Optional;

abstract class StateTransformationEventImpl implements KeletonModuleEvent.StateTransformation {
    StateTransformationEventImpl(KeletonModule module, KeletonModule.State from, KeletonModule.State to, Cause cause)
    {
        this.module = module;
        this.from = from;
        this.to = to;
        this.cause = cause;
    }

    @Override
    public KeletonModule.State from()
    {
        return from;
    }

    @Override
    public KeletonModule.State to()
    {
        return to;
    }

    @Override
    public KeletonModule getModule()
    {
        return module;
    }

    @Override
    public Cause getCause()
    {
        return cause;
    }

    static class Ignored extends StateTransformationEventImpl implements KeletonModuleEvent.StateTransformation.Ignored
    {
        Ignored(KeletonModule module, KeletonModule.State from, KeletonModule.State to, Cause cause, String info)
        {
            super(module, from, to, cause);
            this.info = info;
        }

        @Override
        public String getMessage()
        {
            return info;
        }

        private final String info;
    }

    static class Pre extends StateTransformationEventImpl implements KeletonModuleEvent.StateTransformation.Pre
    {
        Pre(KeletonModule module, KeletonModule.State from, KeletonModule.State to, Cause cause)
        {
            super(module, from, to, cause);
        }

        @Override
        public Optional<Cause> getCancellationCause()
        {
            return Optional.ofNullable(cause);
        }

        @Override
        public void cancel(Cause cause)
        {
            setCancelled(true);
            this.cause = cause;
        }

        @Override
        public boolean isCancelled()
        {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancel)
        {
            this.cancelled = cancel;
            if(!cancel)
                this.cause = null;
        }

        private volatile boolean cancelled;

        private volatile Cause cause;
    }

    static class Cancelled extends StateTransformationEventImpl implements KeletonModuleEvent.StateTransformation.Cancelled
    {
        Cancelled(KeletonModule module, KeletonModule.State from, KeletonModule.State to, Cause cause)
        {
            super(module, from, to, cause);
        }
    }

    static class Transformed extends StateTransformationEventImpl implements KeletonModuleEvent.StateTransformation.Transformed
    {
        Transformed(KeletonModule module, KeletonModule.State from, KeletonModule.State to, Cause cause)
        {
            super(module, from, to, cause);
        }
    }

    static class Failed extends StateTransformationEventImpl implements KeletonModuleEvent.StateTransformation.Failed
    {
        Failed(KeletonModule module, KeletonModule.State from, KeletonModule.State to, Cause cause, Throwable exception)
        {
            super(module, from, to, cause.merge(Cause.of(NamedCause.of("exception", exception))));
            this.exception = exception;
        }

        @Override
        public Throwable getException()
        {
            return exception;
        }

        private final Throwable exception;
    }

    static class BadDependency extends StateTransformationEventImpl implements KeletonModuleEvent.StateTransformation.BadDependency
    {

        BadDependency(KeletonModule module, KeletonModule.State from, KeletonModule.State to, Cause cause, KeletonModule dep)
        {
            super(module, from, to, cause);
            this.dep = dep;
        }

        @Override
        public KeletonModule getDependency()
        {
            return dep;
        }

        private final KeletonModule dep;
    }

    private final Cause cause;

    private final KeletonModule.State from;

    private final KeletonModule.State to;

    private final KeletonModule module;
}
