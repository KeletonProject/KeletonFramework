package org.kucro3.keleton.auth;

import org.kucro3.keleton.Since;

/**
 * �ɱ������Ķ���
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface Transient {
	/**
	 * �����Ƿ���Ȼ����
	 * @return �Ƿ����
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public boolean isVaild();
}