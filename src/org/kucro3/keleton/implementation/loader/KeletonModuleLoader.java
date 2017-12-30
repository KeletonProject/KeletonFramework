package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.implementation.exception.KeletonLoaderException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;
import java.util.function.BiConsumer;

public final class KeletonModuleLoader {
    private KeletonModuleLoader()
    {
    }

    public static void load(KeletonModuleContainer container, BiConsumer<KeletonInstance, KeletonLoaderException> consumer)
    {
        for(PluginContainer plugin : Sponge.getPluginManager().getPlugins())
        {
            Optional<?> optional = plugin.getInstance();

            if(!optional.isPresent())
                continue;

            Object object = optional.get();

            if(!(object instanceof KeletonInstance))
                continue;

            KeletonInstance instance = (KeletonInstance) object;

            Class<?> clz = instance.getClass();
            KeletonModule info = clz.getAnnotation(KeletonModule.class);

            if(info == null)
            {
                consumer.accept(instance, new KeletonLoaderException("Metadata not found"));
                continue;
            }

            KeletonLoadedModule loaded = new KeletonLoadedModule(info, instance);

            try {
                container.addModule(info.name(), loaded);
            } catch (KeletonLoaderException e) {
                consumer.accept(instance, e);
            }
        }
    }
}
