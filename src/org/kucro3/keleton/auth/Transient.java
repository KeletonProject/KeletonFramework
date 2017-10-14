package org.kucro3.keleton.auth;

import org.kucro3.keleton.Since;

/**
 * 可被丢弃的对象
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface Transient {
	/**
	 * 对象是否仍然可用
	 * @return 是否可用
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public boolean isVaild();
}