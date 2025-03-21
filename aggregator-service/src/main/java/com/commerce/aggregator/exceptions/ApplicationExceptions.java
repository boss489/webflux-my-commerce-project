package com.commerce.aggregator.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

	public static <T> Mono<T> customerNotFound(Integer id) {
		return Mono.error(new CustomerNotFoundException(id));
	}

	public static <T> Mono<T> InvalidTradeRequestException(String message) {
		return Mono.error(new InvalidTradeRequestException(message));
	}

	public static <T> Mono<T> missingTicker() {
		return Mono.error(new InvalidTradeRequestException("Ticker is required"));
	}

	public static <T> Mono<T> missingTradeAction() {
		return Mono.error(new InvalidTradeRequestException("trade action is required"));
	}

	public static <T> Mono<T> invalidQuantity() {
		return Mono.error(new InvalidTradeRequestException("quantity should be > 0"));
	}



}
