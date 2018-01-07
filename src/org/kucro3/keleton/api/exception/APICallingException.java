package org.kucro3.keleton.api.exception;

public class APICallingException extends RuntimeException {
    public APICallingException()
    {
    }

    public APICallingException(String msg)
    {
        super(msg);
    }

    public APICallingException(Throwable cause)
    {
        super(cause);
    }

    public APICallingException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
