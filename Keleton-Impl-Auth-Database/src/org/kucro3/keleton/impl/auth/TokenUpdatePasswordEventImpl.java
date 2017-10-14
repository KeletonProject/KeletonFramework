package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.AuthToken;
import org.kucro3.keleton.auth.event.TokenUpdatePasswordEvent;
import org.spongepowered.api.event.cause.Cause;

class TokenUpdatePasswordEventImpl {
	private TokenUpdatePasswordEventImpl() 
	{
	}
	
	static class Pre extends AbstractTokenEvent.Pre implements TokenUpdatePasswordEvent.Pre
	{
		Pre(Cause cause, UUID handler, AuthToken token) 
		{
			super(cause, handler, token);
		}
	}
	
	static class Updated extends AbstractTokenEvent.Completed implements TokenUpdatePasswordEvent.Updated
	{
		Updated(Cause cause, UUID handler, AuthToken token) 
		{
			super(cause, handler, token);
		}
	}
}
