package org.kucro3.keleton.auth;

import org.kucro3.annotation.Since;
import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.KeyToken;

/**
 * 登陆服务相关的键及句柄
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface AuthKeys {
	/**
	 * 用于获取登陆服务实例的键
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public static final Key<AuthServicePool> SERVICE_POOL = Key.of("AuthServicePool", AuthServicePool.class, AuthServicePool.class);
	
	/**
	 * 用于从默认对象池中获取登录服务实例的句柄
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public static final KeyToken<AuthServicePool> SERVICE_POOL_TOKEN = KeyToken.of(SERVICE_POOL);
}