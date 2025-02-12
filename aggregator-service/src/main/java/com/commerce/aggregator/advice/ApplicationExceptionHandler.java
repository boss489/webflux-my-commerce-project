package com.commerce.aggregator.advice;

import java.net.URI;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.commerce.aggregator.exceptions.CustomerNotFoundException;
import com.commerce.aggregator.exceptions.InvalidTradeRequestException;

@ControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(CustomerNotFoundException.class)
	public ProblemDetail handleException(CustomerNotFoundException e) {
		return build(HttpStatus.NOT_FOUND, e, p -> {
			p.setType(URI.create("https://commerce.com/customers-not-found"));
			p.setTitle("customer not found");
		});
	}

	@ExceptionHandler(InvalidTradeRequestException.class)
	public ProblemDetail handleException(InvalidTradeRequestException e) {
		return build(HttpStatus.BAD_REQUEST, e, p -> {
			p.setType(URI.create("https://commerce.com/invalid-trade"));
			p.setTitle("invalid trade request");
		});
	}

	private ProblemDetail build(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
		var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
		consumer.accept(problem);
		return problem;
	}
}
