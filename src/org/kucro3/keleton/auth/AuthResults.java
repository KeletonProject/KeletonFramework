package org.kucro3.keleton.auth;

import org.kucro3.keleton.Since;

/**
 * 提供常用登陆结果的常量
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface AuthResults {
	/**
	 * 登录请求通过
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult PASSED = AuthResult.builder().passed(true).build();
	
	/**
	 * 密码错误
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult WRONG_PASSWORD = AuthResult.builder().passed(false).message("Wrong password").build();
	
	/**
	 * 尚未注册
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult NOT_REGISTERED = AuthResult.builder().passed(false).message("Not registered").build();
	
	/**
	 * 已经登陆
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult ALREADY_LOGGED_IN = AuthResult.builder().passed(false).message("Already logged in").build();
	
	/**
	 * 已经注册
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult ALREADY_REGISTERED = AuthResult.builder().passed(false).message("Already registered").build();
	
	/**
	 * 尚未登陆
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult NOT_LOGGED_IN = AuthResult.builder().passed(false).message("Not logged in").build();
	
	/**
	 * 登陆请求被取消
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult CANCELLED = AuthResult.builder().passed(false).message("Cancelled").build();
}