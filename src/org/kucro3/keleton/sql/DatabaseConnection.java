package org.kucro3.keleton.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface DatabaseConnection {	
	public Optional<ResultSet> execute(String sql) throws SQLException;
	
	public void process(ConnectionConsumer consumer) throws SQLException;
	
	public void close() throws SQLException;
	
	public PreparedStatement prepareStatement(String statement) throws SQLException;
}