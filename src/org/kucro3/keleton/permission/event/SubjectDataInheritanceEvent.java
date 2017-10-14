package org.kucro3.keleton.permission.event;

import org.spongepowered.api.service.permission.Subject;

public interface SubjectDataInheritanceEvent extends SubjectDataEvent {
	public static interface Clear extends SubjectDataInheritanceEvent
	{
	}
	
	public static interface Add extends SubjectDataInheritanceEvent
	{
		public Subject getAddedParent();
	}
	
	public static interface Remove extends SubjectDataInheritanceEvent
	{
		public Subject getRemovedParent();
	}
}
