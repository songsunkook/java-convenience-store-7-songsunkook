package store.domain.customer;

import store.domain.store.Stock;

public class Order {
    private final Stock stock;
    private final int quantity;
    private final int bonusQuantity;
    private final boolean isPromotioning;

    public Order(Stock stock, int quantity, int bonusQuantity, boolean isPromotioning) {
        this.stock = stock;
        this.quantity = quantity;
        this.bonusQuantity = bonusQuantity;
        this.isPromotioning = isPromotioning;
    }

    public int price() {
        return stock.getPrice();
    }

    public boolean isPromotioning() {
        return isPromotioning;
    }

    public int getBonusQuantity() {
        return bonusQuantity;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }
}
