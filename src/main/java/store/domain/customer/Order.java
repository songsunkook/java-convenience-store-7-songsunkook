package store.domain.customer;

import store.domain.store.Stock;

public class Order {
    private final Stock stock;
    private final int quantity;
    private final boolean isPromotioning;

    public Order(Stock stock, int quantity, boolean isPromotioning) {
        this.stock = stock;
        this.quantity = quantity;
        this.isPromotioning = isPromotioning;
    }

    public int price() {
        return stock.getPrice() * quantity;
    }

    public boolean isPromotioning() {
        return isPromotioning;
    }
}
