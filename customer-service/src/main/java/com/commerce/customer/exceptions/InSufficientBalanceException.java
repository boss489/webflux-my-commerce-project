package com.commerce.customer.exceptions;

public class InSufficientBalanceException extends RuntimeException {
    private static final String MESSAGE = "customer [id=%d] does not have sufficient balance";

	public InSufficientBalanceException(Integer customerId) {
		super(MESSAGE.formatted(customerId));
	}

}
