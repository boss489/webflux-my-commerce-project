package com.commerce.customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.customer.dto.CustomerInformation;
import com.commerce.customer.dto.StockTradeRequest;
import com.commerce.customer.dto.StockTradeResponse;
import com.commerce.customer.service.CustomerService;
import com.commerce.customer.service.TradeService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController {

	private final CustomerService customerService;
	private final TradeService tradeService;

	@GetMapping("{customerId}")
	public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId){
		return this.customerService.getCustomerInformation(customerId);
	}

	@PostMapping("{customerId}/trade")
	public Mono<StockTradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<StockTradeRequest> mono){
		return mono.flatMap(req -> this.tradeService.trade(customerId, req));
	}
}
