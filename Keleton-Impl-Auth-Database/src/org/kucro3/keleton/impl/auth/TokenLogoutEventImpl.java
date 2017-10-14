package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.AuthToken;
import org.kucro3.keleton.auth.event.TokenLogoutEvent;
import org.spongepowered.api.event.cause.Cause;

class TokenLogoutEventImpl {
	private TokenLogoutEventImpl()
	{
	}
	
	static class Pre extends AbstractTokenEvent.Pre implements TokenLogoutEvent.Pre
	{
		Pre(Cause cause, UUID handler, AuthToken token)
		{
			super(cause, handler, token);
		}
	}
	
	static class LoggedOut extends AbstractTokenEvent.Pre implements TokenLogoutEvent.LoggedOut
	{
		LoggedOut(Cause cause, UUID handler, AuthToken token)
		{
			super(cause, handler, token);
		}
	}
}
