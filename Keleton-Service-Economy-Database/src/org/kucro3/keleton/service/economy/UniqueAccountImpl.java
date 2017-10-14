package org.kucro3.keleton.service.economy;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.kucro3.keleton.sql.DatabaseConnection;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;

public class UniqueAccountImpl extends AccountImpl implements UniqueAccount {
	UniqueAccountImpl(EconomyServiceImpl owner, DatabaseConnection db, String table, UUID uuid)
			throws SQLException 
	{
		super(owner, db, table, Misc.Naming.unique(uuid));
		this.uuid = uuid;
		
		Text.Builder builder = Text.builder();
		Optional<Player> player = Sponge.getServer().getPlayer(uuid);
		if(player.isPresent())
			builder.append(Text.of("Player: ").concat(Text.of(player.get().getName())));
		else
			builder.append(Text.of("Unknown entity: ").concat(Text.of(uuid.toString())));
		
		this.displayName = builder.build();
	}

	@Override
	public Text getDisplayName()
	{
		return displayName;
	}

	@Override
	public UUID getUniqueId() 
	{
		return uuid;
	}
	
	final Text displayName;
	
	final UUID uuid;
}
