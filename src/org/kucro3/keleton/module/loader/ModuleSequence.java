package org.kucro3.keleton.module.loader;

import org.kucro3.keleton.exception.KeletonException;
import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.exception.KeletonLoaderException;
import org.kucro3.keleton.module.exception.KeletonModuleException;
import org.kucro3.keleton.module.exception.KeletonModuleFunctionException;

import java.util.*;

public class ModuleSequence {
    ModuleSequence(Collection<KeletonModuleImpl> modules) throws KeletonException
    {
        this.modules = new HashMap<>();
        this.dependencies = new HashMap<>();
        this.demanders = new HashMap<>();

        for(KeletonModuleImpl impl : modules)
        {
            impl.bind(this);
            impl.callback = this::checkDependedAndRemove;

            this.modules.put(impl.getId(), impl);

            Set<String> deps = impl.getDependencies();
            this.dependencies.put(impl.getId(), deps);
            for(String dep : deps)
            {
                Set<String> dmds = this.demanders.get(dep);

                if(dmds == null)
                    this.demanders.put(dep, dmds = new HashSet<>());

                dmds.add(impl.getId());
            }
        }

        this.sequence = computeSequence();
    }

    private ModuleSequence(Map<String, Set<String>> demanders,
                           Map<String, Set<String>> dependencies,
                           Map<String, KeletonModuleImpl> modules)
    {
        this.demanders = new HashMap<>(demanders);
        this.dependencies = new HashMap<>(dependencies);
        this.modules = new HashMap<>(modules);
        this.sequence = null;
    }

    void checkDependedAndRemove(KeletonModuleImpl impl) throws KeletonModuleException
    {
        if(hasDependencies(impl.getId()))
            throw new KeletonModuleFunctionException("Module \"" + impl.getId() + "\" is in use and cannot be removed safely");
    }

    boolean hasDemanders(String id)
    {
        Set<String> set = demanders.get(id);

        if(set == null || set.isEmpty())
            return false;

        return true;
    }

    boolean hasDependencies(String id)
    {
        Set<String> set = dependencies.get(id);

        if(set == null || set.isEmpty())
            return false;

        return true;
    }

    Set<String> getDepended(String id)
    {
        Set<String> set = demanders.get(id);

        if(set == null)
            return Collections.emptySet();

        return Collections.unmodifiableSet(set);
    }

    Set<String> getDependencies(String id)
    {
        Set<String> set = dependencies.get(id);

        if(set == null)
            return Collections.emptySet();

        return Collections.unmodifiableSet(set);
    }

    List<KeletonModuleImpl> computeSequence() throws KeletonLoaderException
    {
        ModuleSequence subseq = new ModuleSequence(demanders, dependencies, modules);

        List<KeletonModuleImpl> result = new ArrayList<>();

        int last = subseq.modules.size();
        while(!subseq.modules.isEmpty())
        {
            for (Map.Entry<String, KeletonModuleImpl> entry : subseq.modules.entrySet()) {
                if (!hasDependencies(entry.getKey())) {
                    result.add(entry.getValue());
                    if (hasDemanders(entry.getKey()))
                        for (String dmd : demanders.get(entry.getKey()))
                            dependencies.get(dmd).remove(entry.getKey());
                }
            }

            if(subseq.modules.size() == last)
                throw new KeletonLoaderException("Dependency loop detected");

            last = subseq.modules.size();
        }

        return result;
    }

    void loadAll()
    {
        for(KeletonModuleImpl impl : sequence)
            impl.load();
    }

    void enableAll()
    {
        for(KeletonModuleImpl impl : sequence)
            impl.enable();
    }

    void disableAll()
    {
        for(int i = sequence.size() - 1; i >= 0; i--)
            sequence.get(i).disable();
    }

    void destroyAll()
    {
        for(int i = sequence.size() - 1; i >= 0; i--)
            sequence.get(i).destroy();
    }

    KeletonModuleImpl getModule(String id)
    {
        return modules.get(id);
    }

    boolean hasModule(String id)
    {
        return modules.containsKey(id);
    }

    Map<String, KeletonModuleImpl> getModules()
    {
        return modules;
    }

    final List<KeletonModuleImpl> sequence;

    final Map<String, Set<String>> demanders;

    final Map<String, Set<String>> dependencies;

    final Map<String, KeletonModuleImpl> modules;
}
