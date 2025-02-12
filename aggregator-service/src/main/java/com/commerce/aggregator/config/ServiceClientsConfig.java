package com.commerce.aggregator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.commerce.aggregator.client.CustomerServiceClient;
import com.commerce.aggregator.client.StockServiceClient;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ServiceClientsConfig {

	@Bean
	public CustomerServiceClient customerServiceClient(@Value("${customer.service.url}") String baseUrl) {
		return new CustomerServiceClient(createWebClient(baseUrl));
	}

	@Bean
	public StockServiceClient stockServiceClient(@Value("${stock.service.url}") String baseUrl) {
		return new StockServiceClient(createWebClient(baseUrl));
	}

	private WebClient createWebClient(String baseUrl) {
		log.info("Creating WebClient bean with base url: {}", baseUrl);
		return WebClient.builder()
				.baseUrl(baseUrl)
				.build();
	}

}
