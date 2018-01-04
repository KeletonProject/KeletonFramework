package org.kucro3.keleton.implementation;

import java.util.Collection;
import java.util.Map;

public interface KeletonModuleManager {
    public boolean hasModule(String name);

    public boolean hasDemanders(String name);

    public Collection<KeletonModule> getDemanders(String name);

    public Map<String, KeletonModule> getModules();
}
