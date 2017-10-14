package org.kucro3.keleton.i18n;

import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.KeyToken;

import java.util.Locale;

public interface LocaleService {
	public LocaleProperties getProperties(Locale locale, String id);

	public default LocaleProperties getProperties(String id)
	{
		return getProperties(Locale.getDefault(), id);
	}

	public LocaleProperties getFallbackProperties(String id);

	public default LocaleProperties fallbackIfAbsent(Locale locale, String id)
	{
		LocaleProperties properties = getProperties(locale, id);
		if(properties instanceof LocaleProperties.Empty)
			return getFallbackProperties(id);
		return properties;
	}

	public default LocaleProperties fallbackIfAbsent(String id)
	{
		return fallbackIfAbsent(Locale.getDefault(), id);
	}

	public static final KeyToken<LocaleService> TOKEN =
			KeyToken.of(Key.of("LocaleService", LocaleService.class, LocaleService.class));
}