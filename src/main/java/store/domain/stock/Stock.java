package store.domain.stock;

import store.domain.promotion.Promotion;

public class Stock {

    private final String name;
    private int price;
    private int quantity;
    private Promotion promotion;
    private int buyQuantity;

    public Stock(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
        buyQuantity = 0;
    }

    public void buy(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
        this.quantity -= quantity;
        buyQuantity += quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int bonus() {
        return promotion.bonus(buyQuantity);
    }
}
