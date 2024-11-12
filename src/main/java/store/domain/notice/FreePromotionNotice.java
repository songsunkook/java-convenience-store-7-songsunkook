package store.domain.notice;

import store.domain.store.Stock;

public class FreePromotionNotice implements Notice {

    private final Stock target;
    private final NoticeType noticeType;
    private final int buyQuantity;
    private final int freeBonusQuantity;

    private Integer id;

    public FreePromotionNotice(NoticeType noticeType, Stock target, int buyQuantity, int freeBonusQuantity) {
        this.noticeType = noticeType;
        this.target = target;
        this.buyQuantity = buyQuantity;
        this.freeBonusQuantity = freeBonusQuantity;
    }

    @Override
    public void save(int id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public NoticeType getType() {
        return noticeType;
    }

    public Stock getStock() {
        return target;
    }

    public int getQuantity() {
        return buyQuantity;
    }

    public int getFreeBonusQuantity() {
        return freeBonusQuantity;
    }
}
