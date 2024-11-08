package store.domain.customer;

import java.util.ArrayList;
import java.util.List;

import store.domain.notice.CantPromotionNotice;
import store.domain.notice.FreeBonusNotice;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;
import store.domain.store.Stock;
import store.repository.NoticeRepository;

public class Customer {

    private final List<Order> orders = new ArrayList<>();
    private final List<FreeBonusNotice> notices = new ArrayList<>();
    private boolean membership = false;

    public void notice(NoticeType noticeType, Stock target, int quantity, int needQuantityForBonus) {
        notices.add(new FreeBonusNotice(noticeType, target, quantity, needQuantityForBonus));
    }

    public void order(Stock stock, int quantity, boolean isPromotioning) {
        orders.add(new Order(stock, quantity, isPromotioning));
        stock.buy(quantity);
    }

    public void noticeAnswer(Notice notice, boolean answer) {
        if (notice.getType() == NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY) {
            FreeBonusNotice formattedNotice = (FreeBonusNotice)notice;
            if (answer) {
                buy(formattedNotice.getStock(), formattedNotice.getQuantity() + formattedNotice.getMoreQuantity(),
                    true);
                return;
            }
            buy(formattedNotice.getStock(), formattedNotice.getMoreQuantity(), false);
        }
        if (notice.getType() == NoticeType.CANT_PROMOTION_SOME_STOCKS) {
            CantPromotionNotice formattedNotice = (CantPromotionNotice)notice;
            if (answer) {
                buy(formattedNotice.getNormalStock(), formattedNotice.getQuantity(), false);
            }
        }
    }

    private void buy(Stock formattedNotice, int formattedNotice1, boolean isPromotioning) {
        orders.add(new Order(
            formattedNotice,
            formattedNotice1,
            isPromotioning));
        formattedNotice.buy(formattedNotice1);
    }

    public void useMembership(boolean membership) {
        this.membership = membership;
    }

    public int payment() {
        int totalPrice = getTotalPrice();
        if (membership) {
            return totalPrice - getMembershipDiscount(totalPrice);
        }
        return totalPrice;
    }

    public boolean hasNotice() {
        return !notices.isEmpty();
    }

    public FreeBonusNotice popNotice() {
        return notices.removeFirst();
    }

    public List<FreeBonusNotice> getNotices() {
        return new ArrayList<>(notices);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getTotalPrice() {
        return orders.stream()
            .filter(order -> !order.isPromotioning())
            .mapToInt(Order::price)
            .sum();
    }

    public int getPromotionDiscount() {
        return orders.stream()
            .filter(Order::isPromotioning)
            .mapToInt(Order::price)
            .sum();
    }

    public int getMembershipDiscount(int totalPrice) {
        if (totalPrice * 0.3 >= 8000) {
            return 8000;
        }
        return (int)(totalPrice * 0.3);
    }

    public void flushNotices(NoticeRepository noticeRepository) {
        for (FreeBonusNotice notice : notices) {
            noticeRepository.save(notice);
        }
    }
}
