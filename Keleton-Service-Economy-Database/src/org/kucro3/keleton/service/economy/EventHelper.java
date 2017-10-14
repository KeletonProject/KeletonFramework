package org.kucro3.keleton.service.economy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.kucro3.keleton.cause.FromUniqueService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.economy.EconomyTransactionEvent;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

public class EventHelper {
	private EventHelper()
	{
	}
	
	public static TransactionEventBuilder transfer(Cause cause, Account from, Account to)
	{
		return new TransactionEventBuilder().cause(cause).from(from).transfer(to);
	}
	
	public static TransactionEventBuilder transfer(Cause cause, Account from, Account to, Currency currency)
	{
		return transfer(cause, from, to).currency(currency);
	}
	
	public static TransactionEventBuilder transfer(Cause cause, Account from, Account to, Currency currency, Set<Context> contexts)
	{
		return transfer(cause, from, to, currency).contexts(contexts);
	}
	
	public static TransactionEventBuilder transfer(Cause cause, Account from, Account to, Currency currency, Set<Context> contexts, BigDecimal amount)
	{
		return transfer(cause, from, to, currency, contexts).amount(amount);
	}
	
	public static TransactionEventBuilder withdraw(Cause cause, Account from)
	{
		return new TransactionEventBuilder().cause(cause).from(from).withdraw();
	}
	
	public static TransactionEventBuilder withdraw(Cause cause, Account from, Currency currency)
	{
		return withdraw(cause, from).currency(currency);
	}
	
	public static TransactionEventBuilder withdraw(Cause cause, Account from, Currency currency, Set<Context> contexts)
	{
		return withdraw(cause, from, currency).contexts(contexts);
	}
	
	public static TransactionEventBuilder withdraw(Cause cause, Account from, Currency currency, Set<Context> contexts, BigDecimal amount)
	{
		return withdraw(cause, from, currency, contexts).amount(amount);
	}
	
	public static TransactionEventBuilder deposit(Cause cause, Account from)
	{
		return new TransactionEventBuilder().cause(cause).from(from).deposit();
	}
	
	public static TransactionEventBuilder deposit(Cause cause, Account from, Currency currency)
	{
		return deposit(cause, from).currency(currency);
	}
	
	public static TransactionEventBuilder deposit(Cause cause, Account from, Currency currency, Set<Context> contexts)
	{
		return deposit(cause, from, currency).contexts(contexts);
	}
	
	public static TransactionEventBuilder deposit(Cause cause, Account from, Currency currency, Set<Context> contexts, BigDecimal amount)
	{
		return deposit(cause, from, currency, contexts).amount(amount);
	}

	public static Cause fromHandler(UUID uuid)
	{
		return Cause.builder().named("handler", (FromUniqueService) () -> uuid).build();
	}
	
	public static class TransactionEventBuilder
	{
		public TransactionResult fire()
		{
			EconomyTransactionEvent event = buildEvent();
			Sponge.getEventManager().post(event);
			return event.getTransactionResult();
		}
		
		public TransferResult fireTransfer()
		{
			return (TransferResult) fire();
		}
		
		public EconomyTransactionEvent buildEvent()
		{
			return new EconomyTransactionEventImpl(cause(), buildTransaction());
		}
		
		public TransactionEventBuilder cause(Cause cause)
		{
			this.cause = cause;
			return this;
		}
		
		public TransactionResult buildTransaction()
		{
			if(type().equals(TransactionTypes.TRANSFER))
				return new TransferResultImpl(from(), type(), result(), currency(), amount(), contexts(), to());
			return new TransactionResultImpl(from(), type(), result(), currency(), amount(), contexts());
		}
		
		public TransactionEventBuilder from(Account from)
		{
			this.from = from;
			return this;
		}
		
		public TransactionEventBuilder currency(Currency currency)
		{
			this.currency = currency;
			return this;
		}
		
		public TransactionEventBuilder contexts(Set<Context> contexts)
		{
			this.contexts = contexts;
			return this;
		}
		
		public TransactionEventBuilder result(ResultType result)
		{
			this.result = result;
			return this;
		}
		
		public TransactionEventBuilder account_no_funds()
		{
			return result(ResultType.ACCOUNT_NO_FUNDS);
		}
		
