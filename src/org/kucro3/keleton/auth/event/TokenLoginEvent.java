package org.kucro3.keleton.auth.event;

public interface TokenLoginEvent extends TokenEvent {
	interface Pre extends TokenLoginEvent, TokenEvent.Pre {}
	
	interface LoggedIn extends TokenLoginEvent, TokenEvent.Completed {}
}
