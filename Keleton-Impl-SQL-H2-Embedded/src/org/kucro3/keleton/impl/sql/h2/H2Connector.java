package org.kucro3.keleton.impl.sql.h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class H2Connector {
	public static void touch()
	{
		// <clinit>
	}
	
	static Connection connect(String url, String user, String password) throws SQLException
	{
		return DriverManager.getConnection(url, user, password);
	}
	
	static {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}