package org.kucro3.keleton;

import org.kucro3.keleton.implementation.loader.KeletonModuleContainer;

/**
 * 由此获取一些关于Keleton框架的基本信息
 * 
 * @author Kumonda221
 *
 */
public class Keleton {
	/**
	 * 获取Keleton框架的主版本号
	 * @return 主版本号
	 */
	public static int getMajorVersion()
	{
		return 1;
	}
	
	/**
	 * 获取Keleton框架的次版本号
	 * @return 次版本号
	 */
	public static int getMinorVersion()
	{
		return 0;
	}

	public static KeletonModuleContainer getModuleContainer()
	{
		return container;
	}

	private static final KeletonModuleContainer container = new KeletonModuleContainer();
}