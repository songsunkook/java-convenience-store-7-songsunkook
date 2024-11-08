package store.dto;

import java.util.List;

public record ReceiptResponse(
    /**
     *
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

    public record InnerOrder(
        String name,
        int quantity,
        int price
    ) {
    }
}
