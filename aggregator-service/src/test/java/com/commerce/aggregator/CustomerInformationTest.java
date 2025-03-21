package com.commerce.aggregator;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerInformationTest extends AbstractIntegrationTest{


	@Test
	public void customerInformation(){
		// var responseBody = """
		// 	{
		// 		"id" : -1,
		// 		"name" : "sam"
		// 	}
		// 	""";

		//given
		mockCustomerInformation("customer-service/customer-information-200.json", 200);
		//when
		getCustomerInformation(HttpStatus.OK)
			.jsonPath("$.id").isEqualTo(1)
			.jsonPath("$.name").isEqualTo("Sam")
			.jsonPath("$.balance").isEqualTo(10000)
			.jsonPath("$.holdings").isNotEmpty();

	}

	@Test
	public void customerNotFoundInformation(){
		//given
		mockCustomerInformation("customer-service/customer-information-404.json", 404);
		//when
		getCustomerInformation(HttpStatus.NOT_FOUND)
			.jsonPath("$.detail").isEqualTo("Customer [id=1] is not found")
			.jsonPath("$.title").isNotEmpty();

	}


	private void mockCustomerInformation(String path, int responseCode){
		var responseBody = this.resourceToString(path);
		mockServerClient
			.when(HttpRequest.request("/customers/1"))
			.respond(
				HttpResponse
					.response(responseBody)
					.withStatusCode(responseCode)
					.withContentType(MediaType.APPLICATION_JSON)

			);
	}

	private WebTestClient.BodyContentSpec getCustomerInformation(HttpStatus expectedStatus){
		return this.client.get()
			.uri("/customers/1")
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectBody()
			.consumeWith(e -> log.info("thisismine" + new String(Objects.requireNonNull(e.getResponseBody()))));
	}
}
