package org.kucro3.keleton.world.home.exception;

public class InvalidHomeException extends HomeException {
    public InvalidHomeException()
    {
    }

    public InvalidHomeException(String msg)
    {
        super(msg);
    }

    public InvalidHomeException(Throwable cause)
    {
        super(cause);
    }

    public InvalidHomeException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
