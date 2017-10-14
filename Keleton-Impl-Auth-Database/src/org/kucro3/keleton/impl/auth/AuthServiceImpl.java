package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.AuthException;
import org.kucro3.keleton.auth.AuthResult;
import org.kucro3.keleton.auth.AuthResults;
import org.kucro3.keleton.auth.AuthService;
import org.kucro3.keleton.auth.AuthTokenPool;
import org.kucro3.keleton.auth.AuthTokenResult;
import org.kucro3.keleton.auth.AuthUtil;
import org.kucro3.keleton.auth.event.RegisterEvent;
import org.kucro3.keleton.auth.event.TokenDestroyEvent;
import org.kucro3.keleton.auth.event.TokenLoginEvent;
import org.kucro3.keleton.auth.event.TokenLogoutEvent;
import org.kucro3.keleton.auth.event.TokenUpdatePasswordEvent;
import org.kucro3.keleton.auth.event.TokenUpdateTimestampEvent;
import org.kucro3.keleton.cause.FromUniqueService;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.filter.cause.Named;

public class AuthServiceImpl implements AuthService {
	AuthServiceImpl(DatabaseConnection db) throws AuthException
	{
		this.vaild = true;
		this.db = db;
		this.pool = new AuthTokenPoolImpl(this, db);
		this.uuid = UUID.randomUUID();
		Sponge.getEventManager().registerListeners(SpongeMain.getInstance(), this);
	}

	@Override
	public AuthTokenPool getTokenPool()
	{
		return pool;
	}

	@Override
	public boolean isOnline(UUID uuid, Cause cause) throws AuthException
	{
		AuthTokenResult token = pool.getToken(uuid, cause);
		if(token.isCancelled())
			return false;
		return token.getToken().isPresent() ? token.getToken().get().isOnline() : false;
	}

	@Override
	public boolean isRegistered(UUID uuid, Cause cause) throws AuthException
	{
		return pool.available(uuid, cause);
	}

	@Override
	public boolean isVaild() 
	{
		return vaild;
	}
	
	@Override
	public UUID getUniqueId()
	{
		return uuid;
	}

	@Override
	public AuthResult checkPassword(UUID uuid, String password, Cause cause) throws AuthException
	{
		AuthTokenResult _token = pool.getToken(uuid, cause);
		if(_token.isCancelled())
			return AuthResults.CANCELLED;
		if(!_token.getToken().isPresent())
			return AuthResults.NOT_REGISTERED;
		AuthTokenImpl token = (AuthTokenImpl) _token.getToken().get();
		try {
			if(!token.md5().equals(AuthUtil.toMD5(password)))
				return AuthResults.WRONG_PASSWORD;
		} catch (Exception e) {
			throw new AuthException(e);
		}
		return AuthResults.PASSED;
	}
	
	@Override
	public AuthResult changePassword(UUID uuid, String oldpassword, String newpassword, Cause cause) throws AuthException
	{
		AuthTokenResult _token = pool.getToken(uuid, cause);
		if(_token.isCancelled())
			return AuthResults.CANCELLED;
		if(!_token.getToken().isPresent())
			return AuthResults.NOT_REGISTERED;
		AuthTokenImpl token = (AuthTokenImpl) _token.getToken().get();
		try {
			String oldMd5 = AuthUtil.toMD5(oldpassword);
			String newMd5 = AuthUtil.toMD5(newpassword);
			if(!oldMd5.equals(token.md5()))
				return AuthResults.WRONG_PASSWORD;
			token.md5(newMd5, cause);
			return AuthResults.PASSED;
		} catch (Exception e) {
			throw new AuthException(e);
		}
	}

	@Override
	public AuthResult login(UUID uuid, String password, Cause cause) throws AuthException
	{
		AuthTokenResult _token = pool.getToken(uuid, cause);
		if(_token.isCancelled())
			return AuthResults.CANCELLED;
		if(!_token.getToken().isPresent())
			return AuthResults.NOT_REGISTERED;
		AuthTokenImpl token = (AuthTokenImpl) _token.getToken().get();
		if(token.isOnline())
			return AuthResults.ALREADY_LOGGED_IN;
		try {
			// event
			TokenLoginEvent.Pre event_pre = new TokenLoginEventImpl.Pre(cause.merge(Causes.fromHandler(this.uuid)), this.uuid, token);
			Sponge.getEventManager().post(event_pre);
			if(event_pre.isCancelled())
				return AuthResults.CANCELLED;
			
			String md5 = AuthUtil.toMD5(password);
			if(md5.equals(token.md5()))
			{
				token.online(true);
				token.lastlogin(System.currentTimeMillis(), cause);
				
				// event
				TokenLoginEvent.LoggedIn event_completed = new TokenLoginEventImpl.LoggedIn(cause.merge(Causes.fromHandler(this.uuid)), this.uuid, token);
				Sponge.getEventManager().post(event_completed);
				
				return AuthResults.PASSED;
			}
			else
				return AuthResults.WRONG_PASSWORD;
		} catch (Exception e) {
			throw new AuthException(e);
		}
	}

