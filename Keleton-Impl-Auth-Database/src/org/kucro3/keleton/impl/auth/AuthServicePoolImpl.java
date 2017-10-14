package org.kucro3.keleton.impl.auth;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.kucro3.keleton.auth.AuthException;
import org.kucro3.keleton.auth.AuthService;
import org.kucro3.keleton.auth.AuthServiceDuplicatedException;
import org.kucro3.keleton.auth.AuthServicePool;
import org.kucro3.keleton.auth.AuthServiceUnreachableException;
import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.kucro3.keleton.sql.DatabasePool;
import org.kucro3.keleton.sql.JDBCUrlFactory;

class AuthServicePoolImpl implements AuthServicePool {
	AuthServicePoolImpl(Key<DatabasePool> dbpool, Key<JDBCUrlFactory> urlfactory)
	{
		this.dbpoolKey = dbpool;
		this.urlfactoryKey = urlfactory;
	}
	
	@Override
	public boolean available(String name) 
	{
		return map.containsKey(name);
	}

	@Override
	public AuthService construct(String name, Object... args) throws AuthException
	{
		AuthServiceImpl impl = map.get(name);
		if(impl != null)
			throw new AuthServiceDuplicatedException(name);
		try {
			Optional<DatabasePool> _dbpool = ObjectService.get(dbpoolKey);
			Optional<JDBCUrlFactory> _urlfactory = ObjectService.get(urlfactoryKey);
			if(!_dbpool.isPresent() || !_urlfactory.isPresent())
				throw new AuthException("Database service not available");
			DatabasePool dbpool = _dbpool.get();
			JDBCUrlFactory urlfactory = _urlfactory.get();
			Optional<DatabaseConnection> _db = args.length == 0 ? 
					dbpool.forDatabase(urlfactory.createUrl(name)) :
					dbpool.forDatabase(urlfactory.createUrl(name), args[0].toString(), args[1].toString());
			if(!_db.isPresent())
				throw new AuthException("Failed to create database connection");
			DatabaseConnection db = _db.get();
			impl = new AuthServiceImpl(db);
			checkNull(map.put(name, impl));
			return impl;
		} catch (SQLException e) {
			throw new AuthException(e);
		}
	}

	@Override
	public void destruct(String name) throws AuthException
	{
		AuthServiceImpl impl = map.get(name);
		if(impl == null)
			throw new AuthServiceUnreachableException(name);
		impl.vaild(false);
		try {
			impl.db.close();
		} catch (SQLException e) {
			throw new AuthException(e);
		}
	}

	@Override
	public AuthService get(String name) 
	{
		return map.get(name);
	}
	
	void checkNull(Object obj)
	{
		if(obj != null)
			throw new IllegalStateException("Should not reach here");
	}
	
	private final Key<DatabasePool> dbpoolKey;
	
	private final Key<JDBCUrlFactory> urlfactoryKey;
	
	private final Map<String, AuthServiceImpl> map = new HashMap<>();
}