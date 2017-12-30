package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.exception.KeletonException;

import java.util.List;
import java.util.function.BiConsumer;

public class KeletonLoaderPipeline {
    KeletonLoaderPipeline(List<KeletonLoadedModule> list)
    {
        this.order = list;
    }

    public void onLoad(BiConsumer<KeletonLoadedModule, KeletonException> consumer)
    {
        for(int i = 0; i < order.size(); i++)
            try {
                order.get(i).getInstance().onLoad();
            } catch (KeletonException e) {
                consumer.accept(order.get(i), e);
            }
    }

    public void onDisable(BiConsumer<KeletonLoadedModule, KeletonException> consumer)
    {
        for(int i = order.size() - 1; i > -1; i--)
            try {
                order.get(i).getInstance().onDisable();
            } catch (KeletonException e) {
                consumer.accept(order.get(i), e);
            }
    }

    public void onEnable(BiConsumer<KeletonLoadedModule, KeletonException> consumer)
    {
        for(int i = 0; i < order.size(); i++)
            try {
                order.get(i).getInstance().onDisable();
            } catch (KeletonException e) {
                consumer.accept(order.get(i), e);
            }
    }

    private final List<KeletonLoadedModule> order;
}
