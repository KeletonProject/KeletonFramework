package org.kucro3.keleton.auth;

import java.util.Optional;

import javax.annotation.Nullable;

import org.kucro3.keleton.Since;

/**
 * ��½���
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
	 * ����һ����½�����Builder
	 * @return
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public static Builder builder()
	{
		return new Builder();
	}
	
	/**
	 * ��ȡ�õ�½���������Ĭ����Ϣ
	 * @return Ĭ����Ϣ
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public Optional<String> getMessage()
	{
		return Optional.ofNullable(message);
	}
	
	/**
	 * �Ƿ�ͨ����½����
	 * @return �Ƿ�ͨ����¼����
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public boolean isPassed()
	{
		return passed;
	}
	
	String message;
	
	boolean passed;
	
	/**
	 * ��½�����Builder
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
		 * ����һ����½���
		 * @return �ѹ�����ϵĵ�½���
		 */
		@Since(majorVersion = 1, minorVersion = 0)
		public AuthResult build()
		{
			return new AuthResult(message, passed);
		}
		
		/**
		 * ���õ�½�����Ĭ����Ϣ
		 * @param message Ĭ����Ϣ
		 * @return this
		 */
		@Since(majorVersion = 1, minorVersion = 0)
		public Builder message(@Nullable String message)
		{
			this.message = message;
			return this;
		}
		
		/**
		 * ���õ�¼�����Ƿ�ͨ��
		 * @param passed �Ƿ�ͨ����¼����
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