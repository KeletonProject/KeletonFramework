package org.kucro3.keleton.auth;

import org.kucro3.annotation.Since;

/**
 * ��½ϵͳ�����½����ʱ�׳����쳣
 * @author Kumonda221
 * 
 */
@Since(majorVersion = 1, minorVersion = 0)
public class AuthException extends Exception {

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