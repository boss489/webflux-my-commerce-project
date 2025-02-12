package com.commerce.customer.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.commerce.customer.domain.Ticker;
import com.commerce.customer.entity.PortfolioItem;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {
	Flux<PortfolioItem> findAllByCustomerId(Integer customerId);
	Mono<PortfolioItem> findByCustomerIdAndTicker(Integer customerId, Ticker ticker);
}
