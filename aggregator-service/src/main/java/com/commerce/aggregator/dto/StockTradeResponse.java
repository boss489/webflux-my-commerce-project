package com.commerce.aggregator.dto;

import com.commerce.aggregator.domain.Ticker;
import com.commerce.aggregator.domain.TradeAction;

public record StockTradeResponse(Integer customerId,
								 Ticker ticker,
								 Integer price,
								 Integer quantity,
								 TradeAction action,
								 Integer totalPrice,
								 Integer balance) {
}
