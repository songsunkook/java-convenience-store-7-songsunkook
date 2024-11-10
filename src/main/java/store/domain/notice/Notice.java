package store.domain.notice;

import static store.domain.notice.NoticeType.CANT_PROMOTION_SOME_STOCKS;
import static store.domain.notice.NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY;

import store.domain.store.Stock;
import store.exception.state.InvalidNoticeTypeException;

public interface Notice {

    void save(int id);

    Integer getId();

    NoticeType getType();

    static Notice of(NoticeType noticeType, Stock stock, int quantity) {
        if (noticeType == CAN_PROMOTION_WITH_MORE_QUANTITY) {
            return new FreePromotionNotice(noticeType, stock, quantity, stock.getPromotion().getGet());
        }
        if (noticeType == CANT_PROMOTION_SOME_STOCKS) {
            return new CantPromotionNotice(noticeType, stock, quantity);
        }
        throw new InvalidNoticeTypeException();
    }
}
