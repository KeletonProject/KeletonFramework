package org.kucro3.keleton.economy;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

public final class FormattedCurrency {
	private FormattedCurrency()
	{
	}
	
	static Text format(BigDecimal amount, int fractionDigits, Text singular, Text plural)
	{
		BigDecimal scaled = amount.setScale(fractionDigits, BigDecimal.ROUND_DOWN);
		boolean isSingular = scaled.compareTo(BigDecimal.ONE) == 0;
		Text symbol = isSingular ? singular : plural;
		return Text.builder()
				.append(Text.of(scaled.toPlainString()))
				.append(Text.of(" "))
				.append(symbol)
			.build();
	}
	
	public static interface DisplayingName extends Currency
	{
		@Override
		public default Text format(BigDecimal amount, int fractionDigits)
		{
			return FormattedCurrency.format(amount, fractionDigits, this.getDisplayName(), this.getPluralDisplayName());
		}
		
		public static interface Enhanced extends DisplayingName, EnhancedCurrency
		{
		}
	}
	
	public static interface DisplayingSymbol extends Currency
	{
		@Override
		public default Text format(BigDecimal amount, int fractionDigits)
		{
			return FormattedCurrency.format(amount, fractionDigits, this.getSymbol(), this.getSymbol());
		}
		
		public static interface Enhanced extends DisplayingSymbol, EnhancedCurrency
		{
		}
	}
}
