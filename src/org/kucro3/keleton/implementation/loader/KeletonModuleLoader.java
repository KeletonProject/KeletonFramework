package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.*;
import org.kucro3.keleton.implementation.exception.KeletonLoaderException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.asm.lib.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiConsumer;

public final class KeletonModuleLoader {
    private KeletonModuleLoader()
    {
    }

    public static void load(KeletonModuleManagerImpl manager, BiConsumer<Object, KeletonLoaderException> consumer)
    {
        load(manager.container, consumer);
    }

    public static void load(KeletonModuleContainer container, BiConsumer<Object, KeletonLoaderException> consumer)
    {
        LOOP: for(PluginContainer plugin : Sponge.getPluginManager().getPlugins())
        {
            Optional<?> optional = plugin.getInstance();

            if(!optional.isPresent())
                continue;

            Object instance = optional.get();

            Class<?> clz = instance.getClass();

            // KeletonModule info = clz.getAnnotation(KeletonModule.class); // ClassDefNotFound caused by wrapped launcher
            KeletonModule info = getKeletonModule(clz);

            if(info == null)
                continue;

            Method onLoad = null;
            Method onEnable = null;
            Method onDisable = null;

            Method[] methods = clz.getMethods();
            for(Method method : methods)
            {
                boolean flag0, flag1, flag2;

                if(flag0 = getInvokeOnLoad(method) != null)
                    if(onLoad != null)
                    {
                        consumer.accept(instance, new KeletonLoaderException("Duplicated onLoad function"));
                        continue LOOP;
                    }
                    else
                        onLoad = method;
                if(flag1 = getInvokeOnEnable(method) != null)
                {
                    if(flag0)
                    {
                        consumer.accept(instance, new KeletonLoaderException("Multi-meta on the same method: " + Type.getMethodDescriptor(method)));
                        continue LOOP;
                    }
                    else
                        onEnable = method;
                }
                if(flag2 = getInvokeOnEnable(method) != null)
                {
                    if(flag0 || flag1)
                    {
                        consumer.accept(instance, new KeletonLoaderException("Multi-meta on the same method: " + Type.getMethodDescriptor(method)));
                        continue LOOP;
                    }
                    else
                        onDisable = method;
                }

                if(flag0 || flag1 || flag2)
                    if(method.getParameterCount() != 0)
                    {
                        consumer.accept(instance, new KeletonLoaderException("Unsatisfied arguments of method: " + Type.getMethodDescriptor(method)));
                        continue LOOP;
                    }
                    else;
            }

            KeletonLoadedModule loaded = new KeletonLoadedModuleImpl(info, new KeletonInstanceImpl(instance, onLoad, onEnable, onDisable));

            try {
                container.addModule(info.name(), loaded);
            } catch (KeletonLoaderException e) {
                consumer.accept(instance, e);
            }
        }
    }

    static Annotation getAnnotation(AnnotatedElement at, String classname)
    {
        for(Annotation anno : at.getAnnotations())
            if(anno.annotationType().getCanonicalName().equals(classname))
                return anno;
        return null;
    }

    static InvokeOnLoad getInvokeOnLoad(AnnotatedElement at)
    {
        return (InvokeOnLoad) getAnnotation(at, "org.kucro3.keleton.implementation.InvokeOnLoad");
    }

    static InvokeOnEnable getInvokeOnEnable(AnnotatedElement at)
    {
        return (InvokeOnEnable) getAnnotation(at, "org.kucro3.keleton.implementation.InvokeOnEnable");
    }

    static InvokeOnDisable getInvokeOnDisable(AnnotatedElement at)
    {
        return (InvokeOnDisable) getAnnotation(at, "org.kucro3.keleton.implementation.InvokeOnDisable");
    }

    static KeletonModule getKeletonModule(AnnotatedElement at)
    {
        return (KeletonModule) getAnnotation(at, "org.kucro3.keleton.implementation.KeletonModule");
    }
}
