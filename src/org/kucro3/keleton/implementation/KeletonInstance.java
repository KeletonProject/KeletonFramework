package org.kucro3.keleton.implementation;

public interface KeletonInstance {
    public default void onLoad() throws Exception {}

    public default void onEnable() throws Exception {}

    public default void onDisable() throws Exception {}

    public default void onDestroy() throws Exception {}
}
