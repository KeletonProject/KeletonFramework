package org.kucro3.keleton.auth;

import java.util.UUID;

import org.spongepowered.api.event.cause.Cause;

/**
 * ��½�Ự���󣬰����˻Ự��һЩ��Ϣ���˶����Ǳ�����ģ����Դ��ڵ�½���󲢲��ȼ������ߣ��Ƿ����������{@link AuthToken#isOnline() isOnline}��
 * @author Kumonda221
 */
public interface AuthToken extends Transient {
	/**
	 * �Ự�Ƿ�����
	 * @return ���Ự���ߣ�����{@code true}�����򷵻�{@code false}
	 */
	public boolean isOnline();

	/**
	 * ��һ�ε�¼��ʱ��
	 * @return ����Ϊ��λ��ʱ���
	 */
	public long firstLogin();

	/**
	 * ���һ�ε�½��ʱ��
	 * @return ����Ϊ��λ��ʱ���
	 */
	public long lastLogin();

	/**
	 * ��øõ�½�����UUID
	 * @return UUID
	 */
	public UUID getUUID();

	/**
	 * ��øõ�½���������
	 * @return ���루��ʹ��{@link AuthUtil#toMD5(String) MD5}��
	 */
	public String getPassword();
}