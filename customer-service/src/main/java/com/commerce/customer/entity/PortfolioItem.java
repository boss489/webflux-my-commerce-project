package com.commerce.customer.entity;

import org.springframework.data.annotation.Id;

import com.commerce.customer.domain.Ticker;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortfolioItem {

	@Id
	private Integer id;
	private Integer customerId;
	private Ticker ticker;
	private Integer quantity;
}
