package com.commerce.aggregator.dto;

import java.time.LocalDateTime;

import com.commerce.aggregator.domain.Ticker;

public record StockPriceResponse(Ticker ticker,
								 Integer price) {
}
