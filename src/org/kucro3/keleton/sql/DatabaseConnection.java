package org.kucro3.keleton.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

public interface DatabaseConnection {	
	public Optional<ResultSet> execute(String sql) throws SQLException;
	
	public void process(ConnectionConsumer consumer) throws SQLException;

	public default <T> T apply(Function<Connection, T> function) throws SQLException
	{
		final Object[] t = new Object[1];
		process((conn) -> t[0] = function.apply(conn));
		return (T) t[0];
	}
	
	public void close() throws SQLException;
	
	public PreparedStatement prepareStatement(String statement) throws SQLException;
}