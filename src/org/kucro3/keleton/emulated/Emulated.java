package org.kucro3.keleton.emulated;

public interface Emulated {
    public EmulatedHandle getHandle(String path);

    public EmulatedHandle getModuleDirectory();

    public EmulatedHandle getBootFile();
}
