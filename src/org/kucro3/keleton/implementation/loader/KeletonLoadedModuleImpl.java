package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonLoadedModule;
import org.kucro3.keleton.implementation.KeletonModule;

public class KeletonLoadedModuleImpl implements KeletonLoadedModule {
    KeletonLoadedModuleImpl(KeletonModule module, KeletonInstance instance)
    {
        this.module = module;
        this.instance = instance;
    }

    @Override
    public KeletonInstance getInstance()
    {
        return instance;
    }

    @Override
    public KeletonModule getInfo()
    {
        return module;
    }

    private final KeletonInstance instance;

    private final KeletonModule module;
}
