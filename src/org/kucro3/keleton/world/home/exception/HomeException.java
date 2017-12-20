package org.kucro3.keleton.world.home.exception;

import org.kucro3.keleton.exception.KeletonRuntimeException;

public class HomeException extends KeletonRuntimeException {
    public HomeException()
    {
    }

    public HomeException(String msg)
    {
        super(msg);
    }

    public HomeException(Throwable e)
    {
        super(e);
    }

    public HomeException(String msg, Throwable e)
    {
        super(msg, e);
    }
}
