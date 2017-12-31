package org.kucro3.keleton;

import com.google.inject.Inject;
import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.implementation.loader.KeletonModuleLoader;
import org.kucro3.keleton.implementation.loader.KeletonModuleManagerImpl;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "keletonframework",
        name = "keletonframework",
        version = "1.0",
        description = "Keleton Framework",
        authors = "Kumonda221")
@KeletonModule(name = "keletonframework",
               dependencies = {"keleton-utility"})
public class SpongeMain extends KeletonInstance {
    @Inject
    public SpongeMain(Logger logger)
    {
        this.logger = logger;
    }

    @Listener
    public void onLoad(GamePreInitializationEvent event)
    {
        KeletonModuleManagerImpl impl;
        Keleton.manager = impl = new KeletonModuleManagerImpl();

        KeletonModuleLoader.load(impl, (instance, exception) -> logger.error("Failed to mount module: " + instance.getClass().getCanonicalName(), exception));
    }

    public Logger getLogger()
    {
        return logger;
    }

    final Logger logger;
}
