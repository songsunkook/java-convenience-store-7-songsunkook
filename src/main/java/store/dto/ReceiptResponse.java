package store.dto;

import java.util.List;
import java.util.stream.Collectors;

import store.domain.customer.Customer;
import store.domain.customer.Order;

public record ReceiptResponse(
    List<InnerOrder> orders,
    List<InnerOrder> bonusOrders,
    int totalPrice,
    int promotionDiscount,
    int membershipDiscount,
    int payment
) {

    public static ReceiptResponse from(Customer customer) {
        return new ReceiptResponse(
            getOrders(customer),
            getBonusOrders(customer),
            customer.getTotalPrice(),
            customer.getPromotionDiscount(),
            customer.getMembershipDiscount(),
            customer.payment()
        );
    }

    private static List<InnerOrder> getOrders(Customer customer) {
        return customer.getOrders().stream()
            .map(InnerOrder::normalFrom)
            .collect(Collectors.toMap(
                InnerOrder::name,
                innerOrder -> innerOrder,
                InnerOrder::combineOrders
            )).values().stream()
            .toList();
    }

    private static List<InnerOrder> getBonusOrders(Customer customer) {
        return customer.getOrders().stream()
            .filter(order -> order.getBonusQuantity() > 0)
            .map(InnerOrder::bonusFrom)
            .toList();
    }

    public record InnerOrder(
        String name,
        int quantity,
        int price
    ) {

        public static InnerOrder normalFrom(Order order) {
            return new InnerOrder(
                order.getStock().getName(),
                order.getQuantity(),
                order.price() * order.getQuantity()
            );
        }

        public static InnerOrder bonusFrom(Order order) {
            return new InnerOrder(
                order.getStock().getName(),
                order.getBonusQuantity(),
                order.price() * order.getBonusQuantity()
            );
        }

        public static InnerOrder combineOrders(InnerOrder o1, InnerOrder o2) {
            return new InnerOrder(
                o1.name,
                o1.quantity + o2.quantity,
                o1.price + o2.price
            );
        }
    }
}
