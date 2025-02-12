package com.commerce.customer.dto;

import com.commerce.customer.domain.Ticker;
import com.commerce.customer.domain.TradeAction;

public record StockTradeRequest(Ticker ticker,
								Integer price,
								Integer quantity,
								TradeAction action) {

	public Integer totalPrice() {
		return price() * quantity();
	}
}
