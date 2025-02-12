package com.commerce.aggregator.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.aggregator.client.StockServiceClient;
import com.commerce.aggregator.dto.PriceUpdate;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("stock")
public class StockPriceStreamContoller {

	private final StockServiceClient stockServiceClient;

	@GetMapping(value = "price-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<PriceUpdate> getStockPriceStream(){
		return this.stockServiceClient.getStockPriceStream();
	}
}
