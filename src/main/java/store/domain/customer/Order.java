package store.domain.customer;

import store.domain.store.Stock;

public class Order {
    private final Stock stock;
    private final int quantity;
    private final int bonusQuantity;
    private final boolean onPromotion;

    public Order(Stock stock, int quantity, int bonusQuantity, boolean onPromotion) {
        this.stock = stock;
        this.quantity = quantity;
        this.bonusQuantity = bonusQuantity;
        this.onPromotion = onPromotion;
    }

    public int price() {
        return stock.getPrice();
    }

    public boolean getOnPromotion() {
        return onPromotion;
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
