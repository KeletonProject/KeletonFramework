package org.kucro3.keleton.implementation;

import java.util.Set;

public interface KeletonModule {
    public String getId();

    public Set<String> getDependencies();

    public KeletonInstance getInstance();

    public boolean supportDisabling();

    public State getState();

    enum State {
        LOADED,
        ENABLED,
        DISABLED
    }
}
