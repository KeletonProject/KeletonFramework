package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.implementation.event.KeletonModuleEvent;
import org.spongepowered.api.event.cause.Cause;

import java.util.Optional;

abstract class FailedOnRecoveryEventImpl implements KeletonModuleEvent.Recovery {
    FailedOnRecoveryEventImpl(KeletonModule module, KeletonModule.State expected, Cause cause)
    {
        this.module = module;
        this.expected = expected;
        this.cause = cause;
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

    @Override
    public KeletonModule.State expected()
    {
        return expected;
    }

    private final KeletonModule module;

    private final KeletonModule.State expected;

    private final Cause cause;

    static class Pre extends FailedOnRecoveryEventImpl implements Recovery.Pre
    {
        Pre(KeletonModule module, KeletonModule.State expected, Cause cause)
        {
            super(module, expected, cause);
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
                this.cancelled = true;
        }

        private boolean cancelled;

        private Cause cause;
    }

    static class Cancelled extends FailedOnRecoveryEventImpl implements Recovery.Cancelled
    {
        Cancelled(KeletonModule module, KeletonModule.State expected, Cause cause)
        {
            super(module, expected, cause);
        }
    }

    static class Failed extends FailedOnRecoveryEventImpl implements Recovery.Failed
    {
        Failed(KeletonModule module, KeletonModule.State expected, Cause cause, Throwable exception)
        {
            super(module, expected, cause);
            this.exception = exception;
        }

        @Override
        public Optional<Throwable> getException()
        {
            return Optional.ofNullable(exception);
        }

        private final Throwable exception;
    }

    static class Recovered extends FailedOnRecoveryEventImpl implements Recovery.Recovered
    {
        Recovered(KeletonModule module, KeletonModule.State expected, Cause cause, KeletonModule.State achieved)
        {
            super(module, expected, cause);
            this.achieved = achieved;
        }

        @Override
        public KeletonModule.State recoveredTo()
        {
            return achieved;
        }

        private final KeletonModule.State achieved;
    }
}
