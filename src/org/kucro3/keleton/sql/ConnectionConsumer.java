package org.kucro3.keleton.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionConsumer {
	void accept(Connection connection) throws SQLException;
}