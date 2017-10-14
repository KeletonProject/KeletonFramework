package org.kucro3.keleton.impl.config.klink;

import org.kucro3.keleton.impl.config.ImplementationInstance;

public class API {
	public static <T> void bindCustomType(CustomType<T> custom)
	{
		ImplementationInstance._API_bindCustom(custom);
	}
}
