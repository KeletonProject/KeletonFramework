package org.kucro3.keleton.auth.event;

public interface TokenDestroyEvent extends TokenEvent {
	interface Pre extends TokenDestroyEvent, TokenEvent.Pre {}
	
	interface Destroyed extends TokenDestroyEvent, TokenEvent.Completed {}
}
