package org.kucro3.keleton.exception;

public class KeletonBootstrapException extends KeletonException {
    public KeletonBootstrapException()
    {
    }

    public KeletonBootstrapException(String msg)
    {
        super(msg);
    }

    public KeletonBootstrapException(Throwable cause)
    {
        super(cause);
    }

    public KeletonBootstrapException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
