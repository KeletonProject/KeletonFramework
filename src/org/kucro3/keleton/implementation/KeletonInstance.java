package org.kucro3.keleton.implementation;

import org.kucro3.keleton.exception.KeletonException;

public interface KeletonInstance {
    public void onLoad() throws KeletonException;

    public void onEnable() throws KeletonException;

    public void onDisable() throws KeletonException;

    public Object getInstance();
}
