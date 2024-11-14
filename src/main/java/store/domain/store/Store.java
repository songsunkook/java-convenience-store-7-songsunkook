package store.domain.store;

import java.util.List;

import store.domain.customer.Customer;

public class Store implements Cloneable {

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

    @Override
    public Store clone() {
        try {
            Store clone = (Store)super.clone();
            stocks.get().forEach(stock -> {
                // TODO: Stock.clone으로 이관
                Stock clonedStock = new Stock(
                    stock.getName(),
                    stock.getPrice(),
                    stock.getQuantity(),
                    stock.getPromotion()
                );
                addStock(clonedStock);
            });
            // TODO: 불변이긴해도 clone 이관
            clone.addPromotions(promotions.get());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
