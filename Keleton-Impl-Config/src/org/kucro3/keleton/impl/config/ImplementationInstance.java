package org.kucro3.keleton.impl.config;

import org.kucro3.keleton.config.ConfigurationKeys;
import org.kucro3.keleton.config.ConfigurationService;
import org.kucro3.keleton.impl.config.klink.CustomType;
import org.kucro3.keleton.keyring.ObjectService;
import org.slf4j.Logger;

public class ImplementationInstance {
	public static void initialize()
	{
		// clinit
	}
	
	static void _Init_setLogger(Logger logger)
	{
		ImplementationInstance.logger = logger;
	}
	
	public static ConfigurationService _API_getService()
	{
		return SERVICE;
	}
	
	public static <T> void _API_bindCustom(CustomType<T> t)
	{
		SERVICE.operatorKlink.bindCustom(t);
	}
	
	public static Logger getLogger()
	{
		return logger;
	}
	
	private static Logger logger;
	
	private static final ConfigurationServiceImpl SERVICE;
	
	static {
		ObjectService.put(ConfigurationKeys.SERVICE, SERVICE = new ConfigurationServiceImpl());
	}
}
