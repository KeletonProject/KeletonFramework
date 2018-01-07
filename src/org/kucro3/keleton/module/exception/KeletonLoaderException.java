package org.kucro3.keleton.module.exception;

import org.kucro3.keleton.exception.KeletonException;

public class KeletonLoaderException extends KeletonException {
    public KeletonLoaderException()
    {
        super();
    }

    public KeletonLoaderException(String msg)
    {
        super(msg);
    }

    public KeletonLoaderException(Throwable cause)
    {
        super(cause);
    }

    public KeletonLoaderException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
