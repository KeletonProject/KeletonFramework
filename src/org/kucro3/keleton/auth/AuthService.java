package org.kucro3.keleton.auth;

import java.util.UUID;

import org.kucro3.keleton.Since;
import org.kucro3.keleton.UniqueService;
import org.spongepowered.api.event.cause.Cause;

/**
 * ��½����
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface AuthService extends Transient, UniqueService {
	/**
	 * ��õ�½�Ự�Ķ����
	 * @return
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthTokenPool getTokenPool() throws AuthException;
	
	/**
	 * ��ѯ�Ƿ����ָ��UUID�����߻Ự
	 * @param uuid ��Ҫ��ѯ�ĻỰ��UUID
	 * @param cause ��ѯԭ��
	 * @return �Ƿ����ָ��UUID�����߻Ự
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public boolean isOnline(UUID uuid, Cause cause) throws AuthException;
	
	/**
	 * ��ѯ��UUID�Ƿ��Ѿ�ע��
	 * @param uuid ��Ҫ��ѯ��UUID
	 * @param cause ��ѯԭ��
	 * @return ��UUID�Ƿ��Ѿ�ע��
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public boolean isRegistered(UUID uuid, Cause cause) throws AuthException;
	
	/**
	 * ע�ᣨ�����UUID��û��ע�ᣩ
	 * @param uuid ��Ҫע���UUID
	 * @param password ע�����루��ʹ��{@link AuthUtil#toMD5(String) MD5}��
	 * @param cause ע��ԭ��
	 * @return 
	 * 	<dl>
	 * 		<dt>���������</dt>
	 * 		<dd>�����UUID�Ѿ�ע�ᣬ��᷵��{@link AuthResults#ALREADY_REGISTERED ALREADY_REGISTERED}��</dd>
	 * 		<dd>���ע�������ȡ������᷵��{@link AuthResults#CANCELLED CANCELLED}��</dd>
	 * 		<dd>����ɹ�ע�ᣬ��᷵��{@link AuthResults#PASSED PASSED}��</dd>
	 * 	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult register(UUID uuid, String password, Cause cause) throws AuthException;
	
	/**
	 * ��½����½�ɹ�֮��ᴴ��һ�����߻Ự�������UUID�Ѿ�ע�ᣩ
	 * @param uuid ��Ҫ��¼�������Ự��UUID
	 * @param password ���루��ʹ��{@link AuthUtil#toMD5(String) MD5}��
	 * @param cause ��½ԭ��
	 * @return 
	 * 	<dl>
	 * 		<dt>���������</dt>
	 * 		<dd>�����ѯ��UUIDû��ע�ᣬ��᷵��{@link AuthResults#NOT_REGISTERED NOT_REGISTERED}��</dd>
	 * 		<dd>�����UUID�Ѿ��������ߣ����Ѿ���½���Ự����᷵��{@link AuthResults#ALREADY_LOGGED_IN ALREADY_LOGGED_IN}��</dd>
	 * 		<dd>������������᷵��{@link AuthResults#WRONG_PASSWORD WRONG_PASSWORD}��</dd>
	 * 		<dd>�����½������ȡ������᷵��{@link AuthResults#CANCELLED CANCELLED}��</dd>
	 * 		<dd>����ɹ���½����᷵��{@link AuthResults#PASSED PASSED}��</dd>
	 * 	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult login(UUID uuid, String password, Cause cause) throws AuthException;
	
	/**
	 * �ǳ���UUID�����߻Ự��������ڸ����߻Ự��
	 * @param uuid ��Ҫ�ǳ��ĻỰ��UUID
	 * @param cause �ǳ�ԭ��
	 * @return 
	 * 	<dl>
	 * 		<dt>���������</dt>
	 *		<dd>�����ѯ��UUIDû��ע�ᣬ��᷵��{@link AuthResults#NOT_REGISTERED NOT_REGISTERED}��</dd>
	 * 		<dd>�����UUID���������߻Ự����᷵��{@link AuthResults#NOT_LOGGED_IN NOT_LOGGED_IN}��</dd>
	 * 		<dd>����ǳ�������ȡ������᷵��{@link AuthResults#CANCELLED CANCELLED}��</dd>
	 *		<dd>����ɹ��ǳ�����᷵��{@link AuthResults#PASSED PASSED}��</dd>
	 *	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult logout(UUID uuid, Cause cause) throws AuthException;
	
	/**
	 * �޸ĸ�UUID�����룬����Ϊ�˱�֤��ȫ�������ṩ�����루�����UUID�Ѿ�ע�ᣩ
	 * @param uuid ��Ҫ�޸������UUID
	 * @param oldpassword �����루��ʹ��{@link AuthUtil#toMD5(String) MD5}��
	 * @param newpassword �����루��ʹ��{@link AuthUtil#toMD5(String) MD5}��
	 * @param cause �޸�ԭ��
	 * @return
	 * 	<dl> 
	 * 		<dt>���������</dt>
	 * 		<dd>�����ѯ��UUIDû��ע�ᣬ��᷵��{@link AuthResults#NOT_REGISTERED NOT_REGISTERED}��</dd>
	 * 		<dd>��������������᷵��{@link AuthResults#WRONG_PASSWORD WRONG_PASSWORD}��</dd>
	 * 		<dd>����޸����������ȡ������᷵��{@link AuthResults#CANCELLED CANCELLED}��</dd>
	 * 		<dd>�����������ȷ�����ҳɹ��޸������룬��᷵��{@link AuthResults#PASSED PASSED}��</dd>
	 * 	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult changePassword(UUID uuid, String oldpassword, String newpassword, Cause cause) throws AuthException;
	
	/**
	 * ��֤���ڸ�UUID�������Ƿ���ȷ�������UUID�Ѿ�ע�ᣩ
	 * @param uuid ��Ҫ��֤�����UUID
	 * @param password ���루��ʹ��{@link AuthUtil#toMD5(String) MD5}��
	 * @param cause ��ѯԭ��
	 * @return 
	 * 	<dl>
	 * 		<dt>��ѯ�����</dt>
	 * 		<dd>�����ѯ��UUIDû��ע�ᣬ��᷵��{@link AuthResults#NOT_REGISTERED NOT_REGISTERED}��</dd>
	 * 		<dd>�����֤���������ȡ������᷵��{@link AuthResults#CANCELLED CANCELLED}��</dd>
	 * 		<dd>���������ȷ����᷵��{@link AuthResults#PASSED PASSED}��</dd>
	 * 	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult checkPassword(UUID uuid, String password, Cause cause) throws AuthException;
}