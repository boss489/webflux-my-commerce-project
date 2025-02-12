package com.commerce.aggregator.dto;

import com.commerce.aggregator.domain.Ticker;
import com.commerce.aggregator.domain.TradeAction;

public record StockTradeRequest(Ticker ticker,
								Integer price,
								Integer quantity,
								TradeAction action) {

}
