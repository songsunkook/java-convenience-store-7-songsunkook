package store.dto;

import java.util.List;

import store.domain.store.Stock;

public record StocksResponse(
    List<Stock> stocks
) {
}
