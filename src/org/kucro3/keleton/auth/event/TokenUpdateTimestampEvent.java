package org.kucro3.keleton.auth.event;

public interface TokenUpdateTimestampEvent extends TokenUpdateEvent {
	interface Pre extends TokenUpdateTimestampEvent, TokenUpdateEvent.Pre {}
	
	interface Updated extends TokenUpdateTimestampEvent, TokenUpdateEvent.Updated {}
}
