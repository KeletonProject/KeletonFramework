package org.kucro3.keleton.module.loader;

import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.event.KeletonModuleEvent;
import org.spongepowered.api.event.cause.Cause;

import java.util.Optional;

abstract class RecoveryEventImpl implements KeletonModuleEvent.Recovery {
    RecoveryEventImpl(KeletonModule module, KeletonModule.State expected, Throwable exception, Cause cause)
    {
        this.module = module;
        this.expected = expected;
        this.cause = cause;
        this.exception = exception;
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

    @Override
    public Throwable getException()
    {
        return exception;
    }

    private final Throwable exception;

    private final KeletonModule module;

    private final KeletonModule.State expected;

    private final Cause cause;

    static class Pre extends RecoveryEventImpl implements Recovery.Pre
    {
        Pre(KeletonModule module, KeletonModule.State expected, Throwable exception, Cause cause)
        {
            super(module, expected, exception, cause);
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

    static class Cancelled extends RecoveryEventImpl implements Recovery.Cancelled
    {
        Cancelled(KeletonModule module, KeletonModule.State expected, Throwable exception, Cause cause)
        {
            super(module, expected, exception, cause);
        }
    }

    static class Failed extends RecoveryEventImpl implements Recovery.Failed
    {
        Failed(KeletonModule module, KeletonModule.State expected, Throwable exception, Cause cause, Throwable recoveryException)
        {
            super(module, expected, exception, cause);
            this.exception = recoveryException;
        }

        @Override
        public Optional<Throwable> getRecoveryException()
        {
            return Optional.ofNullable(exception);
        }

        private final Throwable exception;
    }

    static class Recovered extends RecoveryEventImpl implements Recovery.Recovered
    {

        Recovered(KeletonModule module, KeletonModule.State expected, Throwable exception, Cause cause, KeletonModule.State achieved)
        {
            super(module, expected, exception, cause);
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
