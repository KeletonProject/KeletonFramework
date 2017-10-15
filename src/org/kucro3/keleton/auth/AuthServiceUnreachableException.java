package org.kucro3.keleton.auth;

/**
 * 当正在操作的服务不可达，或该对象不可用时，抛出此异常
 * @author Kumonda221
 */
public class AuthServiceUnreachableException extends AuthException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9109002902889077583L;
	
	public AuthServiceUnreachableException()
	{
	}
	
	public AuthServiceUnreachableException(String message)
	{
		super(message);
	}
}