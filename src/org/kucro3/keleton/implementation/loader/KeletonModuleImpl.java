package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.implementation.Module;
import org.kucro3.keleton.implementation.event.KeletonModuleEvent;
import org.kucro3.keleton.implementation.exception.KeletonModuleException;
import org.kucro3.keleton.implementation.exception.KeletonModuleExecutionException;
import org.kucro3.keleton.implementation.exception.KeletonModuleFunctionException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
            this.instance.tryRecovery(this, expected, e);
        } catch (Throwable recoveryException) {
            this.state = State.FAILED;
            postFailedOnRecovery(expected, recoveryException);
        }
        return null;
    }

    void transformed(State to)
    {
        this.state = to;
        postTransformed();
    }

    @Override
    public void load()
    {
        checkConvert(State.LOADED);

        fence();

        try {
            CompletableFuture<Void> future = instance.onLoad();

            future.exceptionally((e) -> exceptionally(e, State.LOADED));

            future.thenAccept((unused) -> transformed(State.LOADED));
        } catch (Exception e) {
            postExcepted(State.LOADED, e);
        }
    }

    @Override
    public void enable() throws KeletonModuleException
    {
        checkConvert(State.ENABLED);

        fence();

        try {
             instance.onEnable();
        } catch (Exception e) {

            throw new KeletonModuleExecutionException(e);
        }

        this.state = State.ENABLED;
        postTransformed();
    }

    @Override
    public void disable() throws KeletonModuleException {
        checkDisablingFunction();
        checkConvert(State.DISABLED);

        synchronized (callback) {
            callback.onDisable(this);
            fence();
        }

        try {
            instance.onDisable().thenAccept();
        } catch (Exception e) {
            throw new KeletonModuleExecutionException(e);
        }

        this.state = State.DISABLED;
        postTransformed();
    }

    void postExcepted(State expected, Exception e)
    {
        Sponge.getEventManager().post(new StateTransformationEventImpl.Failed(this, this.state, State.ENABLED, createCause(), e));
    }

    void postTransformed()
    {
        Sponge.getEventManager().post(new StateTransformationEventImpl.Transformed(this, this.state, State.ENABLED, createCause()));
    }

    void postFailedOnRecovery(State expected, Throwable exception)
    {
        Sponge.getEventManager().post(new FailedOnRecoveryEventImpl(this, expected, exception, createCause()));
    }

    boolean prepost(State expected)
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

    void checkDisablingFunction() throws KeletonModuleException
    {
        if(!supportDisabling())
        {
            String msg = "Disabing operation not supported";
            Sponge.getEventManager().post(new StateTransformationEventImpl.Ignored(this, this.state, State.DISABLED, createCause(), msg));
            throw new KeletonModuleFunctionException(msg);
        }
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
}
