package com.commerce.customer.advice;

import java.net.URI;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.commerce.customer.entity.Customer;
import com.commerce.customer.exceptions.CustomerNotFoundException;
import com.commerce.customer.exceptions.InSufficientBalanceException;
import com.commerce.customer.exceptions.InSufficientShareException;

@ControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(CustomerNotFoundException.class)
	public ProblemDetail handleException(CustomerNotFoundException e) {
		return build(HttpStatus.NOT_FOUND, e, p -> {
			p.setType(URI.create("https://commerce.com/customers-not-found"));
			p.setTitle("customer not found");
		});
	}

	@ExceptionHandler(InSufficientBalanceException.class)
	public ProblemDetail handleException(InSufficientBalanceException e) {
		return build(HttpStatus.BAD_REQUEST, e, p -> {
			p.setType(URI.create("https://commerce.com/insufficient-balance"));
			p.setTitle("Insufficient balance");
		});
	}

	@ExceptionHandler(InSufficientShareException.class)
	public ProblemDetail handleException(InSufficientShareException e) {
		return build(HttpStatus.BAD_REQUEST, e, p -> {
			p.setType(URI.create("https://commerce.com/insufficient-share"));
			p.setTitle("Insufficient share");
		});
	}


	private ProblemDetail build(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
		var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
		consumer.accept(problem);
		return problem;
	}
}
