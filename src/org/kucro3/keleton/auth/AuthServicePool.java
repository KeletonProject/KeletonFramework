package org.kucro3.keleton.auth;

import java.util.Optional;

import org.kucro3.annotation.Since;

/**
 * 登陆服务对象池
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface AuthServicePool {	
	/**
	 * 根据ID获得登陆服务对象
	 * @param id 服务对象ID
	 * @return 
	 * 	<dl>
	 * 		<dt>服务对象</dt>
	 * 		<dd>如果对象池中已构造了该登陆服务对象，则返回该对象</dd>
	 * 		<dd>如果对象池中没有构造登陆服务对象，则返回{@code null}</dd>
	 * 	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthService get(String id) throws AuthException;
	
	/**
	 * 尝试根据ID获得登陆服务对象
	 * @see #get(String)
	 * @param id 服务对象ID
	 * @return 用{@code Optional}包装的返回值
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public default Optional<AuthService> tryGet(String id) throws AuthException
	{
		return Optional.ofNullable(get(id));
	}

	/**
	 * 检查对象池中是否存在指定ID的登陆服务对象
	 * @param id 服务对象ID
	 * @return 若池中有该登陆服务对象，则返回{@code true}，反则返回{@code false}
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public boolean available(String id) throws AuthException;

	/**
	 * 析构指定ID的登陆服务对象（只是将该对象从池中删除并且设置状态为不可用，与内存回收没有关联）
	 * @param id 服务对象ID
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public void destruct(String id) throws AuthException;

	/**
	 * 构造指定ID的登陆服务对象
	 * @param id 服务对象ID
	 * @param args 可选构造参数
	 * @return 已构造的服务对象
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthService construct(String id, Object... args) throws AuthException;

	/**
	 * 如果对象池中不存在指定ID的登陆服务对象，则构造对象
	 * @param id 服务对象ID
	 * @param args 可选构造参数
	 * @return 已存在的或已构造的服务对象
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public default AuthService constructIfAbsent(String id, Object... args) throws AuthException
	{
		Optional<AuthService> service;
		return ((service = tryGet(id)).isPresent()) ? service.get() : construct(id, args);
	}
}