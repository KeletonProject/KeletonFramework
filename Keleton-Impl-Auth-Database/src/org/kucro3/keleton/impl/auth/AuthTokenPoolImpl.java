package org.kucro3.keleton.impl.auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.kucro3.keleton.auth.AuthException;
import org.kucro3.keleton.auth.AuthQueryException;
import org.kucro3.keleton.auth.AuthServiceUnreachableException;
import org.kucro3.keleton.auth.AuthTokenDuplicatedException;
import org.kucro3.keleton.auth.AuthTokenPool;
import org.kucro3.keleton.auth.AuthTokenResult;
import org.kucro3.keleton.auth.event.QueryEvent;
import org.kucro3.keleton.auth.event.TokenConstructEvent;
import org.kucro3.keleton.auth.event.TokenDestroyEvent;
import org.kucro3.keleton.auth.event.TokenUpdateEvent;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;

class AuthTokenPoolImpl implements AuthTokenPool {
	AuthTokenPoolImpl(AuthServiceImpl owner, DatabaseConnection db) throws AuthException
	{
		this.vaild = true;
		this.owner = owner;
		this.db = db;
		this.ensureTable();
	}
	
	@Override
	public boolean available(UUID uuid, Cause cause) throws AuthException
	{
		if(!query(uuid, cause))
			return false;
		return tokens.containsKey(uuid);
	}

	@Override
	public AuthTokenResult destroyToken(UUID uuid, Cause cause) throws AuthException
	{
		query(uuid, cause);
		AuthTokenImpl impl = tokens.get(uuid);
		if(impl != null)
			if(!delete(uuid, cause))
				return AuthTokenResultImpl.cancelled();
		return AuthTokenResultImpl.token(impl);
	}

	@Override
	public AuthTokenResult getToken(UUID uuid, Cause cause)  throws AuthException
	{
		if(!tokens.containsKey(uuid))
			if(!query(uuid, cause))
				return AuthTokenResultImpl.cancelled();
		return AuthTokenResultImpl.token(tokens.get(uuid));
	}

	@Override
	public boolean isVaild() 
	{
		if(!owner.isVaild())
			return false;
		return vaild;
	}

	@Override
	public AuthTokenResult newToken(UUID uuid, String password, Cause cause) throws AuthException
	{
		if(!available(uuid, cause))
			if(!insert(uuid, password, cause))
				return AuthTokenResultImpl.cancelled();
			else
				;
		else
			throw new AuthTokenDuplicatedException(uuid.toString());
		return AuthTokenResultImpl.token(checkNonnull(tokens.get(uuid)));
	}
	
	void check() throws AuthException
	{
		if(!isVaild())
			throw new AuthServiceUnreachableException();
	}
	
	synchronized boolean insert(UUID uuid, String password, Cause cause) throws AuthException
	{
		check();
		try {
			long timestamp = System.currentTimeMillis();
			AuthTokenImpl impl = new AuthTokenImpl(this, uuid, password, timestamp, timestamp, cause);
			
			// event
			TokenConstructEvent.Pre event_pre = new TokenConstructEventImpl.Pre(cause.merge(Causes.fromHandler(this.owner.uuid)), this.owner.uuid, impl);
			Sponge.getEventManager().post(event_pre);
			if(event_pre.isCancelled())
				return false;
			
			db.execute("INSERT INTO keleton_auth (UID,Password,FirstLogin,LastLogin) "
					 + "VALUES (\'" + uuid + "\',\'" + password + "\'," + timestamp + "," + timestamp + ");");
			checkNull(tokens.put(uuid, impl));
			
			// event
			TokenConstructEvent.Constructed event = new TokenConstructEventImpl.Constructed(cause.merge(Causes.fromHandler(this.owner.uuid)), this.owner.uuid, impl);
			Sponge.getEventManager().post(event);
		} catch (SQLException e) {
			vaild(false);
			throw new AuthQueryException(e);
		}
		return true;
	}
	
