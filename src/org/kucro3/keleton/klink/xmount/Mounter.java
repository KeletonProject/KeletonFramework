package org.kucro3.keleton.klink.xmount;

import org.kucro3.klink.expression.ExpressionLibrary;

public interface Mounter {
    public void mount(ExpressionLibrary lib);

    public void unmount(ExpressionLibrary lib);
}
