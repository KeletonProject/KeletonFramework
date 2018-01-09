package org.kucro3.keleton;

import org.kucro3.keleton.api.APIContainer;
import org.kucro3.keleton.api.ImportAPI;
import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.KeletonModuleManager;
import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;

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
	public static native int getMajorVersion();
	
	/**
	 * 获取Keleton框架的次版本号
	 * @return 次版本号
	 */
	@ImportAPI(namespace = "kernel", name = "GetMinorVersion")
	public static native int getMinorVersion();

	@ImportAPI(namespace = "kernel", name = "GetModuleManager")
	public static native KeletonModuleManager getModuleManager();

	@ImportAPI(namespace = "kernel", name = "GetKernelFenceEstablisher")
	public static native KeletonModule.FenceEstablisher getKeletonEstablisher();

	@ImportAPI(namespace = "kernel", name = "GetKernelKlinkInstance")
	public static native Klink getKlink();

	@ImportAPI(namespace = "kernel", name = "GetKernelKlinkEnvironment")
	public static native Environment getKlinkEnvironment();
}