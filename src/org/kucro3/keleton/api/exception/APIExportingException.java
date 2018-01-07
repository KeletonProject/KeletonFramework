package org.kucro3.keleton.api.exception;

public class APIExportingException extends Exception {
    public APIExportingException()
    {
    }

    public APIExportingException(String msg)
    {
        super(msg);
    }

    public APIExportingException(Throwable cause)
    {
        super(cause);
    }

    public APIExportingException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
