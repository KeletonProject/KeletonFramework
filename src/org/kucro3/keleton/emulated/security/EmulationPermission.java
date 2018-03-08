package org.kucro3.keleton.emulated.security;

import org.kucro3.keleton.security.KeletonPermission;

public class EmulationPermission extends KeletonPermission {
    public EmulationPermission(String subname, String action)
    {
        super(NAME, subname, action);
    }

    public static final String NAME = "keleton.emulation";
}
