package org.kucro3.keleton.auth;

/**
 * �����ڲ����ķ��񲻿ɴ��ö��󲻿���ʱ���׳����쳣
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