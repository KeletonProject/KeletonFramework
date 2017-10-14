package org.kucro3.keleton.permission;

import java.util.Set;

import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

public interface EnhancedSubjectData extends SubjectData {
	public boolean isTransient();
	
	public Tristate getPermission(Set<Context> contexts, String permission);
}
