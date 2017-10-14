package org.kucro3.keleton.impl.auth;

import java.util.Optional;

import org.kucro3.keleton.auth.AuthToken;
import org.kucro3.keleton.auth.AuthTokenResult;

class AuthTokenResultImpl implements AuthTokenResult {
	AuthTokenResultImpl(AuthToken token, boolean isCancelled)
	{
		this.token = Optional.ofNullable(token);
		this.isCancelled = isCancelled;
	}
	
	static AuthTokenResult cancelled()
	{
		return new AuthTokenResultImpl(null, true);
	}
	
	static AuthTokenResult token(AuthToken token)
	{
		return new AuthTokenResultImpl(token, false);
	}
	
	@Override
	public Optional<AuthToken> getToken() 
	{
		return token;
	}

	@Override
	public boolean isCancelled() 
	{
		return isCancelled;
	}
	
	private final Optional<AuthToken> token;
	
	private final boolean isCancelled;
}
