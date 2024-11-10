package store.domain.store;

import java.util.Objects;

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
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
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

    public boolean equalsName(String name) {
        return Objects.equals(this.name, name);
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

    public int canBonusIfMoreQuantity(int buyQuantity) {
        // 제공 가능하다면
        if (quantity >= promotion.buyAndGet()) {
            return promotion.getBuy() - buyQuantity;
        }
        return -1;
    }
}
