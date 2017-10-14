package org.kucro3.keleton.impl.i18n;

import java.io.File;

import org.kucro3.keleton.i18n.LocaleService;
import org.kucro3.keleton.keyring.ObjectService;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

@Plugin(id = "keleton-i18n",
		name = "keleton-i18n",
		version = "1.0",
		description = "International Implementation for Keleton Framework",
		authors = "Kumonda221")
public class SpongeMain {
	@Inject
	public SpongeMain(Logger logger)
	{
		ensureFolder();
		instance = this;
		this.logger = logger;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	@Listener
	public void onLoad(GameConstructionEvent event)
	{
		LocaleService.TOKEN.put(new LocaleServiceImpl(LOCALE_FOLDER));
	}
	
	public static void ensureFolder()
	{
		File file = LOCALE_FOLDER;
		if(!file.exists() || !file.isDirectory())
			file.mkdir();
	}
	
	public static SpongeMain getInstance()
	{
		return instance;
	}
	
	public static final File LOCALE_FOLDER = new File(".\\locale");
	
	private static SpongeMain instance;
	
	private final Logger logger;
}