package org.kucro3.keleton;

import org.kucro3.annotation.Since;

import java.util.UUID;

/**
 * 
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface UniqueService {
	/**
	 * 获得此服务对象的UUID。
	 * <br />（注意：此UUID不可用于类似于对象持久化的情境，UUID的实现是自由的，可以是随机生成的。
	 * 该UUID的作用仅限于体现每个服务对象的唯一性，且服务必须保证在服务对象被构造之后UUID不会变化，直到该服务对象被垃圾回收器回收）
	 * @return 此登陆服务的UUID
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public UUID getUniqueId();
}
