package org.kucro3.keleton.auth.event;

public interface TokenUpdateEvent extends TokenEvent {
	interface Pre extends TokenUpdateEvent, TokenEvent.Pre {}
	
	interface Updated extends TokenUpdateEvent, TokenEvent.Completed {}
}
