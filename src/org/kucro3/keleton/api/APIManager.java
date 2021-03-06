package org.kucro3.keleton.api;

import org.kucro3.keleton.api.exception.APIExportingException;

import java.util.Map;
import java.util.Optional;

// implemented by kernel
@APIContainer
public final class APIManager {
    private APIManager()
    {
    }

    public static Optional<APIMethodNamespace> getNamespace(String name)
    {
        return Optional.of(getNamespaces().get(name));
    }

    public static boolean hasNamespace(String name)
    {
        return getNamespaces().containsKey(name);
    }

    @ImportAPI(namespace = "kernel-api", name = "GetNamespaces")
    public static native Map<String, APIMethodNamespace> getNamespaces();

    @ImportAPI(namespace = "kernel-api", name = "ExportAPI")
    public static native void export(Class<?> apiProvider) throws APIExportingException;
}
