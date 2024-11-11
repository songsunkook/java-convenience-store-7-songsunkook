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
            .filter(Stock::onPromotion)
            .filter(onPromotionStock -> !containsNormalStock(onPromotionStock))
            .toList();
        onlyPromotionStocks.forEach(onPromotionStock -> {
            int index = stocks.indexOf(onPromotionStock) + 1;
            stocks.add(index, Stock.normalEmptyStockFrom(onPromotionStock));
        });
    }

    private boolean containsNormalStock(Stock onPromotionStock) {
        return findByName(onPromotionStock.getName()).stream()
            .anyMatch(stock -> !stock.onPromotion());
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
