package org.kucro3.keleton.world.home.event;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.event.CancellableWithCause;
import org.kucro3.keleton.event.ProceduralEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface HomeEvent extends ProceduralEvent {
	public @CaseInsensitive String getName();
	
	public Location<World> getLocation();
	
	public interface Pre extends HomeEvent, CancellableWithCause
	{
	}
	
	public interface Completed extends HomeEvent
	{
	}
	
	public interface Failed extends HomeEvent
	{
	}
}
