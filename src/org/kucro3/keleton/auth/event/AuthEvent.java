package org.kucro3.keleton.auth.event;

import java.util.UUID;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;

public interface AuthEvent extends Event {
	public UUID getHandler();
	
	static interface Pre extends AuthEvent, Cancellable {}
	
	static interface Completed extends AuthEvent {}
}