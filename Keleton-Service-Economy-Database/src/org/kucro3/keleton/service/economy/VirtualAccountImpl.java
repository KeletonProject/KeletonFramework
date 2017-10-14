package org.kucro3.keleton.service.economy;

import java.sql.SQLException;

import org.kucro3.keleton.sql.DatabaseConnection;
import org.spongepowered.api.service.economy.account.VirtualAccount;
import org.spongepowered.api.text.Text;

public class VirtualAccountImpl extends AccountImpl implements VirtualAccount {
	VirtualAccountImpl(EconomyServiceImpl owner, DatabaseConnection db, String table, String identifier)
			throws SQLException 
	{
		super(owner, db, table, Misc.Naming.virtual(identifier));
		this.displayName = Text.of("Virtual: " + identifier);
	}

	@Override
	public Text getDisplayName()
	{
		return displayName;
	}
	
	final Text displayName;
}
