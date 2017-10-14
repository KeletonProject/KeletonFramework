package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.AuthToken;
import org.kucro3.keleton.auth.event.TokenEvent;
import org.spongepowered.api.event.cause.Cause;

abstract class AbstractTokenEvent extends AbstractAuthEvent implements TokenEvent {
	AbstractTokenEvent(Cause cause, UUID handler, AuthToken token)
	{
		super(cause, handler);
		this.token = token;
	}

	@Override
	public AuthToken getToken() 
	{
		return token;
	}
	
	protected final AuthToken token;
	
	static abstract class Pre extends AbstractTokenEvent implements TokenEvent.Pre
	{
		Pre(Cause cause, UUID handler, AuthToken token)
		{
			super(cause, handler, token);
		}

		@Override
		public boolean isCancelled() 
		{
			return cancelled;
		}

		@Override
		public void setCancelled(boolean cancel) 
		{
			this.cancelled = cancel;
		}
		
		protected boolean cancelled;
	}
	
	static abstract class Completed extends AbstractTokenEvent implements TokenEvent.Completed
	{
		Completed(Cause cause, UUID handler, AuthToken token) 
		{
			super(cause, handler, token);
		}
	}
}
