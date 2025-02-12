package com.commerce.customer.service;

import org.springframework.stereotype.Service;

import com.commerce.customer.dto.CustomerInformation;
import com.commerce.customer.entity.Customer;
import com.commerce.customer.exceptions.ApplicationExceptions;
import com.commerce.customer.mapper.EntityDtoMapper;
import com.commerce.customer.repository.CustomerRepository;
import com.commerce.customer.repository.PortfolioItemRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final PortfolioItemRepository portfolioItemRepository;

	public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
		return this.customerRepository.findById(customerId)
			.switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
			.flatMap(this::buildCustomerInformation);
	}

	private Mono<CustomerInformation> buildCustomerInformation(Customer customer) {
		return this.portfolioItemRepository.findAllByCustomerId(customer.getId())
			.collectList()
			.map(list -> EntityDtoMapper.toCustomerInformation(customer, list));
	}
}
