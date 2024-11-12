package store.domain.notice;

import store.domain.store.Stock;

public class CantPromotionNotice implements Notice {

    private final NoticeType noticeType;
    private final Stock onPromotionStock;
    private final Stock noPromotionStock;
    private final int orderedTotalQuantity;
    private final int orderedNoPromotionQuantity;

    private Integer id;

    public CantPromotionNotice(NoticeType noticeType, Stock onPromotionStock, Stock noPromotionStock, int orderedTotalQuantity,
        int orderedNoPromotionQuantity) {
        this.noticeType = noticeType;
        this.onPromotionStock = onPromotionStock;
        this.noPromotionStock = noPromotionStock;
        this.orderedTotalQuantity = orderedTotalQuantity;
        this.orderedNoPromotionQuantity = orderedNoPromotionQuantity;
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

    public String getStockName() {
        return noPromotionStock.getName();
    }

    public Stock getNoPromotionStock() {
        return noPromotionStock;
    }

    public Stock getOnPromotionStock() {
        return onPromotionStock;
    }

    public int getOrderedTotalQuantity() {
        return orderedTotalQuantity;
    }

    public int getOrderedNoPromotionQuantity() {
        return orderedNoPromotionQuantity;
    }

    public int getOrderedOnPromotionQuantity() {
        return orderedTotalQuantity - orderedNoPromotionQuantity;
    }
}
