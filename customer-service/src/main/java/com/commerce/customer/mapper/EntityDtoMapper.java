package com.commerce.customer.mapper;

import java.util.List;

import com.commerce.customer.domain.Ticker;
import com.commerce.customer.dto.CustomerInformation;
import com.commerce.customer.dto.Holding;
import com.commerce.customer.dto.StockTradeRequest;
import com.commerce.customer.dto.StockTradeResponse;
import com.commerce.customer.entity.Customer;
import com.commerce.customer.entity.PortfolioItem;

public class EntityDtoMapper {

	public static CustomerInformation toCustomerInformation(Customer customer, List<PortfolioItem> items) {
		var holdings = items.stream()
			.map(i -> new Holding(i.getTicker(), i.getQuantity())).toList();

		return new CustomerInformation(customer.getId(), customer.getName(), customer.getBalance(), holdings);
	}

	public static PortfolioItem toPortfolioItem(Integer customerId, Ticker ticker) {
		var item = new PortfolioItem();
		item.setCustomerId(customerId);
		item.setTicker(ticker);
		item.setQuantity(0);
		return item;
	}

	public static StockTradeResponse toStockTradeResponse(StockTradeRequest request, Integer customerId, Integer balance) {
		return new StockTradeResponse(customerId,
			request.ticker(),
			request.price(),
			request.quantity(),
			request.action(),
			request.totalPrice(),
			balance);
	}
}
