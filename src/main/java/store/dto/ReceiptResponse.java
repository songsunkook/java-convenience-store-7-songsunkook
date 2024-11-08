package store.dto;

import java.util.List;

import store.domain.customer.Customer;
import store.domain.customer.Order;

public record ReceiptResponse(
    /**
     ==============W 편의점================
     상품명		수량	금액
     오렌지주스		2 	3,600
     =============증	정===============
     오렌지주스		1
     ====================================
     총구매액		2	3,600
     행사할인			-1,800
     멤버십할인			-0
     내실돈			 1,800
     */
    List<InnerOrder> orders,
    List<InnerOrder> bonusOrders,
    int totalPrice,
    int promotionDiscount,
    int membershipDiscount,
    int payment
) {

    public static ReceiptResponse from(Customer customer) {
        return new ReceiptResponse(
            customer.getOrders().stream()
                .map(InnerOrder::normalFrom)
                .toList(),
            customer.getOrders().stream()
                .filter(order -> order.getBonusQuantity() > 0)
                .map(InnerOrder::bonusFrom)
                .toList(),
            customer.getTotalPrice(),
            customer.getPromotionDiscount(),
            customer.getMembershipDiscount(customer.getTotalPrice()),
            customer.payment()
        );
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
    }
}
