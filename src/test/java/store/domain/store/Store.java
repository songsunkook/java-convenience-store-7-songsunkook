package store.domain.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import store.domain.stock.Stock;

public class Store {

    private List<Stock> stocks = new ArrayList<>();

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public void buy(String name, int quantity) {
        List<Stock> targets = stocks.stream()
            .filter(stock -> stock.equalsName(name))
            .toList();

        Optional<Stock> promotioning = targets.stream()
            .filter(Stock::isPromotioning)
            .findAny();

        if (promotioning.isPresent()) {
            promotioning.get().buy(quantity);
            return;
        }
        targets.get(0).buy(quantity);
    }
}
