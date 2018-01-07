package org.kucro3.keleton.module.loader;

import org.kucro3.keleton.module.KeletonInstance;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.*;

class KeletonModuleDiscoverer {
    Collection<KeletonModuleImpl> discover()
    {
        List<KeletonModuleImpl> discovered = new ArrayList<>();

        Collection<PluginContainer> containers = Sponge.getPluginManager().getPlugins();
        for(PluginContainer container : containers)
        {
            Optional<?> optional = container.getInstance();

            if (!optional.isPresent())
                continue;

            Object object = optional.get();

            if(!(object instanceof KeletonInstance))
        }

        return Collections.unmodifiableCollection(discovered);
    }
}
