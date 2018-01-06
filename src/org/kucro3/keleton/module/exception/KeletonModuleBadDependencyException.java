package org.kucro3.keleton.module.exception;

import org.kucro3.keleton.module.KeletonModule;

public class KeletonModuleBadDependencyException extends KeletonModuleException {
    public KeletonModuleBadDependencyException(KeletonModule badModule)
    {
        this.module = badModule;
    }

    public KeletonModule getBadModule()
    {
        return module;
    }

    private final KeletonModule module;
}
