package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonLoadedModule;
import org.kucro3.keleton.implementation.exception.KeletonLoaderException;
import org.spongepowered.api.Sponge;

import java.util.*;

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

    public void addModule(String name, KeletonLoadedModule module) throws KeletonLoaderException
    {
        if(map.containsKey(name))
            throw new KeletonLoaderException("Duplicated module: " + name);

        map.put(name, module);
        deps.put(name, new HashSet<>(Arrays.asList(module.getInfo().dependencies())));
    }

    public Collection<KeletonLoadedModule> getModules()
    {
        return Collections.unmodifiableCollection(map.values());
    }

    public boolean hasDepended(String name)
    {
        for(Set<String> deps : this.deps.values())
            if(deps.contains(name))
                return true;

        return false;
    }

    public Collection<KeletonLoadedModule> getDepended(String name)
    {
        ArrayList<KeletonLoadedModule> array = new ArrayList<>();

        for(Map.Entry<String, Set<String>> deps : this.deps.entrySet())
            if(deps.getValue().contains(name))
                array.add(map.get(deps.getKey()));

        return array;
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
