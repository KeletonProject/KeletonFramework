package org.kucro3.keleton.module.loader;

import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.KeletonModuleManager;

import java.util.*;

public class KeletonModuleManagerImpl implements KeletonModuleManager
{
    @Override
    public boolean hasModule(String name)
    {
        return sequence.hasModule(name);
    }

    @Override
    public boolean hasDemanders(String name)
    {
        return sequence.hasDemanders(name);
    }

    @Override
    public Collection<KeletonModule> getDemanders(String name)
    {
        Set<KeletonModule> module = new HashSet<>();
        for(String dmd : sequence.getDepended(name))
            module.add(sequence.getModule(dmd));
        return Collections.unmodifiableSet(module);
    }

    @Override
    public Map<String, KeletonModule> getModules()
    {
        return Collections.unmodifiableMap((Map) sequence.getModules());
    }

    @Override
    public KeletonModule getModule(String name)
    {
        return sequence.getModule(name);
    }

    ModuleSequence sequence;
}
