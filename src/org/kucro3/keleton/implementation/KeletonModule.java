package org.kucro3.keleton.implementation;

import org.kucro3.keleton.implementation.exception.KeletonModuleException;

import java.util.Set;

public interface KeletonModule {
    public String getId();

    public Set<String> getDependencies();

    public KeletonInstance getInstance();

    public boolean supportDisabling();

    public State getState();

    public void load() throws KeletonModuleException;

    public void enable() throws KeletonModuleException;

    public void disable() throws KeletonModuleException;

    enum State {
        LOADED,
        ENABLED,
        DISABLED
    }
}
