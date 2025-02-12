package com.commerce.aggregator.validator;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import com.commerce.aggregator.dto.TradeRequest;
import com.commerce.aggregator.exceptions.ApplicationExceptions;

import reactor.core.publisher.Mono;

public class RequestValidator {

	public  static UnaryOperator<Mono<TradeRequest>> validate() {
		return mono -> mono.filter(hasTicker())
			.switchIfEmpty(ApplicationExceptions.missingTicker())
			.filter(hasTradeAction())
			.switchIfEmpty(ApplicationExceptions.missingTradeAction())
			.filter(isValidQuantity())
			.switchIfEmpty(ApplicationExceptions.invalidQuantity());

	}
	private static Predicate<TradeRequest> hasTicker(){
		return dto -> Objects.nonNull(dto.ticker());
	}

	private static Predicate<TradeRequest> hasTradeAction (){
		return dto -> Objects.nonNull(dto.action());
	}

	private static Predicate<TradeRequest> isValidQuantity (){
		return dto -> Objects.nonNull(dto.quantity()) && dto.quantity() > 0;
	}
}
