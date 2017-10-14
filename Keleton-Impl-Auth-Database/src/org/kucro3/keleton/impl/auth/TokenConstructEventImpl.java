package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.AuthToken;
import org.kucro3.keleton.auth.event.TokenConstructEvent;
import org.spongepowered.api.event.cause.Cause;

class TokenConstructEventImpl {
	private TokenConstructEventImpl()
	{
	}
	
	static class Pre extends AbstractTokenEvent.Pre implements TokenConstructEvent.Pre
	{
		Pre(Cause cause, UUID handler, AuthToken token)
		{
			super(cause, handler, token);
		}
	}
	
	static class Constructed extends AbstractTokenEvent.Completed implements TokenConstructEvent.Constructed
	{
		Constructed(Cause cause, UUID handler, AuthToken token)
		{
			super(cause, handler, token);
		}
	}
}
