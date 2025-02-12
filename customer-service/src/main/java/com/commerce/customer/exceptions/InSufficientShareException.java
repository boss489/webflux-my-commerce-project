package com.commerce.customer.exceptions;

public class InSufficientShareException extends RuntimeException {
    private static final String MESSAGE = "customer [id=%d] does not have sufficient share";

	public InSufficientShareException(Integer customerId) {
		super(MESSAGE.formatted(customerId));
	}
    
}
