package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.implementation.Module;
import org.kucro3.keleton.implementation.exception.KeletonModuleException;
import org.kucro3.keleton.implementation.exception.KeletonModuleExecutionException;
import org.kucro3.keleton.implementation.exception.KeletonModuleFunctionException;

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
            throw new KeletonModuleExecutionException(e);
        }

        this.state = State.LOADED;
    }

    @Override
    public void enable() throws KeletonModuleException
    {
        checkConvert(State.ENABLED);

        try {
             instance.onEnable();
        } catch (Exception e) {
            throw new KeletonModuleExecutionException(e);
        }

        this.state = State.ENABLED;
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
            throw new KeletonModuleExecutionException(e);
        }

        this.state = State.DISABLED;
    }

    void checkDisablingFunction() throws KeletonModuleException
    {
        if(!supportDisabling())
            throw new KeletonModuleFunctionException("Disabling operation not supported");
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
        throw new KeletonModuleFunctionException("Cannot convert the current state " + this.state.name() + " to " + state.name());
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
