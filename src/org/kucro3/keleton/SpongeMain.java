package org.kucro3.keleton;

import com.google.inject.Inject;
import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.Module;
import org.kucro3.keleton.module.loader.KeletonModuleManagerImpl;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "keletonframework",
        name = "keletonframework",
        version = "1.0",
        description = "Keleton Framework",
        authors = "Kumonda221")
@Module(id = "keletonframework")
public class SpongeMain implements KeletonInstance {
    @Inject
    public SpongeMain(Logger logger)
    {
        this.logger = logger;
    }

    @Listener
    public void onLoad(GamePreInitializationEvent event)
    {
        Keleton.manager = new KeletonModuleManagerImpl();
    }

    public Logger getLogger()
    {
        return logger;
    }

    final Logger logger;
}
