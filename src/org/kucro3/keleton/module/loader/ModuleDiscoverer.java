package org.kucro3.keleton.module.loader;

import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.Module;
import org.kucro3.keleton.module.event.KeletonLoaderEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.*;

final class ModuleDiscoverer {
    private ModuleDiscoverer()
    {
    }

    static Collection<KeletonModuleImpl> discover()
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
                    Sponge.getEventManager().post(new LoaderEventImpl.Ignored(createCause(instance), "Metadata not found", instance));
                    continue;
                }
            }

            KeletonLoaderEvent.Pre event = new LoaderEventImpl.Pre(createCause(instance), info.id(), new HashSet<>(Arrays.asList(info.dependencies())));
            Sponge.getEventManager().post(event);

            if(event.isCancelled())
            {
                Cause cause = createCause(instance);
                if(event.isCancelledWithCause())
                    cause = cause.merge(event.getCancellationCause().get());

                Sponge.getEventManager().post(new LoaderEventImpl.Cancelled(cause, info.id(), new HashSet<>(Arrays.asList(info.dependencies()))));

                continue;
            }

            KeletonModuleImpl module = new KeletonModuleImpl(instance, info);
            discovered.add(module);

            Sponge.getEventManager().post(new LoaderEventImpl.Discovered(createCause(module), module));
        }

        return Collections.unmodifiableCollection(discovered);
    }

    static Cause createCause(KeletonInstance instance)
    {
        return Cause.source(instance).build();
    }

    static Cause createCause(KeletonModule module)
    {
        return Cause.source(module).build();
    }
}
