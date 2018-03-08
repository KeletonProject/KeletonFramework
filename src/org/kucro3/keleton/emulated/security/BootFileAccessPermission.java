package org.kucro3.keleton.emulated.security;

public class BootFileAccessPermission extends EmulationPermission {
    public BootFileAccessPermission()
    {
        super(SUBNAME, ACTION);
    }

    public static final String SUBNAME = "bootfile";

    public static final String ACTION = "ACCESS";
}
