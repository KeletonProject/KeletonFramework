package org.kucro3.keleton.world.home.event;

import org.kucro3.keleton.world.TeleportationResult;
import org.kucro3.keleton.world.home.Home;
import org.spongepowered.api.entity.Entity;

public interface HomeTeleportEvent extends HomeEvent {
	public Home getHome();

	public Entity getEntity();
	
	public interface Pre extends HomeTeleportEvent, HomeEvent.Pre
	{
	}
	
	public interface Completed extends HomeTeleportEvent, HomeEvent.Completed
	{
		public TeleportationResult getResult();
	}
	
	public interface Failed extends HomeTeleportEvent, HomeEvent.Failed
	{
	}
}
