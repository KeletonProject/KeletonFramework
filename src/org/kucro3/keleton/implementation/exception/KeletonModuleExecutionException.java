package org.kucro3.keleton.implementation.exception;

public class KeletonModuleExecutionException extends KeletonModuleException {
    public KeletonModuleExecutionException()
    {
    }

    public KeletonModuleExecutionException(String msg)
    {
        super(msg);
    }

    public KeletonModuleExecutionException(Throwable cause)
    {
        super(cause);
    }

    public KeletonModuleExecutionException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
