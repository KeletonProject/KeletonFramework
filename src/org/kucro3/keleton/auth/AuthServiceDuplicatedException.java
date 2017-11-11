package org.kucro3.keleton.auth;

import org.kucro3.annotation.Since;

/**
 * ����ͼ����һ��ID�Ѿ����ڵĵ�½�������ʱ�׳��Ĵ���
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