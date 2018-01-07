package org.kucro3.keleton.api.exception;

public class APIExportingNamespaceDuplicatedException extends APIExportingException {
    public APIExportingNamespaceDuplicatedException(String namespace)
    {
        this.namespace = namespace;
    }

    public String getNamespace()
    {
        return namespace;
    }

    private final String namespace;
}
