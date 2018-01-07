package org.kucro3.keleton.module.loader;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Collection;

class KeletonModuleDiscoverer {
    Collection<KeletonModuleImpl> discover()
    {
        Collection<PluginContainer> container = Sponge.getPluginManager().getPlugins();
    }
}
