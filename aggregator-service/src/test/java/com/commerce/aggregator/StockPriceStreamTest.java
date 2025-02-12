package com.commerce.aggregator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;

import com.commerce.aggregator.dto.PriceUpdate;

import lombok.extern.slf4j.Slf4j;
import reactor.test.StepVerifier;

@Slf4j
public class StockPriceStreamTest extends AbstractIntegrationTest{

	@Test
	public void priceStream(){

		var responseBody = this.resourceToString("stock-service/stock-price-stream-200.jsonl");
		mockServerClient
			.when(HttpRequest.request("/stock/price-stream"))
			.respond(
				HttpResponse
					.response(responseBody)
					.withStatusCode(200)
					.withContentType(MediaType.parse(("application/x-ndjson")))
			);

		this.client.get()
			.uri("/stock/price-stream")
			.accept(org.springframework.http.MediaType.TEXT_EVENT_STREAM)
			.exchange()
			.expectStatus().is2xxSuccessful()
			.returnResult(PriceUpdate.class)
			.getResponseBody()
			.doOnNext(pu -> log.info("{}", pu))
			.as(StepVerifier::create)
			.assertNext(p -> Assertions.assertEquals(53, p.price()))
			.assertNext(p -> Assertions.assertEquals(54, p.price()))
			.assertNext(p -> Assertions.assertEquals(55, p.price()));

	}
}
