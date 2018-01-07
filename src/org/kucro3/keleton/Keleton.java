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
	public static int getMajorVersion()
	{
		return 1;
	}
	
	/**
	 * ��ȡKeleton��ܵĴΰ汾��
	 * @return �ΰ汾��
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