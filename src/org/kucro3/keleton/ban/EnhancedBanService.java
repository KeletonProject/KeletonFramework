package org.kucro3.keleton.ban;

import org.kucro3.keleton.keyring.Key;
import org.kucro3.keleton.keyring.KeyToken;
import org.spongepowered.api.service.ban.BanService;

public interface EnhancedBanService extends BanService {
    public static final KeyToken<EnhancedBanService> TOKEN =
            KeyToken.of(Key.of("EnhancedBanService", EnhancedBanService.class, EnhancedBanService.class));
}
