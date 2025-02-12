package com.commerce.aggregator.client;

import java.util.Objects;

import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.commerce.aggregator.dto.CustomerInformation;
import com.commerce.aggregator.dto.StockTradeRequest;
import com.commerce.aggregator.dto.StockTradeResponse;
import com.commerce.aggregator.exceptions.ApplicationExceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class CustomerServiceClient {

	private final WebClient webClient;

	public Mono<CustomerInformation> getCustomerInformation(Integer customerId){
		return webClient.get()
			.uri("/customers/{customerId}", customerId)
			.retrieve()
			.bodyToMono(CustomerInformation.class)
			.onErrorResume(WebClientResponseException.NotFound.class, ex -> ApplicationExceptions.customerNotFound(customerId));
	}

	public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest request){
		return webClient.post()
			.uri("/customers/{customerId}/trade", customerId)
			.bodyValue(request)
			.retrieve()
			.bodyToMono(StockTradeResponse.class)
			.onErrorResume(WebClientResponseException.NotFound.class, ex -> ApplicationExceptions.customerNotFound(customerId))
			.onErrorResume(WebClientResponseException.BadRequest.class, this::handleException);
	}

	private <T> Mono<T> handleException(WebClientResponseException.BadRequest exception){
		var pd = exception.getResponseBodyAs(ProblemDetail.class);
		var message = Objects.nonNull(pd) ? pd.getDetail() : exception.getMessage();
		log.error("customer service problem detail: {}", pd);
		return ApplicationExceptions.InvalidTradeRequestException(message);

	}
}
