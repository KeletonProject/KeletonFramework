package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.exception.KeletonException;
import org.kucro3.keleton.exception.KeletonInternalException;
import org.kucro3.keleton.implementation.KeletonInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class KeletonInstanceImpl implements KeletonInstance {
    KeletonInstanceImpl(Object instance, Method onLoad, Method onEnable, Method onDisable)
    {
        this.instance = instance;
        this.onLoad = onLoad;
        this.onEnable = onEnable;
        this.onDisable = onDisable;
    }

    @Override
    public void onLoad() throws KeletonException
    {
        exec(onLoad);
    }

    @Override
    public void onEnable() throws KeletonException
    {
        exec(onEnable);
    }

    @Override
    public void onDisable() throws KeletonException
    {
        exec(onDisable);
    }

    @Override
    public Object getInstance()
    {
        return instance;
    }

    void exec(Method mthd) throws KeletonException
    {
        if(mthd == null)
            return;

        try {
            mthd.invoke(instance);
        } catch (IllegalAccessException e) {
            throw new KeletonInternalException(e);
        } catch (InvocationTargetException e) {
            throw new KeletonException("Failed on calling", e.getCause());
        }
    }

    private final Object instance;

    private final Method onLoad;

    private final Method onEnable;

    private final Method onDisable;
}
