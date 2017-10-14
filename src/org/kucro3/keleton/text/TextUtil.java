package org.kucro3.keleton.text;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;

public class TextUtil {
	public static Text fromColored(String str)
	{
		Text.Builder builder = Text.builder();
		TextColor currentColor = null, tempColor;
		TextStyle currentStyle = null, tempStyle;
		int lastIndex = 0;
		
		char c;
		for(int i = 0; i < str.length(); i++)
			switch(c = str.charAt(i))
			{
			case '&':
				if((++i) < str.length())
				{
					c = str.charAt(i);
					tempColor = COLORS.get(c);
					if(tempColor != null)
					{
						builder.append(Text.builder(str.substring(lastIndex, i - 1))
								.color(currentColor == null ? TextColors.RESET : currentColor)
								.style(currentStyle == null ? TextStyles.RESET : currentStyle)
								.build());
						currentColor = tempColor;
						lastIndex = i + 1;
					}
					else
					{
						tempStyle = STYLES.get(c);
						if(tempStyle != null)
						{
							builder.append(Text.builder(str.substring(lastIndex, i - 1))
									.color(currentColor == null ? TextColors.RESET : currentColor)
									.style(currentStyle == null ? TextStyles.RESET : currentStyle)
									.build());
							if(tempStyle.equals(TextStyles.RESET))
								currentStyle = null;
							lastIndex = i + 1;
						}
						else
							continue;
					}
				}
				break;
			default:
				continue;
			}
		
		builder.append(Text.builder(str.substring(lastIndex, str.length()))
				.color(currentColor == null ? TextColors.RESET : currentColor)
				.style(currentStyle == null ? TextStyles.RESET : currentStyle)
				.build());
		
		return builder.build();
	}
	
	private static final Map<Character, TextColor> COLORS = new HashMap<Character, TextColor>()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1111455024238892910L;
		
			{
				put('4', TextColors.DARK_RED);
				put('c', TextColors.RED);
				put('6', TextColors.YELLOW);
				put('e', TextColors.GOLD);
				put('2', TextColors.DARK_GREEN);
				put('a', TextColors.GREEN);
				put('b', TextColors.AQUA);
				put('3', TextColors.BLUE);
				put('1', TextColors.DARK_BLUE);
				put('9', TextColors.DARK_AQUA);
				put('d', TextColors.LIGHT_PURPLE);
				put('5', TextColors.DARK_PURPLE);
				put('f', TextColors.WHITE);
				put('7', TextColors.GRAY);
				put('8', TextColors.DARK_GRAY);
				put('0', TextColors.BLACK);
			}
		};
		
	private static final Map<Character, TextStyle> STYLES = new HashMap<Character, TextStyle>()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -8154274712481025447L;
		
			{
				put('l', TextStyles.BOLD);
				put('k', TextStyles.OBFUSCATED);
				put('n', TextStyles.UNDERLINE);
				put('m', TextStyles.STRIKETHROUGH);
				put('o', TextStyles.ITALIC);
				put('r', TextStyles.RESET);
			}
		};
}