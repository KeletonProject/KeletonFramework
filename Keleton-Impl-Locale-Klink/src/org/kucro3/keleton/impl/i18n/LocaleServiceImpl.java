package org.kucro3.keleton.impl.i18n;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.kucro3.keleton.i18n.LocaleProperties;
import org.kucro3.keleton.i18n.LocaleService;
import org.kucro3.klink.Executor;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Variables;

class LocaleServiceImpl implements LocaleService {
	LocaleServiceImpl(File folder)
	{
		this.folder = folder;
	}
	
	@Override
	public LocaleProperties getFallbackProperties(String id) 
	{
		return getProperties(Locale.SIMPLIFIED_CHINESE, id);
	}

	@Override
	public LocaleProperties getProperties(Locale locale, String id) 
	{
		String key = key(locale, id);
		LocaleProperties properties = map.get(key);
		if(properties == null)
			map.put(key, properties = tryLoad(key));
		return properties;
	}
	
	LocaleProperties tryLoad(String key)
	{
		try {
			File file = new File(folder, key + ".klnk");
			if(file.exists())
			{
				Variables loaded;
				String envName = "locale:" + key;
				ENGINE.createEnv(envName);
				ENGINE.currentEnv(envName);
				EXECUTOR.execute(ENGINE.compile(file), ENGINE);
				loaded = ENGINE.currentEnv().getVars();
				ENGINE.currentEnv(null);
				return new LocalePropertiesImpl(loaded);
			}
			else
			{
				file.createNewFile();
				return new LocaleProperties.Empty() {};
			}
		} catch (Exception e) {
			SpongeMain.getInstance().getLogger().warn("Unable to load lang file", e);
			return new LocaleProperties.Empty() {};
		}
	}
	
	static String key(Locale locale, String id)
	{
		return id + "-" + locale.toLanguageTag();
	}
	
	private static final Executor EXECUTOR = new Executor()
			{
		{
			this.interruptionHandler = (e) -> {};
			this.scriptExceptionHandler = (e) -> {throw e;};
		}
			};
	
	private static final Klink ENGINE = new Klink();
	
	private final Map<String, LocaleProperties> map = new HashMap<>();
	
	private final File folder;
}