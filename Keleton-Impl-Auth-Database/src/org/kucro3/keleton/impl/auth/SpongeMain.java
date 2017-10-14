package org.kucro3.keleton.impl.auth;

import org.kucro3.keleton.auth.AuthKeys;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.sql.DatabaseKeys;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

@Plugin(id = "keleton-auth",
	name = "KAuth",
	version = "1.0",
	description = "Auth system Implementation fro Keleton Framework",
	authors = {"Kumonda221"})
public class SpongeMain {
	@Inject
	public SpongeMain(Logger logger)
	{
		this.logger = logger;
		instance = this;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	@Listener
	public void onLoad(GameConstructionEvent event)
	{
		ObjectService.put(AuthKeys.SERVICE_POOL, new AuthServicePoolImpl(DatabaseKeys.DATABASE, DatabaseKeys.JDBC_URL_FACTORY));
	}
	
	public static SpongeMain getInstance()
	{
		return instance;
	}
	
	private static SpongeMain instance;
	
	private final Logger logger;
}