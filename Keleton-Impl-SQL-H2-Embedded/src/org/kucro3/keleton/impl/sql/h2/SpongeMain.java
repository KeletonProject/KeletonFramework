package org.kucro3.keleton.impl.sql.h2;

import org.kucro3.keleton.keyring.ObjectService;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

@Plugin(id = "keleton-sql-h2",
		name = "keleton-sql-h2",
		version = "1.0",
		description = "H2 SQL Implementation for Keleton Framework",
		authors = {"Kumonda221"})
public class SpongeMain {
	@Inject
	public SpongeMain(Logger logger)
	{
		this.logger = logger;
	}
	
	@Listener
	public void onLoad(GameConstructionEvent event)
	{
		ObjectService.put(H2Service.SERVICE_SIMPLE_LOGGING, (info) -> logger.info(info));
		H2Service.touch();
	}
	
	private final Logger logger;
}