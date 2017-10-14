package org.kucro3.keleton.auth.event;

import org.kucro3.keleton.auth.AuthToken;
import org.spongepowered.api.event.Cancellable;

public interface TokenEvent extends AuthEvent {
	public AuthToken getToken();
	
	interface Pre extends TokenEvent, Cancellable, AuthEvent.Pre {}
	
	interface Completed extends TokenEvent, AuthEvent.Completed {}
}
