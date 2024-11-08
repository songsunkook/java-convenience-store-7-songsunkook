package store.domain.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import store.domain.Customer;
import store.domain.NoticeType;
import store.domain.stock.Stock;

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

        if (promotioning.isPresent()) {
            Stock target = promotioning.get();
            // 프로모션중인 상품 재고보다 요구 수량이 많은 경우
            if (target.getQuantity() < quantity) {
                if (notPromotionings.isEmpty() ||
                    notPromotionings.stream().mapToInt(Stock::getQuantity).sum() < quantity) {
                    throw new IllegalArgumentException("[ERROR] 재고부족");
                }
                // 프로모션중이지 않은 상품이 여럿이면 합해서 buy할 수 있어야 함. 요구사항도 다시 확인해볼것
                notPromotionings.get(0).buy(quantity);
                return;
            }
            // 프로모션중인 상품 재고가 요구 수량보다 같거나 많은 경우
            if (quantity % target.getPromotion().buyAndGet() != 0) {
                int needQuantityForBonus = target.canBonusIfMoreQuantity(quantity);
                customer.notice(NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY, target, needQuantityForBonus);
                return;
            }

            promotioning.get().buy(quantity);
            return;
        }
        targets.get(0).buy(quantity);
    }
}
