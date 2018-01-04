package org.kucro3.keleton.implementation.loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModuleSequence {
    ModuleSequence(Collection<KeletonModuleImpl> modules)
    {
        this.modules = new HashMap<>();
        this.dependencies = new HashMap<>();

        for(KeletonModuleImpl impl : modules)
        {
            this.modules.put(impl.getId(), impl);
            this.dependencies.put(impl.getId(), impl.getDependencies());
        }
    }

    private final Map<String, Set<String>> dependencies;

    private final Map<String, KeletonModuleImpl> modules;
}
