package org.kucro3.keleton.module.security;

public class ModuleStateTransformationCancellingPermission extends ModulePermission {
    public ModuleStateTransformationCancellingPermission()
    {
        super(SUBNAME, ACTION);
    }

    public static final String SUBNAME = "state.control";

    public static final String ACTION = "CANCEL";
}
