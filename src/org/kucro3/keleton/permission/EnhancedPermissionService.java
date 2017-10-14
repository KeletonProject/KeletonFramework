package org.kucro3.keleton.permission;

import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.KeyToken;
import org.spongepowered.api.service.permission.PermissionService;

public interface EnhancedPermissionService extends PermissionService {
    public static final KeyToken<EnhancedPermissionService> TOKEN =
            KeyToken.of(Key.of("EnhancedPermissionService", EnhancedPermissionService.class, EnhancedPermissionService.class));
}