		public TransactionEventBuilder account_no_space()
		{
			return result(ResultType.ACCOUNT_NO_SPACE);
		}
		
		public TransactionEventBuilder context_mismatch()
		{
			return result(ResultType.CONTEXT_MISMATCH);
		}
		
		public TransactionEventBuilder failed()
		{
			return result(ResultType.FAILED);
		}
		
		public TransactionEventBuilder success()
		{
			return result(ResultType.SUCCESS);
		}
		
		private TransactionEventBuilder type(TransactionType type)
		{
			this.type = type;
			return this;
		}
		
		public TransactionEventBuilder withdraw()
		{
			return type(TransactionTypes.WITHDRAW);
		}
		
		public TransactionEventBuilder withdraw(BigDecimal amount)
		{
			return withdraw().amount(amount);
		}
		
		public TransactionEventBuilder deposit()
		{
			return type(TransactionTypes.DEPOSIT);
		}
		
		public TransactionEventBuilder deposit(BigDecimal amount)
		{
			return deposit().amount(amount);
		}
		
		public TransactionEventBuilder transfer(Account to)
		{
			return type(TransactionTypes.TRANSFER).to(to);
		}
		
		public TransactionEventBuilder transfer(Account to, BigDecimal amount)
		{
			return transfer(to).amount(amount);
		}
		
		public TransactionEventBuilder amount(BigDecimal amount)
		{
			this.amount = amount;
			return this;
		}
		
		private TransactionEventBuilder to(Account to)
		{
			this.to = to;
			return this;
		}
		
		Cause cause()
		{
			return Objects.requireNonNull(cause);
		}
		
		Account from()
		{
			return Objects.requireNonNull(from);
		}
		
		Account to()
		{
			return Objects.requireNonNull(to);
		}
		
		Set<Context> contexts()
		{
			return Objects.requireNonNull(contexts);
		}
		
		TransactionType type()
		{
			return Objects.requireNonNull(type);
		}
		
		ResultType result()
		{
			return Objects.requireNonNull(result);
		}
		
		Currency currency()
		{
			return Objects.requireNonNull(currency);
		}
		
		BigDecimal amount()
		{
			return Objects.requireNonNull(amount);
		}
		
		Account from;
		
		Account to;
		
		Set<Context> contexts = Collections.emptySet();
		
		TransactionType type;
		
		ResultType result;
		
		Currency currency;
		
		BigDecimal amount;
		
		Cause cause;
	}
	
	private static class EconomyTransactionEventImpl implements EconomyTransactionEvent
	{
		EconomyTransactionEventImpl(Cause cause, TransactionResult result)
		{
			this.cause = cause;
			this.result = result;
		}
		
		@Override
		public Cause getCause() 
		{
			return cause;
		}

		@Override
		public TransactionResult getTransactionResult() 
		{
			return result;
		}
		
		private final Cause cause;
		
		private final TransactionResult result;
	}
	
	private static class TransactionResultImpl implements TransactionResult {
		TransactionResultImpl(Account account, TransactionType type, ResultType result, Currency currency, BigDecimal amount,
				Set<Context> contexts)
		{
			this.account = account;
			this.type = type;
			this.result = result;
			this.currency = currency;
			this.amount = amount;
			this.contexts = contexts;
		}
		
		@Override
		public Account getAccount()
		{
			return account;
		}

		@Override
		public BigDecimal getAmount()
		{
			return amount;
		}

		@Override
		public Set<Context> getContexts()
		{
			return contexts;
		}

		@Override
		public Currency getCurrency() 
		{
			return currency;
		}

		@Override
		public ResultType getResult()
		{
			return result;
		}

		@Override
		public TransactionType getType()
		{
			return type;
		}
		
		final Account account;
		
		final BigDecimal amount;
		
		final Set<Context> contexts;
		
		final Currency currency;
		
		final ResultType result;
		
		final TransactionType type;
	}
	
	private static class TransferResultImpl extends TransactionResultImpl implements TransferResult {

		TransferResultImpl(Account account, TransactionType type, ResultType result, Currency currency, BigDecimal amount,
				Set<Context> contexts, Account destination) 
		{
			super(account, type, result, currency, amount, contexts);
			this.destination = destination;
		}

		@Override
		public Account getAccountTo() 
		{
			return destination;
		}
		
		final Account destination;
	}

}
