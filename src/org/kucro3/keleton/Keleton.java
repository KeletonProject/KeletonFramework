package org.kucro3.keleton;

import org.kucro3.keleton.module.KeletonModuleManager;

/**
 * �ɴ˻�ȡһЩ����Keleton��ܵĻ�����Ϣ
 * 
 * @author Kumonda221
 *
 */
public class Keleton {
	/**
	 * ��ȡKeleton��ܵ����汾��
	 * @return ���汾��
	 */
	public static int getMajorVersion()
	{
		return 1;
	}
	
	/**
	 * ��ȡKeleton��ܵĴΰ汾��
	 * @return �ΰ汾��
	 */
	public static int getMinorVersion()
	{
		return 0;
	}

	public static KeletonModuleManager getModuleManager()
	{
		return manager;
	}

	static KeletonModuleManager manager;
}