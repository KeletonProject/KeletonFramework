package org.kucro3.keleton.exception;

public class KeletonRuntimeException extends RuntimeException {
    public KeletonRuntimeException()
    {
    }

    public KeletonRuntimeException(String msg)
    {
        super(msg);
    }

    public KeletonRuntimeException(Throwable cause)
    {
        super(cause);
    }

    public KeletonRuntimeException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
