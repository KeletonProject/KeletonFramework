package org.kucro3.keleton.api.exception;

public class APIExportingUnsatisfiedMethodException extends APIExportingException {
    public APIExportingUnsatisfiedMethodException()
    {
    }

    public APIExportingUnsatisfiedMethodException(String msg)
    {
        super(msg);
    }

    public APIExportingUnsatisfiedMethodException(Throwable cause)
    {
        super(cause);
    }

    public APIExportingUnsatisfiedMethodException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
