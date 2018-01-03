package org.kucro3.keleton;

import com.google.inject.Inject;
import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.Module;
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

    }

    public Logger getLogger()
    {
        return logger;
    }

    final Logger logger;
}
