package org.kucro3.keleton.module.exception;

public class KeletonModuleDuplicationException extends KeletonModuleException {
    public KeletonModuleDuplicationException()
    {
    }

    public KeletonModuleDuplicationException(String msg)
    {
        super(msg);
    }

    public KeletonModuleDuplicationException(Throwable cause)
    {
        super(cause);
    }

    public KeletonModuleDuplicationException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
