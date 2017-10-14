package org.kucro3.keleton.service.economy;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.kucro3.keleton.UniqueService;
import org.kucro3.keleton.cause.FromUniqueService;
import org.kucro3.keleton.economy.EnhancedCurrency;
import org.kucro3.keleton.economy.EnhancedCurrency.Builder;
import org.kucro3.keleton.economy.EnhancedEconomyService;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.economy.EconomyTransactionEvent;
import org.spongepowered.api.event.filter.cause.Named;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

public class EconomyServiceImpl implements EnhancedEconomyService, UniqueService {
	EconomyServiceImpl(DatabaseConnection db, String table)
	{
		this.db = db;
		this.table = table;
		this.calculators = new ArrayList<>();
		this.currencies = new ConcurrentHashMap<>();
		this.uniques = new ConcurrentHashMap<>();
		this.virtuals = new ConcurrentHashMap<>();
	}
	
	@Override
	public Set<Currency> getCurrencies() 
	{
		return Collections.unmodifiableSet(new HashSet<>(this.currencies.values()));
	}

	@Override
	public Currency getDefaultCurrency() 
	{
		return defaultCurrency;
	}

	@Override
	public Optional<UniqueAccount> getOrCreateAccount(UUID uuid) 
	{
		UniqueAccountImpl account;
		if((account = uniques.get(uuid)) == null)
			try {
				uniques.put(uuid, account = new UniqueAccountImpl(this, db, table, uuid));
			} catch (SQLException e) {
				e.printStackTrace();
				return Optional.empty();
			}
		return Optional.of(account);
	}

	@Override
	public Optional<Account> getOrCreateAccount(String identifier) 
	{
		VirtualAccountImpl account;
		if((account = virtuals.get(identifier)) == null)
			try {
				virtuals.put(identifier, account = new VirtualAccountImpl(this, db, table, identifier));
			} catch (SQLException e) {
				return Optional.empty();
			}
		return Optional.of(account);
	}

	@Override
	public boolean hasAccount(UUID uuid)
	{
		return uniques.containsKey(uuid);
	}

	@Override
	public boolean hasAccount(String identifier) 
	{
		return virtuals.containsKey(identifier);
	}

	@Override
	public void registerContextCalculator(ContextCalculator<Account> calculator) 
	{
		this.calculators.add(calculator);
	}
	
	Set<Context> accumulate(Account account)
	{
		return accumulate(account, new HashSet<>());
	}
	
	Set<Context> accumulate(Account account, Set<Context> set)
	{
		for(ContextCalculator<Account> calculator : calculators)
			calculator.accumulateContexts(account, set);
		return Collections.unmodifiableSet(set);
	}
	
	boolean matches(Account account, Set<Context> contexts)
	{
		for(ContextCalculator<Account> calculator : calculators)
			for(Context context : contexts)
				if(!calculator.matches(context, account))
					return false;
		return true;
	}

	@Override
	public Optional<Currency> getCurrency(String id)
	{
		return Optional.ofNullable(this.currencies.get(id));
	}

	@Override
	public boolean registerCurrency(EnhancedCurrency currency)
	{
		return this.currencies.putIfAbsent(currency.getId(), currency) == null;
	}

	@Override
	public boolean setDefaultCurrency(EnhancedCurrency currency)
	{
		if(currency == null)
			return false;
		if(currency.getOwner() != this)
			return false;
		this.defaultCurrency = currency;
		return true;
	}

	@Override
	public boolean trimAccounts()
	{
		for(AccountImpl account : virtuals.values())
			if(!account.trim())
				return false;
		
		for(AccountImpl account : uniques.values())
			if(!account.trim())
				return false;
		
		return true;
	}

	@Override
	public boolean unregisterCurrency(String id)
	{
		return currencies.remove(id) != null;
	}

	@Override
	public Builder newCurrencyBuilder() 
	{
		return new CurrencyBuilderImpl(this);
	}
	
	synchronized void ensureTable() throws SQLException
	{
		db.execute("CREATE TABLE IF NOT EXISTS " + table
				+ " ("
				+ " UID varchar(255) NOT NULL,"
				+ " CURRENCY varchar(255) NOT NULL"
				+ " VALUE decimal(38, 2),"
				+ " CONTEXT text,"
				+ " CONTEXT_HASH decimal(38),"
				+ " UNIQUE (UID, CURRENCY, CONTEXT_HASH)"
				+ " )");
	}
	
	synchronized void loadAll() throws SQLException
	{
		PreparedStatement statement = 
				db.prepareStatement("SELECT DISTINCT UID FROM " + table);
		ResultSet result = statement.executeQuery();
		while(result.next())
		{
			String uid = result.getString("UID");
			
			if(Misc.Naming.isUnique(uid))
			{
				UUID uuid = UUID.fromString(Misc.Naming.identifier(uid));
				uniques.put(uuid, new UniqueAccountImpl(this, db, table, uuid));
			}
			else if(Misc.Naming.isVirtual(uid))
			{
				String identifier = Misc.Naming.identifier(uid);
				virtuals.put(identifier, new VirtualAccountImpl(this, db, table, identifier));
			}
			else
				continue;
		}
	}

	@Listener
	public void _SYNC_onTransaction(EconomyTransactionEvent event, @Named("handler") FromUniqueService service)
	{
		if(isSelf(service))
			return;

		TransactionResult result = event.getTransactionResult();
		TransactionType type = result.getType();
		AccountImpl impl = (AccountImpl) Objects.requireNonNull(result.getAccount());

		Currency currency = result.getCurrency();
		Set<Context> contexts = result.getContexts();
		BigDecimal amount = result.getAmount();
		Cause cause = event.getCause();

		if(TransactionTypes.TRANSFER.equals(type))
			impl.transfer(
					Objects.requireNonNull(((TransferResult) result).getAccountTo()),
					currency,
					amount,
					cause,
					contexts,
					Misc.sync() | Misc.nosql()
			);
		else if(TransactionTypes.DEPOSIT.equals(type))
			impl.deposit(
					currency,
					amount,
					cause,
					contexts,
					Misc.sync() | Misc.nosql()
			);
		else if(TransactionTypes.WITHDRAW.equals(type))
			impl.withdraw(
					currency,
					amount,
					cause,
					contexts,
					Misc.sync() | Misc.nosql()
			);
		else
		{
			// unsupported, may be out of date
		}
	}

	boolean isSelf(FromUniqueService service)
	{
		return uuid.equals(service.getUniqueId());
	}

	public EconomyServiceImpl _ENABLE_()
	{
		Sponge.getEventManager().registerListeners(this, SpongeMain.getInstance());
		return this;
	}
	
	public EconomyServiceImpl _DISABLE_()
	{
		Sponge.getEventManager().unregisterListeners(this);
		return this;
	}

	@Override
	public UUID getUniqueId()
	{
		return this.uuid;
	}
	
	final Map<String, VirtualAccountImpl> virtuals;
	
	final Map<UUID, UniqueAccountImpl> uniques;
	
	final Map<String, EnhancedCurrency> currencies;
	
	EnhancedCurrency defaultCurrency;
	
	final DatabaseConnection db;
	
	final String table;
	
	final List<ContextCalculator<Account>> calculators;

	final UUID uuid = UUID.randomUUID();
}
