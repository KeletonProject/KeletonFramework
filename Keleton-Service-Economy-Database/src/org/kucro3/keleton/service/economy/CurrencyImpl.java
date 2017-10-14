package org.kucro3.keleton.service.economy;

import java.math.BigDecimal;

import org.kucro3.keleton.economy.DefaultValued;
import org.kucro3.keleton.economy.EnhancedCurrency;
import org.kucro3.keleton.economy.EnhancedCurrency.Builder.Formatter;
import org.kucro3.keleton.economy.EnhancedEconomyService;
import org.spongepowered.api.text.Text;

public class CurrencyImpl implements EnhancedCurrency, DefaultValued {
	CurrencyImpl(EconomyServiceImpl owner, String id, String name,
			Text symbol, Text displayName, Text pluralDisplayName, int defaultFractionDigits, Formatter formatter,
			BigDecimal defaultValue, BigDecimal maxValue, BigDecimal minValue)
	{
		this.owner = owner;
		this.id = id;
		this.name = name;
		this.symbol = symbol;
		this.displayName = displayName;
		this.pluralDisplayName = pluralDisplayName;
		this.defaultFractionDigits = defaultFractionDigits;
		this.formatter = formatter;
		this.defaultValue = defaultValue;
		this.maxValue = maxValue;
		this.minValue = minValue;
	}
	
	@Override
	public Text format(BigDecimal amount, int numFractionDigits)
	{
		return formatter.format(this, amount, numFractionDigits);
	}

	@Override
	public int getDefaultFractionDigits() 
	{
		return defaultFractionDigits;
	}

	@Override
	public Text getDisplayName() 
	{
		return displayName;
	}

	@Override
	public Text getPluralDisplayName() 
	{
		return pluralDisplayName;
	}

	@Override
	public Text getSymbol()
	{
		return symbol;
	}

	@Override
	public boolean isDefault() 
	{
		return isDefault;
	}

	@Override
	public String getId() 
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public EnhancedEconomyService getOwner() 
	{
		return owner;
	}
	
	void setDefault(boolean isDefault)
	{
		this.isDefault = isDefault;
	}
	
	@Override
	public BigDecimal getDefaultValue() 
	{
		return defaultValue;
	}
	
	@Override
	public BigDecimal getMaxValue()
	{
		return maxValue;
	}

	@Override
	public BigDecimal getMinValue() 
	{
		return minValue;
	}

	final Formatter formatter;
	
	final EconomyServiceImpl owner;
	
	final String name;
	
	final String id;
	
	final Text symbol;
	
	final Text displayName;
	
	final Text pluralDisplayName;
	
	final int defaultFractionDigits;
	
	final BigDecimal defaultValue;
	
	boolean isDefault;

	final BigDecimal maxValue;
	
	final BigDecimal minValue;
}
