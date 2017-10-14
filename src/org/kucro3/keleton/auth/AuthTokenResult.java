package org.kucro3.keleton.auth;

import java.util.Optional;

public interface AuthTokenResult {
	public boolean isCancelled();
	
	public Optional<AuthToken> getToken();
}