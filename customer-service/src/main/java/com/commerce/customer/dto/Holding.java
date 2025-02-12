package com.commerce.customer.dto;

import com.commerce.customer.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {
}
