package org.kucro3.keleton.exception;

public class KeletonException extends Exception {
    public KeletonException()
    {
    }

    public KeletonException(String msg)
    {
        super(msg);
    }

    public KeletonException(Throwable cause)
    {
        super(cause);
    }

    public KeletonException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
