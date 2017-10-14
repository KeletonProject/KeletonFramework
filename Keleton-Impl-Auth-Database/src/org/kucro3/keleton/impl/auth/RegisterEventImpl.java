package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.event.RegisterEvent;
import org.spongepowered.api.event.cause.Cause;

class RegisterEventImpl {
	private RegisterEventImpl() 
	{
	}
	
	static class Pre extends AbstractAuthEvent.Pre implements RegisterEvent.Pre
	{
		Pre(Cause cause, UUID handler, UUID user) 
		{
			super(cause, handler);
			this.user = user;
		}

		@Override
		public UUID getUser()
		{
			return user;
		}
		
		private final UUID user;
	}
	
	static class Registered extends AbstractAuthEvent.Completed implements RegisterEvent.Registered
	{
		Registered(Cause cause, UUID handler, UUID user)
		{
			super(cause, handler);
			this.user = user;
		}

		@Override
		public UUID getUser()
		{
			return user;
		}
		
		private final UUID user;
	}
}