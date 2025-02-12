package com.commerce.customer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.commerce.customer.domain.Ticker;
import com.commerce.customer.domain.TradeAction;
import com.commerce.customer.dto.StockTradeRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerServiceApplicationTest {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void customerInformation(){
		getCustomer(1, HttpStatus.OK)
			.jsonPath("$.balance").isEqualTo(10000)
			.jsonPath("$.holdings").isEmpty()
			.jsonPath("$.name").isEqualTo("Sam");
	}

	@Test
	public void buyAndSell(){
		var buyRequest = new StockTradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY);
		trade(2, buyRequest, HttpStatus.OK)
			.jsonPath("$.balance").isEqualTo(9500)
			.jsonPath("$.totalPrice").isEqualTo(500);

		var buyRequest2 = new StockTradeRequest(Ticker.GOOGLE, 100, 10, TradeAction.BUY);
		trade(2, buyRequest2, HttpStatus.OK)
			.jsonPath("$.balance").isEqualTo(8500)
			.jsonPath("$.totalPrice").isEqualTo(1000);

		getCustomer(2, HttpStatus.OK)
			.jsonPath("$.holdings").isArray()
			.jsonPath("$.holdings.length()").isEqualTo(1)
			.jsonPath("$.holdings[0].ticker").isEqualTo("GOOGLE")
			.jsonPath("$.holdings[0].quantity").isEqualTo(15);

		var sellRequest = new StockTradeRequest(Ticker.GOOGLE, 110, 5, TradeAction.SELL);
		trade(2, sellRequest, HttpStatus.OK)
			.jsonPath("$.balance").isEqualTo(9050)
			.jsonPath("$.totalPrice").isEqualTo(550);

		var sellRequest2 = new StockTradeRequest(Ticker.GOOGLE, 110, 10, TradeAction.SELL);
		trade(2, sellRequest2, HttpStatus.OK)
			.jsonPath("$.balance").isEqualTo(10150)
			.jsonPath("$.totalPrice").isEqualTo(1100);

		getCustomer(2, HttpStatus.OK)
			.jsonPath("$.holdings").isArray()
			.jsonPath("$.holdings.length()").isEqualTo(1)
			.jsonPath("$.holdings[0].ticker").isEqualTo("GOOGLE")
			.jsonPath("$.holdings[0].quantity").isEqualTo(0);
	}

	@Test
	public void customerNotFound(){
		getCustomer(100, HttpStatus.NOT_FOUND)
			.jsonPath("$.detail").isEqualTo("Customer not found with id: 100");

		var sellRequest2 = new StockTradeRequest(Ticker.GOOGLE, 100, 10, TradeAction.SELL);
		trade(100, sellRequest2, HttpStatus.NOT_FOUND)
			.jsonPath("$.detail").isEqualTo("Customer not found with id: 100");

	}

	@Test
	public void inSufficientBalance(){
		var buyRequest = new StockTradeRequest(Ticker.GOOGLE, 100, 101, TradeAction.BUY);
		trade(3, buyRequest, HttpStatus.BAD_REQUEST)
			.jsonPath("$.detail").isEqualTo("customer [id=3] does not have sufficient balance");
	}

	@Test
	public void inSufficientShare(){
		var sellRequest = new StockTradeRequest(Ticker.GOOGLE, 100, 101, TradeAction.SELL);
		trade(3, sellRequest, HttpStatus.BAD_REQUEST)
			.jsonPath("$.detail").isEqualTo("customer [id=3] does not have sufficient share");
	}

	private WebTestClient.BodyContentSpec getCustomer(Integer customerId, HttpStatus status){
		return this.webClient.get()
			.uri("/customers/" + customerId)
			.exchange()
			.expectStatus().isEqualTo(status)
			.expectBody()
			.consumeWith(e -> log.info("response: {}", new String(Objects.requireNonNull(e.getResponseBody()))));
	}

	private WebTestClient.BodyContentSpec trade(Integer customerId, StockTradeRequest request, HttpStatus status){
		return this.webClient.post()
			.uri("/customers/{customerId}/trade", customerId)
			.bodyValue(request)
			.exchange()
			.expectStatus().isEqualTo(status)
			.expectBody()
			.consumeWith(e -> log.info("response: {}", new String(Objects.requireNonNull(e.getResponseBody()))));
	}
}