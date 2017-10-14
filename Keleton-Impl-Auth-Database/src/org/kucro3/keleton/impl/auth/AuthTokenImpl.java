package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.auth.AuthException;
import org.kucro3.keleton.auth.AuthToken;
import org.spongepowered.api.event.cause.Cause;

class AuthTokenImpl implements AuthToken {
	AuthTokenImpl(AuthTokenPoolImpl owner, UUID uuid, String md5, long lastLogin, long firstLogin, Cause cause)
	{
		this.uuid = uuid;
		this.md5 = md5;
		this.lastLogin = lastLogin;
		this.firstLogin = firstLogin;
		this.owner = owner;
		this.cause = cause;
		this.vaild = true;
	}
	
	@Override
	public long firstLogin() 
	{
		check();
		return firstLogin;
	}

	@Override
	public UUID getUUID()
	{
		check();
		return uuid;
	}

	@Override
	public boolean isOnline() 
	{
		check();
		return online;
	}

	@Override
	public boolean isVaild() 
	{
		if(!owner.isVaild())
			return false;
		return vaild;
	}

	@Override
	public long lastLogin() 
	{
		check();
		return lastLogin;
	}
	
	@Override
	public Cause getCause()
	{
		return cause;
	}
	
	@Override
	public String getPassword()
	{
		return md5();
	}
	
	String md5()
	{
		return md5;
	}
	
	void md5(String md5, Cause cause) throws AuthException
	{
		this.md5 = md5;
		owner.update(uuid, cause, true);
	}
	
	void check()
	{
		if(!isVaild())
			throw new IllegalStateException("Token unreachable");
	}
	
	void vaild(boolean vaild)
	{
		this.vaild = vaild;
	}
	
	void online(boolean online)
	{
		this.online = online;
	}
	
	void lastlogin(long lastLogin, Cause cause) throws AuthException
	{
		this.lastLogin = lastLogin;
		owner.update(uuid, cause, false);
	}
	
	void _SYNC_md5(String md5)
	{
		this.md5 = md5;
	}
	
	void _SYNC_lastlogin(long lastlogin)
	{
		this.lastLogin = lastlogin;
	}
	
	private final Cause cause;
	
	private String md5;
	
	private boolean online;
	
	private boolean vaild;
	
	private final UUID uuid;
	
	private long lastLogin;
	
	private final long firstLogin;
	
	private final AuthTokenPoolImpl owner;
}