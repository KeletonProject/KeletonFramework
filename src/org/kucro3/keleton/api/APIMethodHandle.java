package org.kucro3.keleton.api;

public interface APIMethodHandle {
    public Object call(Object... args);

    public String getName();

    public APIMethodNamespace getNamespace();
}
