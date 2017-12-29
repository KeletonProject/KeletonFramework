package org.kucro3.keleton.implementation;

import org.kucro3.keleton.exception.KeletonException;

public abstract class KeletonInstance {
    protected KeletonInstance()
    {
        this.identifier = new Object();
    }

    public void onLoad() throws KeletonException
    {
    }

    public void onEnable() throws KeletonException
    {
    }

    public void onDisable() throws KeletonException
    {
    }

    public Object getIdentifier()
    {
        return identifier;
    }

    private final Object identifier;
}
