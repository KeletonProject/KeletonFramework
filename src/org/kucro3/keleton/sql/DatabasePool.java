package org.kucro3.keleton.sql;

import java.sql.SQLException;
import java.util.Optional;

public interface DatabasePool {
	public default Optional<DatabaseConnection> forDatabase(JDBCUrl url) throws SQLException
	{
		return forDatabase(url, "", "");
	}
	
	public Optional<DatabaseConnection> forDatabase(JDBCUrl url, String user, String password) throws SQLException;
}