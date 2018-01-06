package org.kucro3.keleton.implementation;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface KeletonModule {
    public String getId();

    public Set<String> getDependencies();

    public KeletonInstance getInstance();

    public boolean supportDisabling();

    public State getState();

    public Optional<CompletableFuture<Void>> load();

    public Optional<CompletableFuture<Void>> enable();

    public Optional<CompletableFuture<Void>> disable();

    public Optional<CompletableFuture<Void>> destroy();

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
}
