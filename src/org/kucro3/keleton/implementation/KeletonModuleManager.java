package org.kucro3.keleton.implementation;

import org.kucro3.keleton.implementation.exception.KeletonLoaderException;

import java.util.Collection;

public interface KeletonModuleManager {
    public void addModule(String name, Class<?> clz) throws KeletonLoaderException;

    public void addModule(String name, KeletonInstance instance) throws KeletonLoaderException;

    public void addModule(String name, KeletonLoadedModule module) throws KeletonLoaderException;

    public boolean disableModule(String name) throws KeletonLoaderException;

    public boolean hasModule(String name);

    public boolean hasDepended(String name);

    public Collection<KeletonLoadedModule> getDepended(String name);

    public boolean disableAllForModule(String name) throws KeletonLoaderException;

    public Collection<KeletonLoadedModule> getModules();
}
