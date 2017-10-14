package org.kucro3.keleton.economy;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

public interface EnhancedCurrency extends Currency, DefaultValued {
	public EnhancedEconomyService getOwner();
	
	public default boolean hasMinValue()
	{
		return getMinValue() != null;
	}
	
	public BigDecimal getMinValue();
	
	public default boolean hasMaxValue()
	{
		return getMaxValue() != null;
	}
	
	public BigDecimal getMaxValue();
	
	public static interface Builder
	{
		public Builder displayName(Text displayName);
		
		public Builder pluralDisplayName(Text pluralDisplayName);
		
		public Builder defaultFractionDigits(int digits);
		
		public Builder symbol(Text symbol);
		
		public Builder formatter(Formatter formatter);
		
		public Builder name(String name);
		
		public Builder id(String name);

		public Builder defaultValue(BigDecimal value);
		
		public Builder minValue(BigDecimal min);
		
		public Builder maxValue(BigDecimal max);
		
		public EnhancedCurrency build();
		
		public static interface Formatter
		{
			public Text format(EnhancedCurrency currency, BigDecimal amount, int numFractionDigits);
			
			public Formatter FROM_NAME = (currency, amount, numFractionDigits) ->
				FormattedCurrency.format(amount, numFractionDigits, currency.getDisplayName(), currency.getPluralDisplayName());
				
			public Formatter FROM_SYMBOL = (currency, amount, numFractionDigits) ->
			FormattedCurrency.format(amount, numFractionDigits, currency.getSymbol(), currency.getSymbol());
		}
	}
}
