package org.kucro3.keleton.service.ban;

import java.util.UUID;

import org.kucro3.keleton.ban.EnhancedBanService;
import org.kucro3.keleton.cause.FromUniqueService;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.kucro3.keleton.sql.DatabaseKeys;
import org.kucro3.keleton.sql.DatabasePool;
import org.kucro3.keleton.sql.JDBCUrlFactory;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ban.BanService;

import com.google.inject.Inject;

@Plugin(id = "keleton-ban",
		name = "keleton-ban",
		version = "1.0",
		description = "Ban Service Implementation",
		authors = "Kumonda221")
public class SpongeMain {
	@Inject
	public SpongeMain(Logger logger)
	{
		SpongeMain.logger = logger;
		instance = this;
	}
	
	@Listener
	public void onLoad(GamePreInitializationEvent event)
	{
		pc = Sponge.getPluginManager().getPlugin("keleton-ban").get();
		try {
			DatabasePool dbpool = ObjectService.get(DatabaseKeys.DATABASE)
					.orElseThrow(() -> new IllegalStateException("Database service not reachable"));
			JDBCUrlFactory urlfactory = ObjectService.get(DatabaseKeys.JDBC_URL_FACTORY).get();
			DatabaseConnection connection = dbpool.forDatabase(urlfactory.createUrl("keleton-banlist"))
					.orElseThrow(() -> new IllegalStateException("Failed to create database connection"));
			
			service = new BanServiceImpl(connection, logger);
			
			Sponge.getEventManager().registerListeners(this, new BanIpListener());

			EnhancedBanService.TOKEN.put(service);
			Sponge.getServiceManager().setProvider(this, BanService.class, service);
		} catch (Exception e) {
			logger.error("Cannot initialize ban service", e);
		}
	}
	
	static Cause from(UUID uuid)
	{
		return Cause.builder().named("handler", (FromUniqueService) () -> uuid).build();
	}
	
	public static Logger getLogger()
	{
		return logger;
	}
	
	public static SpongeMain getInstance()
	{
		return instance;
	}
	
	private static PluginContainer pc;
	
	private static BanServiceImpl service;
	
	private static Logger logger;
	
	private static SpongeMain instance;
}
