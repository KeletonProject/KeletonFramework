package org.kucro3.keleton.economy;

import java.util.Optional;

import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.KeyToken;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

public interface EnhancedEconomyService extends EconomyService {
	public boolean registerCurrency(EnhancedCurrency currency);
	
	public default boolean unregisterCurrency(Currency currency)
	{
		return unregisterCurrency(currency.getId());
	}
	
	public boolean unregisterCurrency(String id);
	
	public Optional<Currency> getCurrency(String id);
	
	public boolean trimAccounts();
	
	public boolean setDefaultCurrency(EnhancedCurrency currency);
	
	public EnhancedCurrency.Builder newCurrencyBuilder();

	public static final KeyToken<EnhancedEconomyService> TOKEN =
			KeyToken.of(Key.of("EnhancedEconomyService", EnhancedEconomyService.class, EnhancedEconomyService.class));
}
