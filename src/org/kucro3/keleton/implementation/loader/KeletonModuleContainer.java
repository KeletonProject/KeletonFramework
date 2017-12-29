package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.exception.KeletonLoaderException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class KeletonModuleContainer {
    public KeletonModuleContainer()
    {
    }

    public Optional<KeletonLoadedModule> getModule(String name)
    {
        return Optional.ofNullable(map.get(name));
    }

    public boolean hasModule(String name)
    {
        return map.containsKey(name);
    }

    boolean removeModule(String name)
    {
        return map.remove(name) != null;
    }

    void addModule(String name, KeletonLoadedModule module) throws KeletonLoaderException
    {
        if(map.containsKey(name))
            throw new KeletonLoaderException("Duplicated module: " + name);

        map.put(name, module);
    }

    KeletonLoaderTree getTree()
    {
        return tree;
    }

    private final KeletonLoaderTree tree = new KeletonLoaderTree();

    private final Map<String, KeletonLoadedModule> map = new HashMap<>();
}
