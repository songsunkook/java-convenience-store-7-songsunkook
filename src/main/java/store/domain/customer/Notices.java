package store.domain.customer;

import static store.domain.notice.NoticeType.CANT_PROMOTION_SOME_STOCKS;
import static store.domain.notice.NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY;

import java.util.ArrayList;
import java.util.List;

import store.domain.notice.CantPromotionNotice;
import store.domain.notice.FreePromotionNotice;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;
import store.domain.store.Stock;

public class Notices {

    private final List<Notice> notices = new ArrayList<>();

    public void add(NoticeType noticeType, Stock stock, int quantity) {
        if (noticeType == CAN_PROMOTION_WITH_MORE_QUANTITY) {
            notices.add(new FreePromotionNotice(noticeType, stock, quantity, stock.getPromotion().getGet()));
            return;
        }
        if (noticeType == CANT_PROMOTION_SOME_STOCKS) {
            notices.add(new CantPromotionNotice(noticeType, stock, quantity));
            return;
        }
        throw new IllegalStateException();
    }

    public boolean hasNext() {
        return !notices.isEmpty();
    }

    public Notice pop() {
        return notices.removeFirst();
    }

    public List<Notice> getNotices() {
        return new ArrayList<>(notices);
    }
}
