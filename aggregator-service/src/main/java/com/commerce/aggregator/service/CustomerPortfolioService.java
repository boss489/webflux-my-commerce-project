package com.commerce.aggregator.service;

import org.springframework.stereotype.Service;

import com.commerce.aggregator.client.CustomerServiceClient;
import com.commerce.aggregator.client.StockServiceClient;
import com.commerce.aggregator.dto.CustomerInformation;
import com.commerce.aggregator.dto.StockPriceResponse;
import com.commerce.aggregator.dto.StockTradeRequest;
import com.commerce.aggregator.dto.StockTradeResponse;
import com.commerce.aggregator.dto.TradeRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerPortfolioService {

	private final StockServiceClient stockServiceClient;
	private final CustomerServiceClient customerServiceClient;

	public Mono<CustomerInformation> getCustomerInformation(Integer customerId){
		return this.customerServiceClient.getCustomerInformation(customerId);
	}

	public Mono<StockTradeResponse> trade(Integer customerId, TradeRequest request){
		return this.stockServiceClient.getStockPrice(request.ticker())
			.map(StockPriceResponse::price)
			.map(price -> toStockTradeRequest(request, price))
			.flatMap(req -> this.customerServiceClient.trade(customerId, req));
	}

	private StockTradeRequest toStockTradeRequest(TradeRequest request, Integer price){
		return new StockTradeRequest(request.ticker(), price, request.quantity(), request.action());
	}
}
