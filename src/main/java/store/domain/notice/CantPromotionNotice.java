package store.domain.notice;

import store.domain.store.Stock;

public class CantPromotionNotice implements Notice {

    private final NoticeType noticeType;
    private final Stock onPromotionStock;
    private final Stock noPromotionStock;
    private final int quantity;

    private Integer id;

    public CantPromotionNotice(NoticeType noticeType, Stock onPromotionStock, Stock noPromotionStock, int quantity) {
        this.noticeType = noticeType;
        this.onPromotionStock = onPromotionStock;
        this.noPromotionStock = noPromotionStock;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }
}
