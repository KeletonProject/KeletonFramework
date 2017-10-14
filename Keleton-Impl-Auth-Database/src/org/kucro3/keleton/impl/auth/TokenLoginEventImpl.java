package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.AuthToken;
import org.kucro3.keleton.auth.event.TokenLoginEvent;
import org.spongepowered.api.event.cause.Cause;

class TokenLoginEventImpl {
	private TokenLoginEventImpl() 
	{
	}
	
	static class Pre extends AbstractTokenEvent.Pre implements TokenLoginEvent.Pre
	{
		Pre(Cause cause, UUID handler, AuthToken token)
		{
			super(cause, handler, token);
		}
	}
	
	static class LoggedIn extends AbstractTokenEvent.Completed implements TokenLoginEvent.LoggedIn
	{
		LoggedIn(Cause cause, UUID handler, AuthToken token)
		{
			super(cause, handler, token);
		}
	}
}
