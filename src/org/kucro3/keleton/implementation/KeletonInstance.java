package org.kucro3.keleton.implementation;

import org.kucro3.keleton.exception.KeletonException;

public interface KeletonInstance {
    public default void onLoad() throws KeletonException {}

    public default void onEnable() throws KeletonException {};

    public default void onDisable() throws KeletonException {}
}
