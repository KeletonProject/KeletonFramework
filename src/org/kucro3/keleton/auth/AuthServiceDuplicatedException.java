package org.kucro3.keleton.auth;

import org.kucro3.annotation.Since;

/**
 * 当试图创建一个ID已经存在的登陆服务对象时抛出的错误
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public class AuthServiceDuplicatedException extends AuthException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8159059596923314633L;

	@Since(majorVersion = 1, minorVersion = 0)
	public AuthServiceDuplicatedException()
	{
	}
	
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthServiceDuplicatedException(String msg)
	{
		super(msg);
	}
}