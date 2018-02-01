package org.kucro3.keleton.klink.xmount;

import org.kucro3.keleton.api.APIContainer;
import org.kucro3.keleton.api.ImportAPI;

@APIContainer
public final class XMount {
    private XMount()
    {
    }

    @ImportAPI(namespace = "kernel-klink-xmount", name = "GetXMountManager")
    public static native XMountManager getManager();
}
