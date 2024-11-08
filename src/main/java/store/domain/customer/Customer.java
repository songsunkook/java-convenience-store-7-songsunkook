package store.domain.customer;

import java.util.ArrayList;
import java.util.List;

import store.domain.stock.Stock;

public class Customer {

    private final List<Order> boughtStocks = new ArrayList<>();
    private final List<Notice> notices = new ArrayList<>();
    private boolean membership = false;

    public List<Notice> getNotices() {
        return new ArrayList<>(notices);
    }

    public void notice(NoticeType noticeType, Stock target, int needQuantityForBonus) {
        notices.add(new Notice(noticeType, target, needQuantityForBonus));
    }

    public void order(Stock stock, int quantity, boolean isPromotioning) {
        boughtStocks.add(new Order(stock, quantity, isPromotioning));
    }

    public void useMembership(boolean membership) {
        this.membership = membership;
    }

    public int payment() {
        int totalCost = boughtStocks.stream()
            .filter(order -> !order.isPromotioning())
            .mapToInt(Order::price)
            .sum();
        if (membership) {
            return (int)(totalCost * 0.7);
        }
        return totalCost;
    }
}
