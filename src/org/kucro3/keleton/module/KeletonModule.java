package org.kucro3.keleton.module;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface KeletonModule {
    public String getId();

    public Set<String> getDependencies();

    public KeletonInstance getInstance();

    public boolean supportDisabling();

    public State getState();

    public Optional<CompletableFuture<Void>> loadImmediately();

    public Optional<CompletableFuture<Void>> load();

    public Optional<CompletableFuture<Void>> enableImmediately();

    public Optional<CompletableFuture<Void>> enable();

    public Optional<CompletableFuture<Void>> disableImmediately();

    public Optional<CompletableFuture<Void>> disable();

    public Optional<CompletableFuture<Void>> destroyImmediately();

    public Optional<CompletableFuture<Void>> destroy();

    public boolean waitForDependencies();

    public default void escapeFence()
    {
        escapeState(State.FENCED);
    }

    public boolean enterFence(FenceEstablisher establisher, FenceObject object);

    public boolean exitFence(FenceEstablisher establisher, FenceObject object);

    public default boolean exitFences(FenceEstablisher establisher)
    {
        synchronized (getState()) {
            if (establisher != getCurrentEstablisher())
                return false;

            Set<FenceObject> objects = new HashSet<>(getCurrentFences());
            for (FenceObject object : objects)
                exitFence(establisher, object);

            return true;
        }
    }

    public FenceEstablisher getCurrentEstablisher();

    public Set<FenceObject> getCurrentFences();

    public void escapeState(State state);

    enum State {
        MOUNTED(0x00),
        LOADED(0x01),
        ENABLED(0x02),
        DISABLED(0x04),
        DESTROYED(0x08),
        FENCED(0x10),
        FAILED(0x20);

        private State(int code)
        {
            this.code = code;
        }

        public int code()
        {
            return code;
        }

        private final int code;
    }

    public static interface FenceEstablisher
    {
        public String getFenceEstablisherName();

        public default void compete(KeletonModule module, FenceEstablisher establisher, FenceObject object)
        {
        }

        public default Optional<PluginContainer> getPlugin()
        {
            return Sponge.getPluginManager().getPlugin(getFenceEstablisherName());
        }
    }

    public static interface FenceObject
    {
        public String getFenceObjectName();
    }
}
