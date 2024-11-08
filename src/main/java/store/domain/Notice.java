package store.domain;

import store.domain.stock.Stock;

public class Notice {
    private final NoticeType noticeType;
    private final Stock target;
    private final int needQuantityForBonus;

    public Notice(NoticeType noticeType, Stock target, int needQuantityForBonus) {
        this.noticeType = noticeType;
        this.target = target;
        this.needQuantityForBonus = needQuantityForBonus;
    }

    public NoticeType type() {
        return noticeType;
    }
}
