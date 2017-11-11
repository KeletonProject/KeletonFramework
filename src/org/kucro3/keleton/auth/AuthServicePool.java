package org.kucro3.keleton.auth;

import java.util.Optional;

import org.kucro3.annotation.Since;

/**
 * ��½��������
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface AuthServicePool {	
	/**
	 * ����ID��õ�½�������
	 * @param id �������ID
	 * @return 
	 * 	<dl>
	 * 		<dt>�������</dt>
	 * 		<dd>�����������ѹ����˸õ�½��������򷵻ظö���</dd>
	 * 		<dd>����������û�й����½��������򷵻�{@code null}</dd>
	 * 	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthService get(String id) throws AuthException;
	
	/**
	 * ���Ը���ID��õ�½�������
	 * @see #get(String)
	 * @param id �������ID
	 * @return ��{@code Optional}��װ�ķ���ֵ
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public default Optional<AuthService> tryGet(String id) throws AuthException
	{
		return Optional.ofNullable(get(id));
	}

	/**
	 * ����������Ƿ����ָ��ID�ĵ�½�������
	 * @param id �������ID
	 * @return �������иõ�½��������򷵻�{@code true}�����򷵻�{@code false}
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public boolean available(String id) throws AuthException;

	/**
	 * ����ָ��ID�ĵ�½�������ֻ�ǽ��ö���ӳ���ɾ����������״̬Ϊ�����ã����ڴ����û�й�����
	 * @param id �������ID
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public void destruct(String id) throws AuthException;

	/**
	 * ����ָ��ID�ĵ�½�������
	 * @param id �������ID
	 * @param args ��ѡ�������
	 * @return �ѹ���ķ������
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthService construct(String id, Object... args) throws AuthException;

	/**
	 * ���������в�����ָ��ID�ĵ�½��������������
	 * @param id �������ID
	 * @param args ��ѡ�������
	 * @return �Ѵ��ڵĻ��ѹ���ķ������
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public default AuthService constructIfAbsent(String id, Object... args) throws AuthException
	{
		Optional<AuthService> service;
		return ((service = tryGet(id)).isPresent()) ? service.get() : construct(id, args);
	}
}