package org.kucro3.keleton.world;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface Located {
	public Location<World> getLocation();
}
