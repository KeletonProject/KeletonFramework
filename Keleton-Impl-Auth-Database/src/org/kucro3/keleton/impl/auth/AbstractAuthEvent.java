package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.event.AuthEvent;
import org.spongepowered.api.event.cause.Cause;

abstract class AbstractAuthEvent implements AuthEvent {
	AbstractAuthEvent(Cause cause, UUID handler)
	{
		this.cause = cause;
		this.handler = handler;
	}
	
	@Override
	public Cause getCause() 
	{
		return cause;
	}

	@Override
	public UUID getHandler()
	{
		return handler;
	}
	
	protected final UUID handler;

	protected final Cause cause;
	
	static class Pre extends AbstractAuthEvent implements AuthEvent.Pre
	{
		Pre(Cause cause, UUID handler) 
		{
			super(cause, handler);
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
	
	static class Completed extends AbstractAuthEvent implements AuthEvent.Completed
	{
		Completed(Cause cause, UUID handler)
		{
			super(cause, handler);
		}
	}
}
