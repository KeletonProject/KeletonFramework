package org.kucro3.keleton.auth;

import java.util.UUID;

import org.spongepowered.api.event.cause.Cause;

public interface AuthToken extends Transient {
	public boolean isOnline();
	
	public long firstLogin();
	
	public long lastLogin();
	
	public UUID getUUID();
	
	public Cause getCause();
	
	public String getPassword();
}