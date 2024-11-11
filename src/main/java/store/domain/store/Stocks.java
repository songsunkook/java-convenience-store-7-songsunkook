package store.domain.store;

import static store.constant.StoreConstant.MINIMUM_STOCK_QUANTITY;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import store.exception.argument.OverStockQuantityException;
import store.exception.argument.QuantityOutOfRangeException;
import store.exception.argument.StockNotFoundException;

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

    public void validate(String name, int quantity) {
        validateStockName(name);
        validateOutOfRangeQuantity(quantity);
        validateRequestOverTotalQuantity(name, quantity);
    }

    private void validateStockName(String name) {
        if (findByName(name).isEmpty()) {
            throw new StockNotFoundException();
        }
    }

    private static void validateOutOfRangeQuantity(int quantity) {
        if (quantity < MINIMUM_STOCK_QUANTITY) {
            throw new QuantityOutOfRangeException();
        }
    }

    private void validateRequestOverTotalQuantity(String name, int requestQuantity) {
        int totalQuantity = findByName(name).stream()
            .mapToInt(Stock::getQuantity)
            .sum();
        if (totalQuantity < requestQuantity) {
            throw new OverStockQuantityException();
        }
    }

    public Stock findByNameAndPromotion(String name, boolean onPromotion) {
        return findByName(name).stream()
            .filter(stock -> stock.onPromotion() == onPromotion)
            .findAny()
            .orElse(null);
    }
}
