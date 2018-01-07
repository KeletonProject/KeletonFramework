package org.kucro3.keleton;

import org.kucro3.keleton.api.APIContainer;
import org.kucro3.keleton.api.ImportAPI;
import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.KeletonModuleManager;

/**
 * �ɴ˻�ȡһЩ����Keleton��ܵĻ�����Ϣ
 * 
 * @author Kumonda221
 *
 */
@APIContainer
public class Keleton {
	/**
	 * ��ȡKeleton��ܵ����汾��
	 * @return ���汾��
	 */
	@ImportAPI(namespace = "kernel", name = "GetMajorVersion")
	public static native int getMajorVersion();
	
	/**
	 * ��ȡKeleton��ܵĴΰ汾��
	 * @return �ΰ汾��
	 */
	@ImportAPI(namespace = "kernel", name = "GetMinorVersion")
	public static native int getMinorVersion();

	@ImportAPI(namespace = "kernel", name = "GetModuleManager")
	public static native KeletonModuleManager getModuleManager();

	@ImportAPI(namespace = "kernel", name = "GetKernelFenceEstablisher")
	public static native KeletonModule.FenceEstablisher getKeletonEstablisher();
}