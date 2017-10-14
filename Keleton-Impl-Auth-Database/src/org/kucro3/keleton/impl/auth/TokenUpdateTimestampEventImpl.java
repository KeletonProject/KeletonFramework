package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.AuthToken;
import org.kucro3.keleton.auth.event.TokenUpdateTimestampEvent;
import org.spongepowered.api.event.cause.Cause;

class TokenUpdateTimestampEventImpl {
	private TokenUpdateTimestampEventImpl() 
	{
	}
	
	static class Pre extends AbstractTokenEvent.Pre implements TokenUpdateTimestampEvent.Pre
	{
		Pre(Cause cause, UUID handler, AuthToken token) 
		{
			super(cause, handler, token);
		}
	}
	
	static class Updated extends AbstractTokenEvent.Completed implements TokenUpdateTimestampEvent.Updated
	{
		Updated(Cause cause, UUID handler, AuthToken token) 
		{
			super(cause, handler, token);
		}
	}
}
