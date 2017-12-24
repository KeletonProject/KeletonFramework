package org.kucro3.keleton.world.home.exception;

public class HomeInternalException extends HomeException {
    public HomeInternalException()
    {
    }

    public HomeInternalException(String msg)
    {
        super(msg);
    }

    public HomeInternalException(Throwable e)
    {
        super(e);
    }

    public HomeInternalException(String msg, Throwable e)
    {
        super(msg, e);
    }
}
