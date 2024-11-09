package store.domain.customer;

import java.util.ArrayList;
import java.util.List;

import store.domain.notice.CantPromotionNotice;
import store.domain.notice.FreePromotionNotice;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;
import store.domain.store.Stock;
import store.repository.NoticeRepository;

public class Customer {

    private final Orders orders = new Orders();
    private final List<Notice> notices = new ArrayList<>();
    private Membership membership = new Membership(false);

    public void notice(NoticeType noticeType, Stock target, int quantity, int needQuantityForBonus) {
        // TODO:  다른클래스로 옮기기. 알림별로 매개변수가 다름
        notices.add(new FreePromotionNotice(noticeType, target, quantity, needQuantityForBonus));
    }

    public void order(Stock stock, int quantity, int bonusQuantity, boolean isPromotioning) {
        orders.add(new Order(stock, quantity, bonusQuantity, isPromotioning));
        stock.buy(quantity);
    }

    public void noticeAnswer(Notice notice, boolean answer) {
        if (notice.getType() == NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY) {
            FreePromotionNotice formattedNotice = (FreePromotionNotice)notice;
            if (answer) {
                order(formattedNotice.getStock(), formattedNotice.getQuantity() + formattedNotice.getMoreQuantity(),
                    formattedNotice.getMoreQuantity(), true);
                return;
            }
            order(formattedNotice.getStock(), formattedNotice.getMoreQuantity(), 0, false);
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
        int totalPrice = getTotalPrice();
        return totalPrice - getMembershipDiscount() - getPromotionDiscount();
    }

    private int getPromotionTotalPrice() {
        return orders.promotionTotalPrice();
    }

    public boolean hasNotice() {
        return !notices.isEmpty();
    }

    public Notice popNotice() {
        return notices.removeFirst();
    }

    public List<Notice> getNotices() {
        return new ArrayList<>(notices);
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

    public void flushNotices(NoticeRepository noticeRepository) {
        for (Notice notice : notices) {
            noticeRepository.save(notice);
        }
    }
}
