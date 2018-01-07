package org.kucro3.keleton;

import org.kucro3.keleton.api.APIContainer;
import org.kucro3.keleton.api.ImportAPI;
import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.KeletonModuleManager;

/**
 * 由此获取一些关于Keleton框架的基本信息
 * 
 * @author Kumonda221
 *
 */
@APIContainer
public class Keleton {
	/**
	 * 获取Keleton框架的主版本号
	 * @return 主版本号
	 */
	@ImportAPI(namespace = "kernel", name = "GetMajorVersion")
	public static int getMajorVersion()
	{
		return 1;
	}
	
	/**
	 * 获取Keleton框架的次版本号
	 * @return 次版本号
	 */
	@ImportAPI(namespace = "kernel", name = "GetMinorVersion")
	public static int getMinorVersion()
	{
		return 0;
	}

	@ImportAPI(namespace = "kernel", name = "GetModuleManager")
	public static KeletonModuleManager getModuleManager()
	{
		return manager;
	}

	@ImportAPI(namespace = "kernel", name = "GetKernelFenceEstablisher")
	public static KeletonModule.FenceEstablisher getKeletonEstablisher()
	{
		return ESTABLISHER;
	}

	private static final KeletonModule.FenceEstablisher ESTABLISHER = () -> "keletonkernel";

	static KeletonModuleManager manager;
}