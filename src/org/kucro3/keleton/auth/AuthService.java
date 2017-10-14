package org.kucro3.keleton.auth;

import java.util.UUID;

import org.kucro3.keleton.Since;
import org.kucro3.keleton.UniqueService;
import org.spongepowered.api.event.cause.Cause;

/**
 * 登陆服务
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface AuthService extends Transient, UniqueService {
	/**
	 * 获得登陆会话的对象池
	 * @return
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthTokenPool getTokenPool() throws AuthException;
	
	/**
	 * 查询是否存在指定UUID的在线会话
	 * @param uuid 需要查询的会话的UUID
	 * @param cause 查询原因
	 * @return 是否存在指定UUID的在线会话
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public boolean isOnline(UUID uuid, Cause cause) throws AuthException;
	
	/**
	 * 查询该UUID是否已经注册
	 * @param uuid 需要查询的UUID
	 * @param cause 查询原因
	 * @return 该UUID是否已经注册
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public boolean isRegistered(UUID uuid, Cause cause) throws AuthException;
	
	/**
	 * 注册（如果该UUID还没有注册）
	 * @param uuid 需要注册的UUID
	 * @param password 注册密码（已使用{@link AuthUtil#toMD5(String) MD5}）
	 * @param cause 注册原因
	 * @return 
	 * 	<dl>
	 * 		<dt>操作结果。</dt>
	 * 		<dd>如果该UUID已经注册，则会返回{@link AuthResults#ALREADY_REGISTERED ALREADY_REGISTERED}。</dd>
	 * 		<dd>如果注册的事务被取消，则会返回{@link AuthResults#CANCELLED CANCELLED}。</dd>
	 * 		<dd>如果成功注册，则会返回{@link AuthResults#PASSED PASSED}。</dd>
	 * 	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult register(UUID uuid, String password, Cause cause) throws AuthException;
	
	/**
	 * 登陆，登陆成功之后会创建一个在线会话（如果该UUID已经注册）
	 * @param uuid 需要登录并创建会话的UUID
	 * @param password 密码（已使用{@link AuthUtil#toMD5(String) MD5}）
	 * @param cause 登陆原因
	 * @return 
	 * 	<dl>
	 * 		<dt>操作结果。</dt>
	 * 		<dd>如果查询的UUID没有注册，则会返回{@link AuthResults#NOT_REGISTERED NOT_REGISTERED}。</dd>
	 * 		<dd>如果该UUID已经存在在线（即已经登陆）会话，则会返回{@link AuthResults#ALREADY_LOGGED_IN ALREADY_LOGGED_IN}。</dd>
	 * 		<dd>如果密码错误，则会返回{@link AuthResults#WRONG_PASSWORD WRONG_PASSWORD}。</dd>
	 * 		<dd>如果登陆的事务被取消，则会返回{@link AuthResults#CANCELLED CANCELLED}。</dd>
	 * 		<dd>如果成功登陆，则会返回{@link AuthResults#PASSED PASSED}。</dd>
	 * 	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult login(UUID uuid, String password, Cause cause) throws AuthException;
	
	/**
	 * 登出该UUID的在线会话（如果存在该在线会话）
	 * @param uuid 需要登出的会话的UUID
	 * @param cause 登出原因
	 * @return 
	 * 	<dl>
	 * 		<dt>操作结果。</dt>
	 *		<dd>如果查询的UUID没有注册，则会返回{@link AuthResults#NOT_REGISTERED NOT_REGISTERED}。</dd>
	 * 		<dd>如果该UUID不存在在线会话，则会返回{@link AuthResults#NOT_LOGGED_IN NOT_LOGGED_IN}。</dd>
	 * 		<dd>如果登出的事务被取消，则会返回{@link AuthResults#CANCELLED CANCELLED}。</dd>
	 *		<dd>如果成功登出，则会返回{@link AuthResults#PASSED PASSED}。</dd>
	 *	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult logout(UUID uuid, Cause cause) throws AuthException;
	
	/**
	 * 修改该UUID的密码，并且为了保证安全，必须提供旧密码（如果该UUID已经注册）
	 * @param uuid 需要修改密码的UUID
	 * @param oldpassword 旧密码（已使用{@link AuthUtil#toMD5(String) MD5}）
	 * @param newpassword 新密码（已使用{@link AuthUtil#toMD5(String) MD5}）
	 * @param cause 修改原因。
	 * @return
	 * 	<dl> 
	 * 		<dt>操作结果。</dt>
	 * 		<dd>如果查询的UUID没有注册，则会返回{@link AuthResults#NOT_REGISTERED NOT_REGISTERED}。</dd>
	 * 		<dd>如果旧密码错误，则会返回{@link AuthResults#WRONG_PASSWORD WRONG_PASSWORD}。</dd>
	 * 		<dd>如果修改密码的事务被取消，则会返回{@link AuthResults#CANCELLED CANCELLED}。</dd>
	 * 		<dd>如果旧密码正确，并且成功修改了密码，则会返回{@link AuthResults#PASSED PASSED}。</dd>
	 * 	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult changePassword(UUID uuid, String oldpassword, String newpassword, Cause cause) throws AuthException;
	
	/**
	 * 验证对于该UUID的密码是否正确（如果该UUID已经注册）
	 * @param uuid 需要验证密码的UUID
	 * @param password 密码（已使用{@link AuthUtil#toMD5(String) MD5}）
	 * @param cause 查询原因
	 * @return 
	 * 	<dl>
	 * 		<dt>查询结果。</dt>
	 * 		<dd>如果查询的UUID没有注册，则会返回{@link AuthResults#NOT_REGISTERED NOT_REGISTERED}。</dd>
	 * 		<dd>如果验证密码的事务被取消，则会返回{@link AuthResults#CANCELLED CANCELLED}。</dd>
	 * 		<dd>如果密码正确，则会返回{@link AuthResults#PASSED PASSED}。</dd>
	 * 	</dl>
	 * @throws AuthException
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public AuthResult checkPassword(UUID uuid, String password, Cause cause) throws AuthException;
}