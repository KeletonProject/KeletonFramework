package org.kucro3.keleton.implementation.exception;

import org.kucro3.keleton.exception.KeletonException;

public class KeletonModuleException extends KeletonException {
    public KeletonModuleException()
    {
    }

    public KeletonModuleException(String msg)
    {
        super(msg);
    }

    public KeletonModuleException(Throwable cause)
    {
        super(cause);
    }

    public KeletonModuleException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
