package org.kucro3.keleton.auth.event;

public interface TokenLogoutEvent extends TokenEvent {
	interface Pre extends TokenLogoutEvent, TokenEvent.Pre {}
	
	interface LoggedOut extends TokenLogoutEvent, TokenEvent.Completed {}
}
