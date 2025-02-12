package com.commerce.aggregator.exceptions;

public class CustomerNotFoundException extends RuntimeException {

	private static final String MESSAGE = "Customer not found with id: %s";

	public CustomerNotFoundException(Integer id) {
		super(MESSAGE.formatted(id));
	}

}
