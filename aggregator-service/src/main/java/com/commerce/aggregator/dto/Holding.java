package com.commerce.aggregator.dto;

import com.commerce.aggregator.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {
}
