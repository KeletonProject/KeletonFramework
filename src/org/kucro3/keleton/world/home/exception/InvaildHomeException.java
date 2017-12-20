package org.kucro3.keleton.world.home.exception;

public class InvaildHomeException extends HomeException {
    public InvaildHomeException()
    {
    }

    public InvaildHomeException(String msg)
    {
        super(msg);
    }

    public InvaildHomeException(Throwable cause)
    {
        super(cause);
    }

    public InvaildHomeException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