	@Override
	public AuthResult logout(UUID uuid, Cause cause) throws AuthException
	{
		AuthTokenResult _token = pool.getToken(uuid, cause);
		if(_token.isCancelled())
			return AuthResults.CANCELLED;
		if(!_token.getToken().isPresent())
			return AuthResults.NOT_REGISTERED;
		AuthTokenImpl token = (AuthTokenImpl) _token.getToken().get();
		if(!token.isOnline())
			return AuthResults.NOT_LOGGED_IN;
		
		// event
		TokenLogoutEvent.Pre event_pre = new TokenLogoutEventImpl.Pre(cause.merge(Causes.fromHandler(this.uuid)), this.uuid, token);
		Sponge.getEventManager().post(event_pre);
		if(event_pre.isCancelled())
			return AuthResults.CANCELLED;
		
		token.online(false);
		
		// event
		TokenLogoutEvent.LoggedOut event = new TokenLogoutEventImpl.LoggedOut(cause.merge(Causes.fromHandler(this.uuid)), this.uuid, token);
		Sponge.getEventManager().post(event);
		
		return AuthResults.PASSED;
	}

	@Override
	public AuthResult register(UUID uuid, String password, Cause cause) throws AuthException
	{
		AuthTokenResult _token = pool.getToken(uuid, cause);
		if(_token.isCancelled())
			return AuthResults.CANCELLED;
		if(_token.getToken().isPresent())
			return AuthResults.ALREADY_REGISTERED;
		
		// event
		RegisterEvent.Pre event_pre = new RegisterEventImpl.Pre(cause.merge(Causes.fromHandler(this.uuid)), this.uuid, uuid);
		Sponge.getEventManager().post(event_pre);
		if(event_pre.isCancelled())
			return AuthResults.CANCELLED;
		
		try {
			String md5 = AuthUtil.toMD5(password);
			if(pool.newToken(uuid, md5, cause).isCancelled())
				return AuthResults.CANCELLED;
			
			// event
			RegisterEvent.Registered event = new RegisterEventImpl.Registered(cause.merge(Causes.fromHandler(this.uuid)), this.uuid, uuid);
			Sponge.getEventManager().post(event);
			
			return AuthResults.PASSED;
		} catch (Exception e) {
			throw new AuthException(e);
		}
	}
	
	void vaild(boolean vaild)
	{
		this.vaild = vaild;
	}
	
	boolean isSelfOperation(FromUniqueService handler)
	{
		return handler.getUniqueId().equals(uuid);
	}
	
	@Listener
	public void _SYNC_onUpdatePassword(TokenUpdatePasswordEvent.Updated event, @Named("handler") FromUniqueService handler)
	{
		if(isSelfOperation(handler))
			return;
		
		UUID user = event.getToken().getUUID();
		AuthTokenImpl impl = pool.tokens.get(user);
		if(impl != null)
			impl._SYNC_md5(event.getToken().getPassword());
	}
	
	@Listener
	public void _SYNC_onUpdateTimestamp(TokenUpdateTimestampEvent.Updated event, @Named("handler") FromUniqueService handler)
	{
		if(isSelfOperation(handler))
			return;
		
		UUID user = event.getToken().getUUID();
		AuthTokenImpl impl = pool.tokens.get(user);
		if(impl != null)
			impl._SYNC_lastlogin(event.getToken().lastLogin());
	}
	
	@Listener
	public void _SYNC_onDestroy(TokenDestroyEvent.Destroyed event, @Named("handler") FromUniqueService handler)
	{
		if(isSelfOperation(handler))
			return;
		
		UUID user = event.getToken().getUUID();
		AuthTokenImpl impl = pool.tokens.remove(user);
		if(impl != null)
			impl.vaild(false);
	}
	
	final UUID uuid;
	
	final DatabaseConnection db;
	
	private final AuthTokenPoolImpl pool;
	
	private boolean vaild;
}