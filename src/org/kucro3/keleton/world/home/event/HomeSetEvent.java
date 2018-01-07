package org.kucro3.keleton.world.home.event;

import org.kucro3.keleton.world.home.Home;

public interface HomeSetEvent extends HomeEvent {
	public interface Pre extends HomeSetEvent, HomeEvent.Pre
	{
	}
	
	public interface Failed extends HomeSetEvent, HomeEvent.Failed
	{
	}
	
	public interface Completed extends HomeSetEvent, HomeEvent.Completed
	{
		public Home getHome();
	}

	public interface Cancelled extends HomeSetEvent, HomeEvent.Cancelled
	{
	}
}
