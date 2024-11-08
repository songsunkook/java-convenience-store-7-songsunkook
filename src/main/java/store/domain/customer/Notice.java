package store.domain.customer;

import store.domain.store.Stock;

public class Notice {
    private final NoticeType noticeType;
    private final Stock target;
    private final int quantity;
    private final int needQuantityForBonus;

    public Notice(NoticeType noticeType, Stock target, int quantity, int needQuantityForBonus) {
        this.noticeType = noticeType;
        this.target = target;
        this.quantity = quantity;
        this.needQuantityForBonus = needQuantityForBonus;
    }

    public NoticeType type() {
        return noticeType;
    }

    public Stock getStock() {
        return target;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMoreQuantity() {
        return needQuantityForBonus;
    }
}
