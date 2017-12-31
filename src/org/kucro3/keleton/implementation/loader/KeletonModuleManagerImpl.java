package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonLoadedModule;
import org.kucro3.keleton.implementation.KeletonModuleManager;

import java.util.Collection;

public final class KeletonModuleManagerImpl implements KeletonModuleManager {
    public KeletonModuleManagerImpl()
    {
        this.container = new KeletonModuleContainer();
    }

    @Override
    public boolean hasModule(String name)
    {
        return container.hasModule(name);
    }

    @Override
    public boolean hasDepended(String name)
    {
        return container.hasDepended(name);
    }

    @Override
    public Collection<KeletonLoadedModule> getDepended(String name)
    {
        return container.getDepended(name);
    }

    @Override
    public Collection<KeletonLoadedModule> getModules()
    {
        return container.getModules();
    }

    final KeletonModuleContainer container;
}
