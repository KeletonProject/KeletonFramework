package org.kucro3.keleton.service.economy;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.kucro3.keleton.economy.EnhancedAccount;
import org.kucro3.keleton.economy.EnhancedCurrency;
import org.kucro3.keleton.service.economy.EventHelper.TransactionEventBuilder;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.kucro3.util.Reference;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransferResult;

public abstract class AccountImpl implements EnhancedAccount {
	AccountImpl(EconomyServiceImpl owner, DatabaseConnection db, String table, String identifier) throws SQLException
	{
		this.owner = owner;
		this.db = db;
		this.identifier = identifier;
		this.table = table;
		this.cache = new ConcurrentHashMap<>();
		
		this.loadAll();
		this.trim();
	}
	
	@Override
	public Set<Context> getActiveContexts()
	{
		return owner.accumulate(this);
	}

	@Override
	public String getIdentifier() 
	{
		return identifier;
	}

	@Override
	public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) 
	{
		return deposit(currency, amount, cause, contexts, 0);
	}
	
	final TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts, int option)
	{
		cause = cause.merge(EventHelper.fromHandler(owner.getUniqueId()));

		EnhancedCurrency object = checkcast(currency);
		TransactionEventBuilder event = EventHelper.deposit(cause, this, currency, contexts, amount);
		try {
			if(amount.signum() < 0)
				return result(event.failed(), option);
			
			Reference<Boolean> recorded = new Reference<>(false);
			Reference<Map<EnhancedCurrency, BigDecimal>> map = new Reference<>();
			
			BigDecimal value = from(object, contexts, recorded, map);
			
			if(!checkMax(object, value = value.add(value)))
				return result(event.account_no_space(), option);
			
			update(object, value, contexts, option, recorded.get(), map.get());
			
			return result(event.success(), option);
		} catch (SQLException e) {
			return result(event.failed(), option);
		}
	}

	@Override
	public BigDecimal getBalance(Currency currency, Set<Context> contexts)
	{
		EnhancedCurrency object = checkcast(currency);
		
		Map<EnhancedCurrency, BigDecimal> map;
		BigDecimal value;
		if((map = cache.get(contexts)) != null)
			if((value = map.get(currency)) != null)
				return value;
			
		return object.getDefaultValue();
	}

	@Override
	public Map<Currency, BigDecimal> getBalances(Set<Context> contexts) 
	{
		Map<EnhancedCurrency, BigDecimal> map;
		if((map = cache.get(contexts)) == null)
			return Collections.emptyMap();
		
		return Collections.unmodifiableMap(new HashMap<>(map));
	}

	@Override
	public BigDecimal getDefaultBalance(Currency currency)
	{
		return checkcast(currency).getDefaultValue();
	}

	@Override
	public boolean hasBalance(Currency currency, Set<Context> contexts) 
	{
		Map<EnhancedCurrency, BigDecimal> map;
		if((map = cache.get(contexts)) == null)
			return false;
		return map.containsKey(currency);
	}

	@Override
	public TransactionResult resetBalance(Currency currency, Cause cause, Set<Context> contexts) 
	{
		return setBalance(currency, checkcast(currency).getDefaultValue(), cause, contexts);
	}

	@Override
	public Map<Currency, TransactionResult> resetBalances(Cause cause, Set<Context> contexts) 
	{
		Map<EnhancedCurrency, BigDecimal> cached;
		if((cached = cache.get(contexts)) == null)
			return Collections.emptyMap();
		
		Map<Currency, TransactionResult> map = new HashMap<>();
		for(Currency currency : cached.keySet())
			resetBalance(currency, cause, contexts);
		
		return Collections.unmodifiableMap(map);
	}

	@Override
	public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) 
	{
		return setBalance(currency, amount, cause, contexts, 0);
	}
	
	final TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts,
			int option)
	{
		cause = cause.merge(EventHelper.fromHandler(owner.getUniqueId()));

		EnhancedCurrency object = checkcast(currency);
		
		Reference<Boolean> recorded = new Reference<>(false);
		Reference<Map<EnhancedCurrency, BigDecimal>> map = new Reference<>();
		
		BigDecimal value = from(object, contexts, recorded, map);
		BigDecimal delta = amount.subtract(value);
		BigDecimal deltaAbs = delta.abs();
		
		TransactionEventBuilder event;
		
		switch(delta.signum())
		{
		case 0:
			return EventHelper.deposit(cause, this, currency, contexts, BigDecimal.ZERO).buildTransaction();
			
		case -1:
			event = EventHelper.withdraw(cause, this, currency, contexts, deltaAbs);
			if(!checkMin(object, amount))
				return event.account_no_funds().fire();
			break;
			
		case 1:
			event = EventHelper.deposit(cause, this, currency, contexts, deltaAbs);
			if(!checkMax(object, amount))
				return event.account_no_space().fire();
			break;
			
		default:
			throw new IllegalStateException("Should not reach here");
		}
		
		try {
			update(object, amount, contexts, option, recorded.get(), map.get());
		} catch (SQLException e) {
			return event.failed().fire();
		}
		
		return event.success().fire();
	}

	@Override
	public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause,
			Set<Context> contexts)
	{
		return transfer(to, currency, amount, cause, contexts, 0);
	}
	
	final TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause,
			Set<Context> contexts, int option)
	{
		cause = cause.merge(EventHelper.fromHandler(owner.getUniqueId()));

		AccountImpl account = checkcast(to);
		TransactionEventBuilder event = EventHelper.transfer(cause, this, to, currency, contexts, amount);
		TransactionResult temp;
		
		temp = this.withdraw(currency, amount, cause, contexts, Misc.sync() | option);
		if(!temp.getResult().equals(ResultType.SUCCESS))
			return event.result(temp.getResult()).fireTransfer();
			
		temp = account.deposit(currency, amount, cause, contexts, Misc.sync() | option);
		if(!temp.getResult().equals(ResultType.SUCCESS))
			return event.result(temp.getResult()).fireTransfer();
			
		return event.success().fireTransfer();
	}

	@Override
	public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) 
	{
		return withdraw(currency, amount, cause, contexts, 0);
	}
	
	final TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts, int option)
	{
		cause = cause.merge(EventHelper.fromHandler(owner.getUniqueId()));

		EnhancedCurrency object = checkcast(currency);
		TransactionEventBuilder event = EventHelper.withdraw(cause, this, currency, contexts, amount);
		try {
			if(amount.signum() < 0)
				return result(event.failed(), option);
			
			Reference<Boolean> recorded = new Reference<>(false);
			Reference<Map<EnhancedCurrency, BigDecimal>> map = new Reference<>();
			
			BigDecimal value = from(object, contexts, recorded, map);
			
			if(!checkMin(object, value = value.subtract(value)))
				return result(event.account_no_funds(), option);
			
			update(object, value, contexts, option, recorded.get(), map.get());

			return result(event.success(), option);
		} catch (SQLException e) {
			return result(event.failed(), option);
		}
	}
	
	final void update(EnhancedCurrency object, BigDecimal value, Set<Context> contexts, int option, boolean recorded,
			Map<EnhancedCurrency, BigDecimal> map)
		throws SQLException
	{
		if(!Misc.nosql(option))
			if(recorded)
				update(object, value, contexts);
			else
				insert(object, value, contexts);
		
		map.put(object, value);
	}
	
	final boolean checkMax(EnhancedCurrency object, BigDecimal value)
	{
		if(object.hasMaxValue())
			if(value.compareTo(object.getMaxValue()) > 0)
				return false;
		return true;
	}
	
	final boolean checkMin(EnhancedCurrency object, BigDecimal value)
	{
		if(object.hasMinValue())
			if(value.compareTo(object.getMinValue()) < 0)
				return false;
		return true;
	}
	
	final BigDecimal from(EnhancedCurrency object, Set<Context> contexts, Reference<Boolean> recorded,
			Reference<Map<EnhancedCurrency, BigDecimal>> mapref)
	{
		BigDecimal value;
		Map<EnhancedCurrency, BigDecimal> map;
		
		if((map = cache.get(contexts)) == null)
			cache.put(contexts, map = Misc.concurrentHashMap(object, value = tryQuery(object, contexts, recorded)));
		else if((value = map.get(object)) == null)
			map.put(object, value = tryQuery(object, contexts, recorded));
		else
			Optional.ofNullable(recorded).ifPresent((ref) -> ref.accept(true));
		
		final Map<EnhancedCurrency, BigDecimal> finalMap = map;
		Optional.ofNullable(mapref).ifPresent((ref) -> ref.accept(finalMap));
		
		return value;
	}
	
	final BigDecimal tryQuery(EnhancedCurrency currency, Set<Context> contexts, Reference<Boolean> recorded)
	{
		try {
			BigDecimal value = query(currency, contexts);
			Optional.ofNullable(recorded).ifPresent((ref) -> ref.accept(value != null));
			if(value == null)
				return currency.getDefaultValue();
			return value;
		} catch (SQLException e) {
			return currency.getDefaultValue();
		}
	}
	
	synchronized BigDecimal query(Currency currency, Set<Context> contexts) throws SQLException
	{
		final BigDecimal hash = Misc.hash128(Misc.serialize(contexts));
		
		PreparedStatement statement =
				db.prepareStatement("SELECT * FROM " + table
								+ "  WHERE UID=?"
								+ "    AND CURRENCY=?"
								+ "    AND CONTEXT_HASH=?");
		statement.setString(1, identifier);
		statement.setString(2, currency.getId());
		statement.setBigDecimal(3, hash);
		ResultSet result = statement.executeQuery();
		
		BigDecimal value;
		
		if(result.next())
			value = result.getBigDecimal("VALUE");
		else
			return null;
		
		if(!result.next())
			return value;
		
		throw new IllegalStateException("Database Feature Failure");
	}
	
	synchronized void insert(Currency currency, BigDecimal amount, Set<Context> contexts) throws SQLException
	{
		final String ctxs = Misc.serialize(contexts);
		final BigDecimal hash = Misc.hash128(ctxs);
		db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("INSERT INTO " + table
											 + " (UID, CURRENCY, VALUE, CONTEXT, CONTEXT_HASH)"
											 + " VALUES (?, ?, ?, ?, ?)");
			statement.setString(1, identifier);
			statement.setString(2, currency.getId());
			statement.setBigDecimal(3, amount);
			statement.setString(4, ctxs);
			statement.setBigDecimal(5, hash);
			statement.executeUpdate();
		});
	}
	
	synchronized void update(Currency currency, BigDecimal amount, Set<Context> contexts) throws SQLException
	{
		final BigDecimal hash = Misc.hash128(Misc.serialize(contexts));
		db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("UPDATE " + table
											 + " SET VALUE=?"
											 + " WHERE UID=?"
											 + "   AND CURRENCY=?"
											 + "   AND CONTEXT_HASH=?");
			statement.setBigDecimal(1, amount);
			statement.setString(2, identifier);
			statement.setString(3, currency.getId());
			statement.setBigDecimal(4, hash);
			statement.executeUpdate();
		});
	}
	
	synchronized void delete(Currency currency, Set<Context> contexts) throws SQLException
	{
		final BigDecimal hash = Misc.hash128(Misc.serialize(contexts));
		db.process((connection) -> {
			PreparedStatement statement =
					connection.prepareStatement("DELETE FROM " + table
											 + " WHERE UID=?"
											 + "   AND CURRENCY=?"
											 + "   AND CONTEXT_HASH=?");
			statement.setString(1, identifier);
			statement.setString(2, currency.getId());
			statement.setBigDecimal(3, hash);
			statement.executeUpdate();
		});
	}
	
	synchronized void loadAll() throws SQLException
	{
		PreparedStatement statement = 
				db.prepareStatement("SELECT * FROM " + table
						 		+ " WHERE UID=?");
		statement.setString(1, identifier);
		ResultSet result = statement.executeQuery();
		
		while(result.next())
		{
			String currencyId = result.getString("CURRENCY");
			BigDecimal value = result.getBigDecimal("VALUE");
			String ctxs = result.getString("CONTEXT");
			
			Set<Context> contexts = Misc.deserialize(ctxs);
			Optional<Currency> optional = owner.getCurrency(currencyId);
			if(!optional.isPresent())
				continue;
			EnhancedCurrency currency = checkcast(optional.get());
			
			Map<EnhancedCurrency, BigDecimal> map;
			if((map = cache.get(contexts)) == null)
				cache.put(contexts, map = new ConcurrentHashMap<>());
			
			map.put(currency, value);
		}
	}
	
	@Override
	public synchronized boolean trim()
	{
		final Set<Currency> currencies = owner.getCurrencies();
		final Set<EnhancedCurrency> objects = new HashSet<>();
		
		for(Currency currency : currencies)
			objects.add(checkcast(currency));
		
		try {
			for(Map.Entry<Set<Context>, Map<EnhancedCurrency, BigDecimal>> entry : cache.entrySet())
				for(EnhancedCurrency object : objects)
					if(!entry.getValue().containsKey(object))
					{
						insert(object, object.getDefaultValue(), entry.getKey());
						entry.getValue().put(object, object.getDefaultValue());
					}
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	static EnhancedCurrency checkcast(Currency currency)
	{
		if(!(currency instanceof EnhancedCurrency))
			throw new IllegalArgumentException("Unsupported currency: " + currency.getId());
		
		return (EnhancedCurrency) currency;
	}
	
	static AccountImpl checkcast(Account account)
	{
		if(!(account instanceof AccountImpl))
			throw new IllegalArgumentException();
		
		return (AccountImpl) account;
	}
	
	static TransactionResult result(TransactionEventBuilder event, int option)
	{
		return Misc.sync(option) ? event.buildTransaction() : event.fire();
	}
	
	final Map<Set<Context>, Map<EnhancedCurrency, BigDecimal>> cache;

	final DatabaseConnection db;
	
	final String table;
	
	final String identifier;
	
	final EconomyServiceImpl owner;
}
