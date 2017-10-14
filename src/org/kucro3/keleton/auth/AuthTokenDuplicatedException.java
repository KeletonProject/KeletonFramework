package org.kucro3.keleton.auth;

public class AuthTokenDuplicatedException extends AuthException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6689008625813020461L;
	
	public AuthTokenDuplicatedException()
	{
	}
	
	public AuthTokenDuplicatedException(String msg)
	{
		super(msg);
	}
}