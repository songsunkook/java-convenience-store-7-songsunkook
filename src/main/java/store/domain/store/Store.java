package store.domain.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import store.domain.customer.Customer;
import store.domain.customer.NoticeType;

public class Store {

    private List<Stock> stocks = new ArrayList<>();

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public void buy(Customer customer, String name, int quantity) {
        List<Stock> targets = stocks.stream()
            .filter(stock -> stock.equalsName(name))
            .toList();

        Optional<Stock> promotioning = targets.stream()
            .filter(Stock::isPromotioning)
            .findAny();

        List<Stock> notPromotionings = targets.stream()
            .filter(stock -> !stock.isPromotioning())
            .toList();

        if (promotioning.isPresent() && promotioning.get().getQuantity() > 0) {
            Stock target = promotioning.get();
            // 프로모션중인 상품 재고보다 요구 수량이 많은 경우
            if (target.getQuantity() < quantity) {
                if (!notPromotionings.isEmpty()) {
                    int dropQuantityToNormal = quantity / target.getPromotion().buyAndGet() * target.getPromotion()
                        .buyAndGet();
                    customer.notice(NoticeType.CANT_PROMOTION_SOME_STOCKS, target, quantity, dropQuantityToNormal);
                    return;

                    // TODO: 일반 재고 + 프로모션 재고 보다 요청 수량이 많으면 안됨
                    // 프로모션중이지 않은 상품이 여럿이면 합해서 buy할 수 있어야 함. 요구사항도 다시 확인해볼것
                    // notPromotionings.get(0).buy(quantity);
                    // return;
                }
                throw new IllegalArgumentException("[ERROR] 재고부족");
            }
            // 프로모션 대상인데 증정품을 안가져온 경우
            int notPromotionCount = quantity % target.getPromotion().buyAndGet();
            if (notPromotionCount == target.getPromotion().getBuy()) {
                customer.notice(NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY, target, quantity, notPromotionCount);
                return;
            }

            int buyCount =
                quantity / promotioning.get().getPromotion().buyAndGet() * promotioning.get()
                    .getPromotion()
                    .buyAndGet();
            quantity -= buyCount;
            promotioning.get().buy(buyCount);
            buyStock(customer, promotioning.get(), buyCount, true);
            if (quantity == 0) {
                return;
            }
        }

        // 프로모션 상품을 구매하고 남는건 일반 상품으로 구매
        if (notPromotionings.isEmpty() ||
            notPromotionings.stream().mapToInt(Stock::getQuantity).sum() < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고부족");
        }
        notPromotionings.get(0).buy(quantity);
        buyStock(customer, notPromotionings.get(0), quantity, false);
    }

    private void buyStock(Customer customer, Stock stock, int quantity, boolean isPromotioning) {
        customer.order(stock, quantity, isPromotioning);
    }

    public List<Stock> getStocks() {
        return stocks;
    }
}
