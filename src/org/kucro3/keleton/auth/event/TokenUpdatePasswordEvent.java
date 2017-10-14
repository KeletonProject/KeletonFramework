package org.kucro3.keleton.auth.event;

public interface TokenUpdatePasswordEvent extends TokenUpdateEvent {
	interface Pre extends TokenUpdatePasswordEvent, TokenUpdateEvent.Pre {}
	
	interface Updated extends TokenUpdatePasswordEvent, TokenUpdateEvent.Updated {}
}
