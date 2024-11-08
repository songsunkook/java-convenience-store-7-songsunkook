package store.domain.customer;

import java.util.ArrayList;
import java.util.List;

import store.domain.store.Stock;
import store.repository.NoticeRepository;

public class Customer {

    private final List<Order> orders = new ArrayList<>();
    private final List<Notice> notices = new ArrayList<>();
    private boolean membership = false;

    public void notice(NoticeType noticeType, Stock target, int quantity, int needQuantityForBonus) {
        notices.add(new Notice(noticeType, target, quantity, needQuantityForBonus));
    }

    public void order(Stock stock, int quantity, boolean isPromotioning) {
        orders.add(new Order(stock, quantity, isPromotioning));
    }

    public void noticeAnswer(Notice notice, boolean answer) {
        if (notice.type() == NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY) {
            if (answer) {
                orders.add(new Order(notice.getStock(), notice.getQuantity() + notice.getMoreQuantity(), true));
                notice.getStock().buy(notice.getQuantity() + notice.getMoreQuantity());
                return;
            }
            orders.add(new Order(notice.getStock(), notice.getMoreQuantity(), false));
            notice.getStock().buy(notice.getMoreQuantity());
        }
        if (notice.type() == NoticeType.CANT_PROMOTION_SOME_STOCKS) {
            // TODO: BUGFIX: Stock단위로 전달하면 안됨. 프로모션 제품과 일반 제품은 재고 자체가 다름
            if (answer) {
                orders.add(new Order(notice.getStock(), notice.getQuantity() - notice.getMoreQuantity(), true));
                notice.getStock().buy(notice.getQuantity() - notice.getMoreQuantity());
                orders.add(new Order(notice.getStock(), notice.getQuantity(), false));
                notice.getStock().buy(notice.getMoreQuantity());
            }
            orders.add(new Order(notice.getStock(), notice.getQuantity() - notice.getMoreQuantity(), false));
            notice.getStock().buy(notice.getMoreQuantity());
        }
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

    public Notice popNotice() {
        return notices.removeFirst();
    }

    public List<Notice> getNotices() {
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
        for (Notice notice : notices) {
            noticeRepository.save(notice);
        }
    }
}
