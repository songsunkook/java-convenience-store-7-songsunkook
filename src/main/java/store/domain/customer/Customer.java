package store.domain.customer;

import java.util.List;

import store.domain.notice.CantPromotionNotice;
import store.domain.notice.FreePromotionNotice;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;
import store.domain.store.Promotion;
import store.domain.store.Stock;
import store.exception.state.InvalidNoticeTypeException;

public class Customer {

    private static final int NO_BONUS = 0;

    private final Orders orders = new Orders();
    private final Notices notices = new Notices();
    private Membership membership = new Membership(false);

    public void notice(Notice notice) {
        notices.add(notice);
    }

    public void order(Stock stock, int quantity, int bonusQuantity, boolean onPromotion) {
        if (quantity > 0) {
            orders.add(new Order(stock, quantity, bonusQuantity, onPromotion));
            stock.buy(quantity);
        }
    }

    public void answer(Notice notice, boolean answer) {
        if (notice.getType() == NoticeType.CAN_GET_FREE_BONUS) {
            answerFreePromotionNotice((FreePromotionNotice)notice, answer);
            return;
        }
        if (notice.getType() == NoticeType.CANT_PROMOTION_SOME_STOCKS) {
            answerCantPromotionNotice((CantPromotionNotice)notice, answer);
            return;
        }
        throw new InvalidNoticeTypeException();
    }

    private void answerFreePromotionNotice(FreePromotionNotice notice, boolean answer) {
        if (answer) {
            order(notice.getStock(),
                notice.getQuantity() + notice.getFreeBonusQuantity(),
                notice.getFreeBonusQuantity(), true);
            return;
        }
        order(notice.getStock(), notice.getQuantity(), NO_BONUS, false);
    }

    private void answerCantPromotionNotice(CantPromotionNotice notice, boolean answer) {
        int onPromotionBuyQuantity = buyOnPromotionStock(notice);
        int leftQuantity = notice.getOrderedTotalQuantity() - onPromotionBuyQuantity;
        if (answer) {
            buyAnyStock(notice, leftQuantity);
        }
    }

    private int buyOnPromotionStock(CantPromotionNotice notice) {
        Promotion promotion = notice.getOnPromotionStock().getPromotion();
        int possibleQuantity = Math.min(notice.getOnPromotionStock().getQuantity(),
            notice.getOrderedOnPromotionQuantity());
        int buySetCount = possibleQuantity / promotion.buyAndGet();
        int buyQuantity = Math.min(possibleQuantity, buySetCount * promotion.buyAndGet());
        int bonusQuantity = buySetCount * promotion.getGet();
        order(notice.getOnPromotionStock(), buyQuantity, bonusQuantity, true);
        return buyQuantity;
    }

    private void buyAnyStock(CantPromotionNotice notice, int leftQuantity) {
        int onPromotionQuantity = notice.getOnPromotionStock().getQuantity();
        int possibleOnPromotionQuantity = Math.min(onPromotionQuantity, leftQuantity);

        order(notice.getOnPromotionStock(), possibleOnPromotionQuantity, NO_BONUS, false);
        leftQuantity -= onPromotionQuantity;
        order(notice.getNoPromotionStock(), leftQuantity, NO_BONUS, false);
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
