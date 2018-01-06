package org.kucro3.keleton.module.loader;

import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.Module;
import org.kucro3.keleton.module.event.KeletonModuleEvent;
import org.kucro3.keleton.module.exception.KeletonModuleException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class KeletonModuleImpl implements KeletonModule {
    KeletonModuleImpl(KeletonInstance instance, Module info)
    {
        this.instance = instance;
        this.info = info;
        this.state = State.MOUNTED;
    }

    @Override
    public String getId()
    {
        return this.info.id();
    }

    @Override
    public Set<String> getDependencies()
    {
        return new HashSet<>(Arrays.asList(info.dependencies()));
    }

    @Override
    public KeletonInstance getInstance()
    {
        return instance;
    }

    @Override
    public boolean supportDisabling()
    {
        return info.supportDisabling();
    }

    @Override
    public State getState()
    {
        return state;
    }

    void fence()
    {
        this.state = State.FENCED;
    }

    Void exceptionally(Throwable e, State expected)
    {
        try {
            if(!prepostRecovery(e, expected))
                return null;
            if(!this.instance.tryRecovery(this, expected, e))
                postFailedOnRecovery(e, expected, null);
            else
                postRecovered(e, expected, expected);
        } catch (Throwable recoveryException) {
            this.state = State.FAILED;
            postFailedOnRecovery(e, expected, recoveryException);
        }
        return null;
    }

    void transformed(State to)
    {
        this.state = to;
        postTransformed();
    }

    Optional<CompletableFuture<Void>> transform(State to, ActionFunction function, Supplier<Boolean> checker)
    {
        if(!checker.get() || !checkConvert(to) || !prepostTransformation(to))
            return Optional.empty();

        fence();

        try {
            CompletableFuture<Void> future = function.get();

            future.exceptionally((e) -> exceptionally(e, to));

            return Optional.of(future.thenAccept((unused) -> transformed(to)));
        } catch (Exception e) {
            postExcepted(to, e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<CompletableFuture<Void>> load()
    {
        return transform(State.LOADED, () -> instance.onLoad(), () -> true);
    }

    @Override
    public Optional<CompletableFuture<Void>> enable()
    {
        return transform(State.ENABLED, () -> instance.onEnable(), () -> true);

    }

    @Override
    public Optional<CompletableFuture<Void>> disable()
    {
        return transform(State.DISABLED, () -> instance.onDisable(), () -> checkDisablingFunction());
    }

    @Override
    public Optional<CompletableFuture<Void>> destroy()
    {
        return transform(State.DESTROYED, () -> instance.onDestroy(), () -> true);
    }

    void postExcepted(State expected, Exception e)
    {
        Sponge.getEventManager().post(new StateTransformationEventImpl.Failed(this, this.state, State.ENABLED, createCause(), e));
    }

    void postTransformed()
    {
        Sponge.getEventManager().post(new StateTransformationEventImpl.Transformed(this, this.state, State.ENABLED, createCause()));
    }

    void postFailedOnRecovery(Throwable source, State expected, Throwable exception)
    {
        Sponge.getEventManager().post(new RecoveryEventImpl.Failed(this, expected, source, createCause(), exception));
    }

    void postRecovered(Throwable source, State expected, State achieved)
    {
        Sponge.getEventManager().post(new RecoveryEventImpl.Recovered(this, expected, source, createCause(), achieved));
    }

    boolean prepostRecovery(Throwable source, State expected)
    {
        KeletonModuleEvent.Recovery.Pre event = new RecoveryEventImpl.Pre(this, expected, source, createCause());
        Sponge.getEventManager().post(event);

        if(event.isCancelled())
        {
            Cause cause = createCause();
            if(event.isCancelledWithCause())
                cause = cause.merge(event.getCancellationCause().get());

            Sponge.getEventManager().post(new RecoveryEventImpl.Cancelled(this, expected, source, cause));
            return false;
        }

        return true;
    }

    boolean prepostTransformation(State expected)
    {
        KeletonModuleEvent.StateTransformation.Pre event = new StateTransformationEventImpl.Pre(this, this.state, expected, createCause());
        Sponge.getEventManager().post(event);

        if(event.isCancelled())
        {
            Cause cause = createCause();
            if(event.isCancelledWithCause())
                cause = cause.merge(event.getCancellationCause().get());

            Sponge.getEventManager().post(new StateTransformationEventImpl.Cancelled(this, this.state, expected, cause));
            return false;
        }

        return true;
    }

    boolean checkDisablingFunction()
    {
        if(!supportDisabling())
        {
            String msg = "Disabing operation not supported";
            Sponge.getEventManager().post(new StateTransformationEventImpl.Ignored(this, this.state, State.DISABLED, createCause(), msg));
            return false;
        }
        return true;
    }

    boolean touchState(State state)
    {
        if(this.state.equals(State.FENCED))
            return false;

        int expected = state.code();
        int prediction = this.state.code() << 1;



        return true;
    }

    boolean checkConvert(State state)
    {
        if(!touchState(state))
            stateFailure(state);
        else
            return true;
        return false;
    }

    void stateFailure(State state)
    {
        String msg = "Cannot convert the current state " + this.state.name() + " to " + state.name();
        Sponge.getEventManager().post(new StateTransformationEventImpl.Ignored(this, this.state, state, createCause(), msg));
    }

    Cause createCause()
    {
        return Cause.source(this).build();
    }

    DisablingCallback callback;

    private volatile State state;

    private final KeletonInstance instance;

    private final Module info;

    static interface DisablingCallback
    {
        void onDisable(KeletonModuleImpl module) throws KeletonModuleException;
    }

    static interface ActionFunction
    {
        CompletableFuture<Void> get() throws Exception;
    }
}
