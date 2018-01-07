package org.kucro3.keleton.module.loader;

import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.event.KeletonLoaderEvent;
import org.spongepowered.api.event.cause.Cause;

import java.util.Optional;
import java.util.Set;

abstract class LoaderEventImpl implements KeletonLoaderEvent {
    LoaderEventImpl(Cause cause, String id, Set<String> dependencies)
    {
        this.cause = cause;
        this.id = id;
        this.dependencies = dependencies;
    }

    @Override
    public Cause getCause()
    {
        return cause;
    }

    public String getId()
    {
        return id;
    }

    public Set<String> getDependencies()
    {
        return dependencies;
    }

    private final String id;

    private final Set<String> dependencies;

    private final Cause cause;

    static class Pre extends LoaderEventImpl implements KeletonLoaderEvent.Pre
    {
        Pre(Cause cause, String id, Set<String> dependencies)
        {
            super(cause, id, dependencies);
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

        private boolean cancelled;

        private Cause cause;
    }

    static class Cancelled extends LoaderEventImpl implements KeletonLoaderEvent.Cancelled
    {
        Cancelled(Cause cause, String id, Set<String> dependencies)
        {
            super(cause, id, dependencies);
        }
    }

    static class Discovered extends LoaderEventImpl implements KeletonLoaderEvent.Discovered
    {
        Discovered(Cause cause, KeletonModule module)
        {
            super(cause, null, null);
            this.module = module;
        }

        @Override
        public KeletonModule getModule()
        {
            return module;
        }

        private final KeletonModule module;
    }

    static class Ignored extends LoaderEventImpl implements KeletonLoaderEvent.Ignored
    {
        Ignored(Cause cause, String message, KeletonInstance instance)
        {
            super(cause, null, null);
            this.message = message;
            this.instance = instance;
        }

        @Override
        public KeletonInstance getInstance()
        {
            return instance;
        }

        @Override
        public String getMessage()
        {
            return message;
        }

        private final KeletonInstance instance;

        private final String message;
    }
}
