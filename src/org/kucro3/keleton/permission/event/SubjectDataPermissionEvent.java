package org.kucro3.keleton.permission.event;

import org.spongepowered.api.util.Tristate;

public interface SubjectDataPermissionEvent extends SubjectDataEvent {
	public static interface Clear extends SubjectDataPermissionEvent
	{
	}
	
	public static interface Set extends SubjectDataPermissionEvent
	{
		public String getPermission();
		
		public Tristate getValue();
	}
}
