package org.kucro3.keleton.auth;

import java.util.UUID;

import org.spongepowered.api.event.cause.Cause;

/**
 * 登陆会话对象，包含了会话的一些信息（此对象是被缓存的，所以存在登陆对象并不等价于在线，是否在线请参阅{@link AuthToken#isOnline() isOnline}）
 * @author Kumonda221
 */
public interface AuthToken extends Transient {
	/**
	 * 会话是否在线
	 * @return 若会话在线，返回{@code true}，否则返回{@code false}
	 */
	public boolean isOnline();

	/**
	 * 第一次登录的时间
	 * @return 毫秒为单位的时间戳
	 */
	public long firstLogin();

	/**
	 * 最后一次登陆的时间
	 * @return 毫秒为单位的时间戳
	 */
	public long lastLogin();

	/**
	 * 获得该登陆对象的UUID
	 * @return UUID
	 */
	public UUID getUUID();

	/**
	 * 获得该登陆对象的密码
	 * @return 密码（已使用{@link AuthUtil#toMD5(String) MD5}）
	 */
	public String getPassword();
}