package org.kucro3.keleton.world;

import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.KeyToken;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface SpawnProvider {
	public Location<World> getSpawn(World world);
	
	public default boolean setSpawn(World world, Location<World> location)
	{
		return false;
	}
	
	public static KeyToken<SpawnProvider> TOKEN = KeyToken.of(Key.of("SpawnProvider", SpawnProvider.class, SpawnProvider.class));
}