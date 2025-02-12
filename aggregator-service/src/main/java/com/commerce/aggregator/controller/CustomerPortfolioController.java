package com.commerce.aggregator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.aggregator.dto.CustomerInformation;
import com.commerce.aggregator.dto.StockTradeRequest;
import com.commerce.aggregator.dto.StockTradeResponse;
import com.commerce.aggregator.dto.TradeRequest;
import com.commerce.aggregator.service.CustomerPortfolioService;
import com.commerce.aggregator.validator.RequestValidator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("customers")
public class CustomerPortfolioController {

	private final CustomerPortfolioService customerPortfolioService;

	@GetMapping("/{customerId}")
	public Mono<CustomerInformation> getCustomerPortfolio(@PathVariable Integer customerId){
		return this.customerPortfolioService.getCustomerInformation(customerId);
	}

	@PostMapping("/{customerId}/trade")
	public Mono<StockTradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<TradeRequest> mono){
		return mono.transform(RequestValidator.validate())
				   .flatMap(request -> this.customerPortfolioService.trade(customerId, request));
	}
}
