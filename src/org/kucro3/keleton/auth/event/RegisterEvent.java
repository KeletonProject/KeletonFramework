package org.kucro3.keleton.auth.event;

import java.util.UUID;

import org.spongepowered.api.event.Cancellable;

public interface RegisterEvent extends AuthEvent {
	public UUID getUser();
	
	interface Pre extends RegisterEvent, Cancellable {}
	
	interface Registered extends RegisterEvent {}
}
