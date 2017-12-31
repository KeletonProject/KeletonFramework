package org.kucro3.keleton.exception;

public class KeletonInternalException extends KeletonRuntimeException {
    public KeletonInternalException()
    {
    }

    public KeletonInternalException(String msg)
    {
        super(msg);
    }

    public KeletonInternalException(Throwable cause)
    {
        super(cause);
    }

    public KeletonInternalException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
