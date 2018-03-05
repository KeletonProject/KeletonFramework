package org.kucro3.keleton.module.security;

import org.kucro3.keleton.module.KeletonModule;

public class ModuleStateTransformingPermission extends ModulePermission {
    public ModuleStateTransformingPermission(KeletonModule.State state)
    {
        super(SUBNAME, state.name());
    }

    public static final String SUBNAME = "state.transform";
}
