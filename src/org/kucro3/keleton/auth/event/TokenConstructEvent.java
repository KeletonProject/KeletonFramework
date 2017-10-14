package org.kucro3.keleton.auth.event;

public interface TokenConstructEvent extends TokenEvent {
	interface Pre extends TokenConstructEvent, TokenEvent.Pre {}
	
	interface Constructed extends TokenConstructEvent, TokenEvent.Completed {}
}