	synchronized boolean update(UUID uuid, Cause cause, boolean password) throws AuthException
	{
		check();
		AuthTokenResult result = this.getToken(uuid, cause);
		
		if(result.isCancelled())
			return false;
		
		if(result.getToken().isPresent())
			try {
				AuthTokenImpl impl = (AuthTokenImpl) result.getToken().get();
				
				// event
				TokenUpdateEvent.Pre event_pre = password ?
						new TokenUpdatePasswordEventImpl.Pre(cause.merge(Causes.fromHandler(this.owner.uuid)), this.owner.uuid, impl) :
						new TokenUpdateTimestampEventImpl.Pre(cause.merge(Causes.fromHandler(this.owner.uuid)), this.owner.uuid, impl);
				Sponge.getEventManager().post(event_pre);
				if(event_pre.isCancelled())
					return false;
				
				db.execute("UPDATE keleton_auth "
						 + "SET Password=\'" + impl.md5() + "\',"
						 +     "LastLogin=" + impl.lastLogin() + " "
						 + "WHERE UID=\'" + uuid.toString() + "\';");
				
				// event
				TokenUpdateEvent.Updated event_updated = password ?
						new TokenUpdatePasswordEventImpl.Updated(cause.merge(Causes.fromHandler(this.owner.uuid)), this.owner.uuid, impl) :
						new TokenUpdateTimestampEventImpl.Updated(cause.merge(Causes.fromHandler(this.owner.uuid)), this.owner.uuid, impl);
				Sponge.getEventManager().post(event_updated);
			} catch (SQLException e) {
				vaild(false);
				throw new AuthQueryException(e);
			}
		return true;
	}
	
	synchronized boolean delete(UUID uuid, Cause cause) throws AuthException
	{
		check();
		try {
			// event
			TokenDestroyEvent.Pre event_pre = new TokenDestroyEventImpl.Pre(cause.merge(Causes.fromHandler(this.owner.uuid)), this.owner.uuid, checkNonnull(tokens.get(uuid)));
			Sponge.getEventManager().post(event_pre);
			if(event_pre.isCancelled())
				return false;
			
			db.execute("DELETE FROM keleton_auth WHERE UID=\'" + uuid.toString() + "\';");
			checkNonnull(tokens.remove(uuid)).vaild(false);
			
			// event
			TokenDestroyEvent.Destroyed event = new TokenDestroyEventImpl.Destroyed(cause.merge(Causes.fromHandler(this.owner.uuid)), this.owner.uuid, checkNonnull(tokens.get(uuid)));
			Sponge.getEventManager().post(event);
		} catch (SQLException e) {
			vaild(false);
			throw new AuthQueryException(e);
		}
		return true;
	}
	
	synchronized boolean query(UUID uuid, Cause cause) throws AuthException
	{
		check();
		if(!tokens.containsKey(uuid)) {
			// event
			QueryEvent.Pre event_pre = new QueryEventImpl.Pre(cause.merge(Causes.fromHandler(this.owner.uuid)), this.owner.uuid, uuid);
			Sponge.getEventManager().post(event_pre);
			if(event_pre.isCancelled())
				return false;
			
			try {
				Optional<ResultSet> optional = db.execute(
						"SELECT * FROM keleton_auth WHERE UID=\'" + uuid.toString() + "\';");
				ResultSet result;
				if(optional.isPresent() && (result = optional.get()).next())
				{
					String md5 = result.getString(result.findColumn("PASSWORD"));
					long firstLogin = result.getLong(result.findColumn("FIRSTLOGIN"));
					long lastLogin = result.getLong(result.findColumn("FIRSTLOGIN"));
					AuthTokenImpl token = new AuthTokenImpl(this, uuid, md5, lastLogin, firstLogin, cause);
					checkNull(tokens.put(uuid, token));
				}
			} catch (SQLException e) {
				vaild(false);
				throw new AuthQueryException(e);
			}
			
			// event
			QueryEvent.Completed event = new QueryEventImpl.Completed(cause.merge(Causes.fromHandler(this.owner.uuid)), this.owner.uuid, uuid);
			Sponge.getEventManager().post(event);
		}
		return true;
	}
	
	synchronized void ensureTable() throws AuthException
	{
		check();
		try {
			db.process((unused) -> {
				db.execute("CREATE TABLE IF NOT EXISTS keleton_auth "
						 + "("
						 + "UID varchar(255) NOT NULL UNIQUE,"
						 + "PASSWORD varchar(255) NOT NULL,"
						 + "FIRSTLOGIN bigint,"
						 + "LASTLOGIN bigint"
						 + ") DEFAULT CHARSET=utf8;");
			});
		} catch (SQLException e) {
			vaild(false);
			throw new AuthQueryException(e);
		}
	}
	
	void vaild(boolean vaild)
	{
		this.vaild = vaild;
	}
	
	void checkNull(Object obj)
	{
		if(obj != null)
			throw new IllegalStateException("Should not reach here");
	}
	
	<T> T checkNonnull(T obj)
	{
		if(obj == null)
			throw new IllegalStateException("Should not reach here");
		return obj;
	}
	
	private boolean vaild;
	
	private final AuthServiceImpl owner;
	
	final Map<UUID, AuthTokenImpl> tokens = new HashMap<>();
	
	private final DatabaseConnection db;
}