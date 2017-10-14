package org.kucro3.keleton.service.economy;

import java.math.BigDecimal;
import java.util.Objects;

import org.kucro3.keleton.economy.EnhancedCurrency;
import org.kucro3.keleton.economy.EnhancedCurrency.Builder;
import org.spongepowered.api.text.Text;

public class CurrencyBuilderImpl implements EnhancedCurrency.Builder {
	CurrencyBuilderImpl(EconomyServiceImpl owner)
	{
		this.owner = owner;
	}
	
	@Override
	public EnhancedCurrency build() 
	{
		return new CurrencyImpl(owner, id(), name(), symbol(), displayName(), pluralDisplayName(), defaultFractionDigits(), formatter(),
				defaultValue(), maxValue(), minValue());
	}

	@Override
	public Builder defaultFractionDigits(int defaultFractionDigits) 
	{
		this.defaultFractionDigits = defaultFractionDigits;
		return this;
	}

	@Override
	public Builder displayName(Text displayName)
	{
		this.displayName = displayName;
		return this;
	}

	@Override
	public Builder formatter(Formatter formatter)
	{
		this.formatter = formatter;
		return this;
	}

	@Override
	public Builder pluralDisplayName(Text pluralDisplayName) 
	{
		this.pluralDisplayName = pluralDisplayName;
		return this;
	}

	@Override
	public Builder symbol(Text symbol)
	{
		this.symbol = symbol;
		return this;
	}

	@Override
	public Builder id(String id)
	{
		this.id = id;
		return this;
	}

	@Override
	public Builder name(String name) 
	{
		this.name = name;
		return this;
	}
	
	@Override
	public Builder defaultValue(BigDecimal value) 
	{
		this.defaultValue = value;
		return this;
	}
	
	String name()
	{
		return Objects.requireNonNull(this.name);
	}
	
	String id()
	{
		return Objects.requireNonNull(this.id);
	}
	
	Text symbol()
	{
		return Objects.requireNonNull(this.symbol);
	}
	
	Text displayName()
	{
		return this.displayName == null ? symbol() : this.displayName;
	}
	
	Text pluralDisplayName()
	{
		return this.pluralDisplayName == null ? displayName() : this.pluralDisplayName;
	}
	
	Formatter formatter()
	{
		return Objects.requireNonNull(this.formatter);
	}
	
	int defaultFractionDigits()
	{
		return this.defaultFractionDigits;
	}
	
	BigDecimal defaultValue()
	{
		return Objects.requireNonNull(this.defaultValue);
	}
	
	BigDecimal maxValue()
	{
		if(maxValue != null && minValue != null)
			if(maxValue.compareTo(minValue) < 0)
				throw new IllegalStateException("Max Value must be bigger than Min Value");
		return maxValue;
	}
	
	BigDecimal minValue()
	{
		if(minValue != null && maxValue != null)
			if(minValue.compareTo(maxValue) > 0)
				throw new IllegalStateException("Min Value must be less than Max Value");
		return minValue;
	}
	
	@Override
	public Builder maxValue(BigDecimal max) 
	{
		this.maxValue = max;
		return this;
	}

	@Override
	public Builder minValue(BigDecimal min)
	{
		this.minValue = min;
		return this;
	}
	
	final EconomyServiceImpl owner;
	
	String name;
	
	String id;
	
	Text symbol;
	
	Text pluralDisplayName;
	
	Text displayName;
	
	Formatter formatter;
	
	BigDecimal defaultValue = BigDecimal.ZERO;
	
	BigDecimal maxValue;
	
	BigDecimal minValue;
	
	int defaultFractionDigits = 2;
}
