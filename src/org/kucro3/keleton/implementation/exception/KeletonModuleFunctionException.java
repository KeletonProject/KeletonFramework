package org.kucro3.keleton.implementation.exception;

public class KeletonModuleFunctionException extends KeletonModuleException {
    public KeletonModuleFunctionException()
    {
    }

    public KeletonModuleFunctionException(String msg)
    {
        super(msg);
    }

    public KeletonModuleFunctionException(Throwable cause)
    {
        super(cause);
    }

    public KeletonModuleFunctionException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
