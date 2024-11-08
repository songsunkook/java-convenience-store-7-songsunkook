package store.domain.notice;

import store.domain.store.Stock;

public class CantPromotionNotice implements Notice {

    private final NoticeType noticeType;
    private final Stock promotionStock;
    private final Stock normalStock;
    private final int quantity;

    private Integer id;

    public CantPromotionNotice(NoticeType noticeType, Stock promotionStock, Stock normalStock, int quantity) {
        this.noticeType = noticeType;
        this.promotionStock = promotionStock;
        this.normalStock = normalStock;
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
        return promotionStock.getName();
    }

    public Stock getPromotionStock() {
        return promotionStock;
    }

    public Stock getNormalStock() {
        return normalStock;
    }

    public int getQuantity() {
        return quantity;
    }
}
