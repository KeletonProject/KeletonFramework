package org.kucro3.keleton.world.home.event;

import org.kucro3.keleton.world.TeleportationResult;
import org.kucro3.keleton.world.home.Home;

public interface HomeTeleportEvent extends HomeEvent {
	public Home getHome();
	
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
