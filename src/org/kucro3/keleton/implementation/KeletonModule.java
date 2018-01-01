package org.kucro3.keleton.implementation;

import java.util.Set;

public interface KeletonModule {
    public String getName();

    public Set<String> getDependencies();

    public KeletonInstance getInstance();
}
