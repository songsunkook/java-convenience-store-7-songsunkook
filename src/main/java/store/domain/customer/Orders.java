package store.domain.customer;

import java.util.ArrayList;
import java.util.List;

public class Orders {

    private final List<Order> orders = new ArrayList<>();

    public void add(Order order) {
        orders.add(order);
    }

    public int bonusDiscount() {
        return orders.stream()
            .mapToInt(order -> order.getBonusQuantity() * order.price())
            .sum();
    }

    public int promotionTotalPrice() {
        return orders.stream()
            .filter(Order::getOnPromotion)
            .mapToInt(order -> order.getQuantity() * order.price())
            .sum();
    }

    public int totalPrice() {
        return orders.stream()
            .mapToInt(order -> order.price() * order.getQuantity())
            .sum();
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}
