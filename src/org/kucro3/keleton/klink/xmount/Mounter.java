package org.kucro3.keleton.klink.xmount;

import org.kucro3.klink.Klink;

public interface Mounter {
    public void mount(Klink sys);

    public void unmount(Klink sys);
}
