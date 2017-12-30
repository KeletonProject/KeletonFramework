package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.exception.KeletonException;
import org.kucro3.keleton.implementation.KeletonLoadedModule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;

import java.util.List;
import java.util.function.BiConsumer;

public class KeletonLoaderPipeline {
    KeletonLoaderPipeline(List<KeletonLoadedModule> list)
    {
        this.order = list;
    }

    public void onLoad(BiConsumer<KeletonLoadedModule, KeletonException> consumer, Cause cause)
    {
        for(int i = 0; i < order.size(); i++)
        {
            KeletonLoadedModule module = order.get(i);
            try {
                module.getInstance().onLoad();
                Sponge.getEventManager().post(new KeletonModuleEventImpl.Load(cause, module));
            } catch (KeletonException e) {
                consumer.accept(order.get(i), e);
            }
        }
    }

    public void onDisable(BiConsumer<KeletonLoadedModule, KeletonException> consumer, Cause cause)
    {
        for(int i = order.size() - 1; i > -1; i--)
        {
            KeletonLoadedModule module = order.get(i);
            try {
                module.getInstance().onDisable();
                Sponge.getEventManager().post(new KeletonModuleEventImpl.Disable(cause, module));
            } catch (KeletonException e) {
                consumer.accept(order.get(i), e);
            }
        }
    }

    public void onEnable(BiConsumer<KeletonLoadedModule, KeletonException> consumer, Cause cause)
    {
        for(int i = 0; i < order.size(); i++)
        {
            KeletonLoadedModule module = order.get(i);
            try {
                module.getInstance().onEnable();
                Sponge.getEventManager().post(new KeletonModuleEventImpl.Enable(cause, module));
            } catch (KeletonException e) {
                consumer.accept(order.get(i), e);
            }
        }
    }

    private final List<KeletonLoadedModule> order;
}
