package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.implementation.Module;
import org.kucro3.keleton.implementation.exception.KeletonModuleException;
import org.kucro3.keleton.implementation.exception.KeletonModuleExecutionException;
import org.kucro3.keleton.implementation.exception.KeletonModuleFunctionException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KeletonModuleImpl implements KeletonModule {
    KeletonModuleImpl(KeletonInstance instance, Module info)
    {
        this.instance = instance;
        this.info = info;
        this.state = State.DISABLED;
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

    @Override
    public void load() throws KeletonModuleException
    {
        checkConvert(State.LOADED);

        try {
            instance.onLoad();
        } catch (Exception e) {
            Sponge.getEventManager().post(new StateTransformationEventImpl.Failed(this, this.state, State.ENABLED, createCause(), e));
            throw new KeletonModuleExecutionException(e);
        }

        this.state = State.LOADED;
        postTransformed();
    }

    @Override
    public void enable() throws KeletonModuleException
    {
        checkConvert(State.ENABLED);

        try {
             instance.onEnable();
        } catch (Exception e) {
            Sponge.getEventManager().post(new StateTransformationEventImpl.Failed(this, this.state, State.ENABLED, createCause(), e));
            throw new KeletonModuleExecutionException(e);
        }

        this.state = State.ENABLED;
        postTransformed();
    }

    @Override
    public void disable() throws KeletonModuleException
    {
        checkDisablingFunction();
        checkConvert(State.DISABLED);

        callback.onDisable(this);

        try {
            instance.onDisable();
        } catch (Exception e) {
            Sponge.getEventManager().post(new StateTransformationEventImpl.Failed(this, this.state, State.ENABLED, createCause(), e));
            throw new KeletonModuleExecutionException(e);
        }

        this.state = State.DISABLED;
        postTransformed();
    }

    void postTransformed()
    {
        Sponge.getEventManager().post(new StateTransformationEventImpl.Transformed(this, this.state, State.ENABLED, createCause()));
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
        if((this.state.ordinal() + 1 % 3) != state.ordinal())
            return false;
        return true;
    }

    void checkConvert(State state) throws KeletonModuleException
    {
        if(!touchState(state))
            stateFailure(state);
    }

    void stateFailure(State state) throws KeletonModuleException
    {
        String msg = "Cannot convert the current state " + this.state.name() + " to " + state.name();
        Sponge.getEventManager().post(new StateTransformationEventImpl.Ignored(this, this.state, state, createCause(), msg));
        throw new KeletonModuleFunctionException(msg);
    }

    Cause createCause()
    {
        return Cause.source(this).build();
    }

    DisablingCallback callback;

    private State state;

    private final KeletonInstance instance;

    private final Module info;

    static interface DisablingCallback
    {
        void onDisable(KeletonModuleImpl module) throws KeletonModuleException;
    }
}
