package org.kucro3.keleton;

import com.google.inject.Inject;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.implementation.event.KeletonModuleEvent;
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
@KeletonModule(name = "keletonframework")
public class SpongeMain {
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

    @Listener
    public void onModuleLoad(KeletonModuleEvent.Load event)
    {
        logger.info("Loading keleton module: " + event.getModule().getInfo().name());
    }

    @Listener
    public void onModuleEnable(KeletonModuleEvent.Enable event)
    {
        logger.info("Enabling keleton module: " + event.getModule().getInfo().name());
    }

    @Listener
    public void onModuleDisable(KeletonModuleEvent.Disable event)
    {
        logger.info("Disabling keleton module: " + event.getModule().getInfo().name());
    }

    public Logger getLogger()
    {
        return logger;
    }

    final Logger logger;
}
