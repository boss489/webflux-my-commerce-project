package com.commerce.aggregator.dto;

import com.commerce.aggregator.domain.Ticker;
import com.commerce.aggregator.domain.TradeAction;

public record TradeRequest(Ticker ticker,
						  TradeAction action,
						  Integer quantity
) {
}
