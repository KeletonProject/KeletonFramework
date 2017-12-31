package org.kucro3.keleton.auth;

import org.kucro3.annotation.Since;
import org.kucro3.keleton.exception.KeletonException;

/**
 * 登陆系统处理登陆事务时抛出的异常
 * @author Kumonda221
 * 
 */
@Since(majorVersion = 1, minorVersion = 0)
public class AuthException extends KeletonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5461095478934416760L;

	@Since(majorVersion = 1, minorVersion = 0)
	public AuthException()
	{
	}
	
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthException(String message)
	{
		super(message);
	}
	
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthException(Throwable e)
	{
		super(e);
	}
	
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthException(String message, Throwable cause)
	{
		super(message, cause);
	}
}