package org.kucro3.keleton.api.exception;

public class APIExportingMetadataNotFoundException extends APIExportingException {
    public APIExportingMetadataNotFoundException()
    {
    }

    public APIExportingMetadataNotFoundException(String msg)
    {
        super(msg);
    }

    public APIExportingMetadataNotFoundException(Throwable cause)
    {
        super(cause);
    }

    public APIExportingMetadataNotFoundException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
