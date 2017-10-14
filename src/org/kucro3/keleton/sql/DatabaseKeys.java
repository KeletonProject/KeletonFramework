package org.kucro3.keleton.sql;

import org.kucro3.keleton.keyring.Key;

public interface DatabaseKeys {
	public Key<DatabasePool> DATABASE = Key.of("DATABASE", null, DatabasePool.class);
	
	public Key<JDBCUrlFactory> JDBC_URL_FACTORY = Key.of("JDBC_URL_FACTORY", null, JDBCUrlFactory.class);
}