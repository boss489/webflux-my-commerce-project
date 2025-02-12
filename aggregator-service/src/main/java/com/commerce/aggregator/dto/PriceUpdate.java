package com.commerce.aggregator.dto;

import java.time.LocalDateTime;

import com.commerce.aggregator.domain.Ticker;

public record PriceUpdate(Ticker ticker,
						  Integer price,
						  LocalDateTime time) {
}
