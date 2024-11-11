package store.domain.customer;

import java.util.List;

import store.domain.notice.CantPromotionNotice;
import store.domain.notice.FreePromotionNotice;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;
import store.domain.store.Stock;

public class Customer {

    private static final int NO_BONUS = 0;

    private final Orders orders = new Orders();
    private final Notices notices = new Notices();
    private Membership membership = new Membership(false);

    public void notice(Notice notice) {
        notices.add(notice);
    }

    public void order(Stock stock, int quantity, int bonusQuantity, boolean onPromotion) {
        orders.add(new Order(stock, quantity, bonusQuantity, onPromotion));
        stock.buy(quantity);
    }

    public void noticeAnswer(Notice notice, boolean answer) {
        //TODO: 메서드 분리
        if (notice.getType() == NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY) {
            FreePromotionNotice formattedNotice = (FreePromotionNotice)notice;
            if (answer) {
                order(formattedNotice.getStock(),
                    formattedNotice.getQuantity() + formattedNotice.getFreeBonusQuantity(),
                    formattedNotice.getFreeBonusQuantity(), true);
                return;
            }
            order(formattedNotice.getStock(), formattedNotice.getFreeBonusQuantity(), NO_BONUS, false);
            return;
        }

        if (notice.getType() == NoticeType.CANT_PROMOTION_SOME_STOCKS) {
            CantPromotionNotice formattedNotice = (CantPromotionNotice)notice;
            if (answer) {
                int noPromotionBuyQuantity = Math.min(formattedNotice.getNoPromotionStock().getQuantity(),
                    formattedNotice.getQuantity());
                order(formattedNotice.getNoPromotionStock(), noPromotionBuyQuantity, NO_BONUS, false);
                int leftQuantity = formattedNotice.getQuantity() - noPromotionBuyQuantity;
                if (leftQuantity > 0) {
                    order(formattedNotice.getOnPromotionStock(), leftQuantity, NO_BONUS, false);
                }
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
