package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.event.QueryEvent;
import org.spongepowered.api.event.cause.Cause;

class QueryEventImpl {
	private QueryEventImpl()
	{
	}
	
	static class Pre extends AbstractAuthEvent.Pre implements QueryEvent.Pre
	{
		Pre(Cause cause, UUID handler, UUID key) 
		{
			super(cause, handler);
			this.key = key;
		}
		
		@Override
		public UUID getKey() 
		{
			return this.key;
		}
		
		private final UUID key;
	}
	
	static class Completed extends AbstractAuthEvent.Completed implements QueryEvent.Completed
	{
		Completed(Cause cause, UUID handler, UUID key) 
		{
			super(cause, handler);
			this.key = key;
		}

		@Override
		public UUID getKey() 
		{
			return this.key;
		}
		
		private final UUID key;
	}
}