package store.domain.customer;

import java.util.List;

import store.domain.notice.CantPromotionNotice;
import store.domain.notice.FreePromotionNotice;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;
import store.domain.store.Stock;

public class Customer {

    private final Orders orders = new Orders();
    private final Notices notices = new Notices();
    private Membership membership = new Membership(false);

    public void notice(NoticeType noticeType, Stock target, int quantity) {
        notices.add(noticeType, target, quantity);
    }

    public void order(Stock stock, int quantity, int bonusQuantity, boolean isPromotioning) {
        orders.add(new Order(stock, quantity, bonusQuantity, isPromotioning));
        stock.buy(quantity);
    }

    public void noticeAnswer(Notice notice, boolean answer) {
        //TODO: 메서드 분리
        if (notice.getType() == NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY) {
            FreePromotionNotice formattedNotice = (FreePromotionNotice)notice;
            if (answer) {
                order(formattedNotice.getStock(), formattedNotice.getQuantity() + formattedNotice.getFreeBonusQuantity(),
                    formattedNotice.getFreeBonusQuantity(), true);
                return;
            }
            order(formattedNotice.getStock(), formattedNotice.getFreeBonusQuantity(), 0, false);
            return;
        }

        if (notice.getType() == NoticeType.CANT_PROMOTION_SOME_STOCKS) {
            CantPromotionNotice formattedNotice = (CantPromotionNotice)notice;
            if (answer) {
                order(formattedNotice.getNormalStock(), formattedNotice.getQuantity(), 0, false);
            }
        }
    }

    public void useMembership(boolean membership) {
        this.membership = this.membership.use(membership);
    }

    public int payment() {
        return getTotalPrice() - getMembershipDiscount() - getPromotionDiscount();
    }

    private int getPromotionTotalPrice() {
        return orders.promotionTotalPrice();
    }

    public boolean hasNotice() {
        return notices.hasNext();
    }

    public Notice popNotice() {
        return notices.pop();
    }

    public List<Notice> getNotices() {
        return notices.getNotices();
    }

    public List<Order> getOrders() {
        return orders.getOrders();
    }

    public int getTotalPrice() {
        return orders.totalPrice();
    }

    public int getPromotionDiscount() {
        return orders.bonusDiscount();
    }

    public int getMembershipDiscount() {
        return membership.getDiscount(getTotalPrice() - getPromotionTotalPrice());
    }
}
