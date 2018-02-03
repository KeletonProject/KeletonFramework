package org.kucro3.keleton.exception;

public class KeletonKernelCrash extends RuntimeException {
    public KeletonKernelCrash()
    {
    }

    public KeletonKernelCrash(String msg)
    {
        super(msg);
    }

    public KeletonKernelCrash(Throwable cause)
    {
        super(cause);
    }

    public KeletonKernelCrash(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
