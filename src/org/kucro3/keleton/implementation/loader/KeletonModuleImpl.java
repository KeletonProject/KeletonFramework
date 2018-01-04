package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.implementation.Module;

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

    boolean shiftState(State state)
    {
        if((this.state.ordinal() + 1 % 3) != state.ordinal())
            return false;
        this.state = state;
        return true;
    }

    private State state;

    private final KeletonInstance instance;

    private final Module info;
}
