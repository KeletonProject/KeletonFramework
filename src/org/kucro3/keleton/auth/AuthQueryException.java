package org.kucro3.keleton.auth;

import org.kucro3.annotation.Since;

/**
 * 登陆服务在处理登陆事务时因触发查询错误而被抛出的异常
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public class AuthQueryException extends AuthException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 812440895774774648L;
	
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthQueryException()
	{
	}
	
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthQueryException(String msg)
	{
		super(msg);
	}
	
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthQueryException(Throwable cause)
	{
		super(cause);
	}
	
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthQueryException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}