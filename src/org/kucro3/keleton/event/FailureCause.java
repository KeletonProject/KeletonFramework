package org.kucro3.keleton.event;

import java.util.Optional;

import org.spongepowered.api.event.cause.Cause;

public class FailureCause {
	FailureCause(Cause cause, Exception exception, String message, boolean cancellation)
	{
		this.cause = cause;
		this.exception = exception;
		this.message = message;
		this.cancellation = cancellation;
	}
	
	public Optional<Cause> getCause() 
	{
		return Optional.ofNullable(cause);
	}
	
	public Optional<Exception> getException()
	{
		return Optional.ofNullable(exception);
	}
	
	public Optional<String> getMessage()
	{
		return Optional.ofNullable(message);
	}
	
	public boolean hasCancellation()
	{
		return cancellation;
	}
	
	public Cause toCause()
	{
		final Cause.Builder builder = Cause.builder()
				.named(NAMED_FAILURE_CAUSE, this);
		getCause().ifPresent((cause) -> builder.named(NAMED_CAUSE, cause));
		getException().ifPresent((exception) -> builder.named(NAMED_EXCEPTION, exception));
		getMessage().ifPresent((message) -> builder.named(NAMED_MESSAGE, message));
		if(hasCancellation()) builder.named(NAMED_CANCELLATION, Cancelled.CANCELLED);
		return builder.build();
	}
	
	public static Builder builder()
	{
		return new Builder();
	}
	
	final Cause cause;
	
	final Exception exception;
	
	final String message;
	
	final boolean cancellation;
	
	public static final String NAMED_FAILURE_CAUSE = "failure_cause";
	
	public static final String NAMED_CAUSE = "failure_cause:cause";
	
	public static final String NAMED_EXCEPTION = "failure_cause:exception";
	
	public static final String NAMED_MESSAGE = "failure_cause:message";
	
	public static final String NAMED_CANCELLATION = "failure_cause:cancelled";
	
	public final static class Cancelled
	{
		public static final Cancelled CANCELLED = new Cancelled();
		
		private Cancelled()
		{
		}
	}
	
	public static final class Builder
	{
		Builder()
		{
		}
		
		public Builder cause(Cause cause)
		{
			this.cause = cause;
			return this;
		}
		
		public Builder exception(Exception e) 
		{
			this.exception = e;
			if(this.message == null)
				this.message = e.getMessage();
			return this;
		}
		
		public Builder message(String message)
		{
			this.message = message;
			return this;
		}
		
		public Builder cancellation(boolean cancelled)
		{
			this.cancellation = cancelled;
			return this;
		}
		
		public FailureCause build()
		{
			return new FailureCause(cause, exception, message, cancellation);
		}
		
		Cause cause;
		
		Exception exception;
		
		String message;
		
		boolean cancellation;
	}
}
