package org.kucro3.keleton.auth;

import org.kucro3.annotation.Since;
import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.KeyToken;

/**
 * ��½������صļ������
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface AuthKeys {
	/**
	 * ���ڻ�ȡ��½����ʵ���ļ�
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public static final Key<AuthServicePool> SERVICE_POOL = Key.of("AuthServicePool", AuthServicePool.class, AuthServicePool.class);
	
	/**
	 * ���ڴ�Ĭ�϶�����л�ȡ��¼����ʵ���ľ��
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public static final KeyToken<AuthServicePool> SERVICE_POOL_TOKEN = KeyToken.of(SERVICE_POOL);
}