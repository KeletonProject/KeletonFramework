package org.kucro3.keleton.event;

public interface ProceduralEvent extends org.spongepowered.api.event.Event {
	public interface Completed extends ProceduralEvent
	{
	}
	
	public interface Failed extends ProceduralEvent
	{
	}
	
	public interface Pre extends ProceduralEvent
	{
	}
}
