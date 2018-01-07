package org.kucro3.keleton.module.loader;

import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.Module;
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
                continue;

            KeletonInstance instance = (KeletonInstance) object;
            Module info;

            // check annotation
            {
                Class<?> clz = instance.getClass();

                info = clz.getAnnotation(Module.class);

                if (info == null)
                {
                    continue;
                }
            }
        }

        return Collections.unmodifiableCollection(discovered);
    }
}
