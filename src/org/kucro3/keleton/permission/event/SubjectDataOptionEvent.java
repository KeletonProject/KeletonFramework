package org.kucro3.keleton.permission.event;

public interface SubjectDataOptionEvent extends SubjectDataEvent {
	public static interface Clear extends SubjectDataOptionEvent
	{
	}
	
	public static interface Set extends SubjectDataOptionEvent
	{
		public String getKey();
		
		public String getValue();
	}
	
	public static interface Remove extends SubjectDataOptionEvent
	{
		public String getKey();
	}
}
