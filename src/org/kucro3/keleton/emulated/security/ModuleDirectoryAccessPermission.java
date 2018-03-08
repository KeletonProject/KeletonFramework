package org.kucro3.keleton.emulated.security;

public class ModuleDirectoryAccessPermission extends EmulationPermission {
    public ModuleDirectoryAccessPermission()
    {
        super(SUBNAME, ACTION);
    }

    public static final String SUBNAME = "moduledirectory";

    public static final String ACTION = "ACCESS";
}
