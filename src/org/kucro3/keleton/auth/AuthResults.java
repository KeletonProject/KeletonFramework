package org.kucro3.keleton.auth;

import org.kucro3.keleton.Since;

/**
 * �ṩ���õ�½����ĳ���
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface AuthResults {
	/**
	 * ��¼����ͨ��
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult PASSED = AuthResult.builder().passed(true).build();
	
	/**
	 * �������
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult WRONG_PASSWORD = AuthResult.builder().passed(false).message("Wrong password").build();
	
	/**
	 * ��δע��
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult NOT_REGISTERED = AuthResult.builder().passed(false).message("Not registered").build();
	
	/**
	 * �Ѿ���½
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult ALREADY_LOGGED_IN = AuthResult.builder().passed(false).message("Already logged in").build();
	
	/**
	 * �Ѿ�ע��
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult ALREADY_REGISTERED = AuthResult.builder().passed(false).message("Already registered").build();
	
	/**
	 * ��δ��½
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult NOT_LOGGED_IN = AuthResult.builder().passed(false).message("Not logged in").build();
	
	/**
	 * ��½����ȡ��
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult CANCELLED = AuthResult.builder().passed(false).message("Cancelled").build();
}