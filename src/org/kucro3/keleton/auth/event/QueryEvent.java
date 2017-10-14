package org.kucro3.keleton.auth.event;

import java.util.UUID;

public interface QueryEvent extends AuthEvent {
	public UUID getKey();
	
	interface Pre extends QueryEvent, AuthEvent.Pre {}
	
	interface Completed extends QueryEvent, AuthEvent.Completed {}
}