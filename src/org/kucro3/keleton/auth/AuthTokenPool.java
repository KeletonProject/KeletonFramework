package org.kucro3.keleton.auth;

import java.util.UUID;

import org.spongepowered.api.event.cause.Cause;

public interface AuthTokenPool extends Transient {
	public boolean available(UUID uuid, Cause cause) throws AuthException;
	
	public AuthTokenResult newToken(UUID uuid, String password, Cause cause) throws AuthException;
	
	public AuthTokenResult getToken(UUID uuid, Cause cause) throws AuthException;
	
	public AuthTokenResult destroyToken(UUID uuid, Cause cause) throws AuthException;
	
	@Override
	public boolean isVaild();
}