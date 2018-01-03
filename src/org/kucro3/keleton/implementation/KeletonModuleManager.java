package org.kucro3.keleton.implementation;

import java.util.Collection;

public interface KeletonModuleManager {
    public boolean hasModule(String name);

    public boolean hasDepended(String name);

    public Collection<KeletonModule> getDepended(String name);

    public Collection<KeletonModule> getModules();
}
