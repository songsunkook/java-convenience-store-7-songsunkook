package store.domain.store;

import camp.nextstep.edu.missionutils.DateTimes;
import store.exception.argument.OverStockQuantityException;
import store.exception.state.RequestBonusForNoPromotionStock;

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

    public static Stock normalEmptyStockFrom(Stock onPromotion) {
        return new Stock(onPromotion.name, onPromotion.price, 0, null);
    }

    public void buy(int quantity) {
        if (this.quantity < quantity) {
            throw new OverStockQuantityException();
        }
        this.quantity -= quantity;
        buyQuantity += quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int bonus() {
        if (promotion == null) {
            throw new RequestBonusForNoPromotionStock();
        }
        return promotion.bonus(buyQuantity);
    }

    public boolean onPromotion() {
        if (promotion == null) {
            return false;
        }
        return promotion.inProgress(DateTimes.now());
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getPrice() {
        return price;
    }

    public boolean isEmpty() {
        return quantity == 0;
    }
}
