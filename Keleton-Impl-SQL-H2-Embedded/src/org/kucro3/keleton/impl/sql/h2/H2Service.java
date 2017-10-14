package org.kucro3.keleton.impl.sql.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.sql.ConnectionConsumer;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.kucro3.keleton.sql.DatabaseKeys;
import org.kucro3.keleton.sql.DatabasePool;
import org.kucro3.keleton.sql.JDBCUrl;

class H2Service implements DatabaseConnection {
	H2Service(String url, Connector connector) throws SQLException
	{
		this.connection = connector.connect(url);
	}
	
	public static H2Service getService(String urlName, final String username, final String password) throws SQLException
	{
		Optional<SimpleLogging> logger = ObjectService.get(SERVICE_SIMPLE_LOGGING);
		H2Service service = CREATED.get(urlName);
		if(service == null)
		{
			logger.ifPresent((provider) -> {
				provider.info("Registering database: " + urlName);
			});
			service = new H2Service(urlName, (url) -> H2Connector.connect(url, username, password));
			CREATED.put(urlName, service);
		}
		return service;
	}
	
	@Override
	public void close() throws SQLException
	{
		checkClosed();
		if(connection != null)
			connection.close();
		this.closed = true;
	}
	
	@Override
	public Optional<ResultSet> execute(String sql) throws SQLException 
	{
		checkClosed();
		Reference<ResultSet> res = new Reference<>();
		process((connection) -> {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.execute();
			res.reference = ps.getResultSet();
		});
		return Optional.ofNullable(res.reference);
	}
	
	@Override
	public PreparedStatement prepareStatement(String statement) throws SQLException
	{
		return connection.prepareStatement(statement);
	}
	
	@Override
	public void process(ConnectionConsumer consumer) throws SQLException
	{
		checkClosed();
		consumer.accept(connection);
	}
	
	void checkClosed() throws SQLException
	{
		if(closed)
			throw new SQLException("Connection already closed");
	}
	
	public static void touch()
	{
		// <clinit>
		H2Connector.touch();
	}
	
	private Connection connection;
	
	private boolean closed;
	
	private static final Map<String, H2Service> CREATED = new HashMap<>();
	
	public static final Key<SimpleLogging> SERVICE_SIMPLE_LOGGING = Key.of("H2Service$SERVICE_SIMPLE_LOGGING", null, SimpleLogging.class);
	
	public static interface SimpleLogging
	{
		void info(String info);
	}
	
	static {
		final H2DatabasePool pool = new H2DatabasePool();
		ObjectService.put(DatabaseKeys.DATABASE, pool);
		ObjectService.put(DatabaseKeys.JDBC_URL_FACTORY, H2Service::provideUrl);
	}
	
	static JDBCUrl provideUrl(String name)
	{
		return new JDBCUrlImpl("H2", "jdbc:h2:.\\database\\" + name);
	}
	
	static class JDBCUrlImpl implements JDBCUrl
	{
		JDBCUrlImpl(String db, String url)
		{
			this.db = db;
			this.url = url;
		}

		@Override
		public String getDatabase() 
		{
			return db;
		}

		@Override
		public String toURL() 
		{
			return url;
		}
		
		private final String db, url;
	}
	
	static class H2DatabasePool implements DatabasePool
	{
		@Override
		public Optional<DatabaseConnection> forDatabase(JDBCUrl url, String user, String password)
				throws SQLException {
			return Optional.ofNullable(H2Service.getService(url.toURL(), user, password));
		}
	}
	
	private static interface Connector
	{
		public Connection connect(String url) throws SQLException;
	}
	
	private static class Reference<T>
	{
		private T reference;
	}
}