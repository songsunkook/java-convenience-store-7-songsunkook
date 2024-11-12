package store.domain.notice;

import static store.domain.notice.NoticeType.CANT_PROMOTION_SOME_STOCKS;
import static store.domain.notice.NoticeType.CAN_GET_FREE_BONUS;

import store.domain.store.Stock;
import store.exception.state.InvalidNoticeTypeException;

public interface Notice {

    void save(int id);

    Integer getId();

    NoticeType getType();

    static Notice of(NoticeType noticeType, Stock stock, int quantity) {
        if (noticeType == CAN_GET_FREE_BONUS) {
            return new FreePromotionNotice(noticeType, stock, quantity, stock.getPromotion().getGet());
        }
        throw new InvalidNoticeTypeException();
    }

    static Notice of(NoticeType noticeType, Stock onPromotionStock, Stock noPromotionStock,
        int totalQuantity, int noPromotionQuantity) {
        if (noticeType == CANT_PROMOTION_SOME_STOCKS) {
            return new CantPromotionNotice(noticeType, onPromotionStock, noPromotionStock, totalQuantity, noPromotionQuantity);
        }
        throw new InvalidNoticeTypeException();
    }
}
