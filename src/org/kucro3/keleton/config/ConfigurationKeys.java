package org.kucro3.keleton.config;

import org.kucro3.keleton.keyring.Key;

public interface ConfigurationKeys {
	public static final Key<ConfigurationService> SERVICE = Key.of("ConfigurationService", ConfigurationService.class, ConfigurationService.class);
}
