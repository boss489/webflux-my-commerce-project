package com.commerce.customer.dto;

import com.commerce.customer.domain.Ticker;
import com.commerce.customer.domain.TradeAction;

public record StockTradeResponse(Integer customerId,
								 Ticker ticker,
								 Integer price,
								 Integer quantity,
								 TradeAction action,
								 Integer totalPrice,
								 Integer balance) {
}
