package org.kucro3.keleton.implementation;

import java.util.concurrent.CompletableFuture;

public interface KeletonInstance {
    public default CompletableFuture<Void> onLoad() throws Exception {return CompletableFuture.completedFuture(null);}

    public default CompletableFuture<Void> onEnable() throws Exception {return CompletableFuture.completedFuture(null);}

    public default CompletableFuture<Void> onDisable() throws Exception {return CompletableFuture.completedFuture(null);}

    public default CompletableFuture<Void> onDestroy() throws Exception {return CompletableFuture.completedFuture(null);}

    public default boolean tryRecovery(KeletonModule module, KeletonModule.State expectedState, Throwable e) {return false;}
}
