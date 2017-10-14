package org.kucro3.keleton.i18n;

public interface LocaleProperties {
	public String by(String id);
	
	public default String by(String id, Object... vals)
	{
		return String.format(by(id), vals);
	}
	
	public boolean contains(String id);
	
	public static interface Empty extends LocaleProperties
	{
		@Override
		public default String by(String id)
		{
			return "#" + id;
		}
		
		@Override
		public default boolean contains(String id)
		{
			return false;
		}
	}
}