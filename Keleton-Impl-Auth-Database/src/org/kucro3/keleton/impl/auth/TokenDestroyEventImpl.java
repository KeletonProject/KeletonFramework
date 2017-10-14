package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.AuthToken;
import org.kucro3.keleton.auth.event.TokenDestroyEvent;
import org.spongepowered.api.event.cause.Cause;

class TokenDestroyEventImpl {
	private TokenDestroyEventImpl() 
	{
	}
	
	static class Pre extends AbstractTokenEvent.Pre implements TokenDestroyEvent.Pre
	{
		Pre(Cause cause, UUID handler, AuthToken token) 
		{
			super(cause, handler, token);
		}
	}
	
	static class Destroyed extends AbstractTokenEvent.Completed implements TokenDestroyEvent.Destroyed
	{
		Destroyed(Cause cause, UUID handler, AuthToken token)
		{
			super(cause, handler, token);
		}
	}
}
