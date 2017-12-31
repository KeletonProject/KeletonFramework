package org.kucro3.keleton.config;

import org.kucro3.keleton.exception.KeletonException;

import java.io.IOException;

public class ConfigurationException extends KeletonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8758787743462991101L;
	
	public ConfigurationException()
	{
	}
	
	public ConfigurationException(String msg)
	{
		super(msg);
	}
	
	public ConfigurationException(Throwable cause)
	{
		super(cause);
	}
	
	public ConfigurationException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
	
	public static class SourceNotSupported extends ConfigurationException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -767212532114591698L;

		public SourceNotSupported()
		{
		}
		
		public SourceNotSupported(String msg)
		{
			super(msg);
		}
		
		public SourceNotSupported(Throwable cause)
		{
			super(cause);
		}
		
		public SourceNotSupported(String msg, Throwable cause)
		{
			super(msg, cause);
		}
	}
	
	public static class DestinationNotSupported extends ConfigurationException
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 9024082105068534363L;
		
		public DestinationNotSupported()
		{
		}
		
		public DestinationNotSupported(String msg)
		{
			super(msg);
		}
		
		public DestinationNotSupported(Throwable cause)
		{
			super(cause);
		}
		
		public DestinationNotSupported(String msg, Throwable cause)
		{
			super(msg, cause);
		}
	}
	
	public static class IO extends ConfigurationException
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -4975984882605097544L;
		
		public IO()
		{
		}
		
		public IO(String msg)
		{
			super(msg);
		}
		
		public IO(IOException cause)
		{
			super(cause);
		}
		
		public IO(String msg, IOException cause)
		{
			super(msg, cause);
		}
	}
}
