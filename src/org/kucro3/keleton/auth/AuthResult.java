package org.kucro3.keleton.auth;

import java.util.Optional;

import javax.annotation.Nullable;

import org.kucro3.keleton.Since;

/**
 * 登陆结果
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public class AuthResult {
	AuthResult(String message, boolean passed)
	{
		this.message = message;
		this.passed = passed;
	}
	
	/**
	 * 构造一个登陆结果的Builder
	 * @return
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public static Builder builder()
	{
		return new Builder();
	}
	
	/**
	 * 获取该登陆结果包含的默认信息
	 * @return 默认信息
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public Optional<String> getMessage()
	{
		return Optional.ofNullable(message);
	}
	
	/**
	 * 是否通过登陆请求
	 * @return 是否通过登录请求
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public boolean isPassed()
	{
		return passed;
	}
	
	String message;
	
	boolean passed;
	
	/**
	 * 登陆结果的Builder
	 * @author Kumonda221
	 *
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public static class Builder
	{
		Builder()
		{
		}
		
		/**
		 * 构造一个登陆结果
		 * @return 已构造完毕的登陆结果
		 */
		@Since(majorVersion = 1, minorVersion = 0)
		public AuthResult build()
		{
			return new AuthResult(message, passed);
		}
		
		/**
		 * 设置登陆结果的默认信息
		 * @param message 默认信息
		 * @return this
		 */
		@Since(majorVersion = 1, minorVersion = 0)
		public Builder message(@Nullable String message)
		{
			this.message = message;
			return this;
		}
		
		/**
		 * 设置登录请求是否通过
		 * @param passed 是否通过登录请求
		 * @return this
		 */
		@Since(majorVersion = 1, minorVersion = 0)
		public Builder passed(boolean passed)
		{
			this.passed = passed;
			return this;
		}
		
		String message;
		
		boolean passed;
	}
}