package store.domain.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Stocks {

    private List<Stock> stocks = new ArrayList<>();

    public void add(Stock stock) {
        stocks.add(stock);
    }

    public void prepareOpen() {
        List<Stock> onlyPromotionStocks = stocks.stream()
            .filter(Stock::isPromotioning)
            .filter(promotioningStock -> !containsNormalStock(promotioningStock))
            .toList();
        onlyPromotionStocks.forEach(promotioningStock -> {
            int index = stocks.indexOf(promotioningStock) + 1;
            stocks.add(index, Stock.normalEmptyStockFrom(promotioningStock));
        });
    }

    private boolean containsNormalStock(Stock promotioningStock) {
        return findByName(promotioningStock.getName()).stream()
            .anyMatch(stock -> !stock.isPromotioning());
    }

    public List<Stock> findByName(String stockName) {
        return stocks.stream()
            .filter(stock -> Objects.equals(stock.getName(), stockName))
            .toList();
    }

    public List<Stock> get() {
        return stocks;
    }
}
