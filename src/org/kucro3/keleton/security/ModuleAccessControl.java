package org.kucro3.keleton.security;

import org.kucro3.keleton.api.APIContainer;
import org.kucro3.keleton.api.ImportAPI;

import java.security.Permission;

@APIContainer
public class ModuleAccessControl {
    @ImportAPI(namespace = "mac", name = "Enabled")
    public static native boolean isEnabled();

    @ImportAPI(namespace = "mac", name = "CheckPermission")
    public static native void checkPermission(Permission permission);
}
