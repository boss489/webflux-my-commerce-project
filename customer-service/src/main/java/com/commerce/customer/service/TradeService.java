package com.commerce.customer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.customer.dto.StockTradeRequest;
import com.commerce.customer.dto.StockTradeResponse;
import com.commerce.customer.entity.Customer;
import com.commerce.customer.entity.PortfolioItem;
import com.commerce.customer.exceptions.ApplicationExceptions;
import com.commerce.customer.mapper.EntityDtoMapper;
import com.commerce.customer.repository.CustomerRepository;
import com.commerce.customer.repository.PortfolioItemRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TradeService {

	private final CustomerRepository customerRepository;
	private final PortfolioItemRepository portfolioItemRepository;

	@Transactional
	public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest request) {
		return switch (request.action()){
			case BUY -> this.buyStock(customerId, request);
			case SELL -> this.sellStock(customerId, request);
		};
	}

	private Mono<StockTradeResponse> sellStock(Integer customerId, StockTradeRequest request) {
		var customerMono = this.customerRepository.findById(customerId)
			.switchIfEmpty(ApplicationExceptions.customerNotFound(customerId));

		var portfolioItemMono = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId,
				request.ticker())
			.filter(p -> p.getQuantity() >= request.quantity())
			.switchIfEmpty(ApplicationExceptions.inSufficientShareException(customerId));

			return customerMono.zipWhen(customer -> portfolioItemMono)
				.flatMap(t -> this.executeSell(t.getT1(), t.getT2(), request));
	}

	private Mono<StockTradeResponse> executeSell(Customer customer, PortfolioItem portfolioItem, StockTradeRequest request) {
		customer.setBalance(customer.getBalance() + request.totalPrice());
		portfolioItem.setQuantity(portfolioItem.getQuantity() - request.quantity());
		return saveAndBuildResponse(customer, portfolioItem, request);
	}

	private Mono<StockTradeResponse> buyStock(Integer customerId, StockTradeRequest request) {
		var customerMono = this.customerRepository.findById(customerId)
			.switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
			.filter(c -> c.getBalance() >= request.totalPrice())
			.switchIfEmpty(ApplicationExceptions.inSufficientBalance(customerId));

		var portfolioItemMono = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId,
				request.ticker())
			.defaultIfEmpty(EntityDtoMapper.toPortfolioItem(customerId, request.ticker()));

		// Mono.zip(customerMono, portfolioItemMono) //둘중에 하나가 에러가나면은 하나는 실행할 필요가 없기 때문에 zip은 불필요
		return customerMono.zipWhen(customer -> portfolioItemMono)
			.flatMap(t -> this.executeBuy(t.getT1(), t.getT2(), request));
	}

	private Mono<StockTradeResponse> executeBuy(Customer customer, PortfolioItem portfolioItem, StockTradeRequest request) {
		customer.setBalance(customer.getBalance() - request.totalPrice());
		portfolioItem.setQuantity(portfolioItem.getQuantity() + request.quantity());
		return saveAndBuildResponse(customer, portfolioItem, request);
	}

	private Mono<StockTradeResponse> saveAndBuildResponse(Customer customer, PortfolioItem portfolioItem, StockTradeRequest request) {
		var response = EntityDtoMapper.toStockTradeResponse(request, customer.getId(), customer.getBalance());
		//zip invokes the two monos in parallel and returns a mono of tuple
		return Mono.zip(this.customerRepository.save(customer), this.portfolioItemRepository.save(portfolioItem))
			.thenReturn(response);
	}
}
