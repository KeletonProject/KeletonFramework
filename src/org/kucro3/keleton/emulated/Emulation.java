package org.kucro3.keleton.emulated;

import org.kucro3.keleton.api.APIContainer;
import org.kucro3.keleton.api.ImportAPI;

@APIContainer
public final class Emulation {
    private Emulation()
    {
    }

    @ImportAPI(namespace = "kernel-emulated", name = "GetEmulated")
    public static native Emulated getEmulated();
}
