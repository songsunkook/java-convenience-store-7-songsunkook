package store.domain.store;

import java.util.List;

import store.domain.customer.Customer;

public class Store {

    private final Stocks stocks = new Stocks();
    private final Promotions promotions = new Promotions();

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public void validateStocks(String name, int quantity) {
        stocks.validate(name, quantity);
    }

    public void buy(Customer customer, String stockName, int stockQuantity) {
        Cashier cashier = new Cashier(stocks, customer);
        cashier.calculate(stockName, stockQuantity);
    }

    public List<Stock> getStocks() {
        return stocks.get();
    }

    public void prepareOpen() {
        stocks.prepareOpen();
    }

    public void addPromotions(List<Promotion> promotions) {
        this.promotions.addAll(promotions);
    }

    public List<Promotion> getPromotions() {
        return promotions.get();
    }
}
