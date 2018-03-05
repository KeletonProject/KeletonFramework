package org.kucro3.keleton.module.security;

import org.kucro3.keleton.security.KeletonPermission;

public class ModulePermission extends KeletonPermission {
    public ModulePermission(String subname, String action)
    {
        super(NAME, subname, action);
    }

    public static final String NAME = "keleton.modules";
}
