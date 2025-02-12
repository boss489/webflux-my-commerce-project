package com.commerce.aggregator.client;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.commerce.aggregator.domain.Ticker;
import com.commerce.aggregator.dto.PriceUpdate;
import com.commerce.aggregator.dto.StockPriceResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@RequiredArgsConstructor
public class StockServiceClient {
	private final WebClient webClient;
	private Flux<PriceUpdate> flux;

	public Mono<StockPriceResponse> getStockPrice(Ticker ticker){
		return this.webClient.get()
			.uri("/stock/{ticker}", ticker)
			.retrieve()
			.bodyToMono(StockPriceResponse.class);
	}

	public Flux<PriceUpdate> getStockPriceStream(){
		if(this.flux == null) {
			this.flux = getPriceUpdates();
		}
		return this.flux;
	}

	private Flux<PriceUpdate> getPriceUpdates(){
		return this.webClient.get()
			.uri("/stock/price-stream")
			.accept(MediaType.APPLICATION_NDJSON)
			.retrieve()
			.bodyToFlux(PriceUpdate.class)
			.retryWhen(retry())
			.cache(1);
	}
	private Retry retry(){
		return Retry.fixedDelay(100, Duration.ofSeconds(10))
			.doBeforeRetry(rs -> log.info("stock service price stream call failed. retrying {}", rs.failure().getMessage()));

	}
}
