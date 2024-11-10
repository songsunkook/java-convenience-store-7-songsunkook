package store.domain.store;

import camp.nextstep.edu.missionutils.DateTimes;

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

    public static Stock normalEmptyStockFrom(Stock promotioningStock) {
        return new Stock(promotioningStock.name, promotioningStock.price, 0, null);
    }

    public void buy(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalStateException("재고 부족한 상황에 초과된 요청");
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
            throw new IllegalStateException("프로모션중이지 않은 재고에 대한 증정품 요청");
        }
        return promotion.bonus(buyQuantity);
    }

    public boolean isPromotioning() {
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
