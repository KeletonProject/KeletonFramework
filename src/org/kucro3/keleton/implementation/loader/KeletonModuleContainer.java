package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.exception.KeletonLoaderException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    public boolean removeModule(String name)
    {
        return map.remove(name) != null;
    }

    public void addModule(String name, KeletonLoadedModule module) throws KeletonLoaderException
    {
        if(map.containsKey(name))
            throw new KeletonLoaderException("Duplicated module: " + name);

        map.put(name, module);
    }

    void compute() throws KeletonLoaderException
    {
        tree = new KeletonLoaderTree(map.values());
        tree.compute();

        pipeline = tree.build();
    }

    KeletonLoaderPipeline getPipeline()
    {
        return pipeline;
    }

    KeletonLoaderTree getTree()
    {
        return tree;
    }

    KeletonLoaderPipeline pipeline;

    private KeletonLoaderTree tree;

    private final Map<String, Set<String>> deps = new HashMap<>();

    private final Map<String, KeletonLoadedModule> map = new HashMap<>();
}
