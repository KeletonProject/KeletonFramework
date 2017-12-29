package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;

public class KeletonLoadedModule {
    KeletonLoadedModule(KeletonModule module, KeletonInstance instance)
    {
        this.module = module;
        this.instance = instance;
    }

    public KeletonInstance getInstance()
    {
        return instance;
    }

    public KeletonModule getModule()
    {
        return module;
    }

    private final KeletonInstance instance;

    private final KeletonModule module;
}
