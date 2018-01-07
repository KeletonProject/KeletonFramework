package org.kucro3.keleton.world.home.event;

import org.kucro3.keleton.world.home.Home;

public interface HomeDeleteEvent extends HomeEvent {
	public interface Pre extends HomeDeleteEvent, HomeEvent.Pre
	{
		public Home getHome();
	}
	
	public interface Completed extends HomeDeleteEvent, HomeEvent.Completed
	{
	}
	
	public interface Failed extends HomeDeleteEvent, HomeEvent.Failed
	{
	}

	public interface Cancelled extends HomeDeleteEvent, HomeEvent.Cancelled
	{
	}
}
