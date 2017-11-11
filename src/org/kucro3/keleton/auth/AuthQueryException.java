package org.kucro3.keleton.auth;

import org.kucro3.annotation.Since;

/**
 * ��½�����ڴ����½����ʱ�򴥷���ѯ��������׳����쳣
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