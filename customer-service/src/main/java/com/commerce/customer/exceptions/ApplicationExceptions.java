package com.commerce.customer.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

	public static <T> Mono<T> customerNotFound(Integer id) {
		return Mono.error(new CustomerNotFoundException(id));
	}

	public static <T> Mono<T> inSufficientBalance(Integer id) {
		return Mono.error(new InSufficientBalanceException(id));
	}

	public static <T> Mono<T> inSufficientShareException(Integer id) {
		return Mono.error(new InSufficientShareException(id));
	}
}
