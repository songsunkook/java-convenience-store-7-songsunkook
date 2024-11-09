package store.domain.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import store.domain.customer.Customer;
import store.domain.notice.NoticeType;

public class Store {

    private List<Stock> stocks = new ArrayList<>();

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public void buy(Customer customer, String name, int quantity) {
        List<Stock> targets = stocks.stream()
            .filter(stock -> stock.equalsName(name))
            .toList();

        Optional<Stock> promotioningStocks = targets.stream()
            .filter(Stock::isPromotioning)
            .findAny();

        List<Stock> notPromotioningStocks = targets.stream()
            .filter(stock -> !stock.isPromotioning())
            .toList();

        if (promotioningStocks.isPresent() && promotioningStocks.get().getQuantity() > 0) {
            Stock target = promotioningStocks.get();
            // 프로모션중인 상품 재고보다 요구 수량이 많은 경우
            if (target.getQuantity() < quantity) {
                if (!notPromotioningStocks.isEmpty()) {
                    // 일반 재고 + 프로모션 재고 보다 요청 수량이 많은 경우
                    if (promotioningStocks.stream().mapToInt(Stock::getQuantity).sum() +
                        notPromotioningStocks.stream().mapToInt(Stock::getQuantity).sum() < quantity) {
                        throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
                    }

                    int dropQuantityToNormal = quantity / target.getPromotion().buyAndGet() * target.getPromotion()
                        .buyAndGet();
                    buyStock(customer, promotioningStocks.get(), target.getQuantity() - dropQuantityToNormal,
                        0, false);
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
                customer.notice(NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY, target, quantity,
                    target.getPromotion().getGet());
                return;
            }

            Promotion promotion = promotioningStocks.get().getPromotion();
            int buyCount =
                quantity / promotion.buyAndGet() * promotion
                    .buyAndGet();
            quantity -= buyCount;
            buyStock(customer, promotioningStocks.get(), buyCount,
                buyCount / promotion.buyAndGet() * promotion.getGet()
                , true);
            if (quantity == 0) {
                return;
            }
        }

        // 프로모션 상품을 구매하고 남는건 일반 상품으로 구매
        if (notPromotioningStocks.isEmpty() ||
            notPromotioningStocks.stream().mapToInt(Stock::getQuantity).sum() < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고부족");
        }
        buyStock(customer, notPromotioningStocks.get(0), quantity, 0, false);
    }

    private void buyStock(Customer customer, Stock stock, int quantity, int bonusQuantity, boolean isPromotioning) {
        customer.order(stock, quantity, bonusQuantity, isPromotioning);
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void readyStocks() {
        List<Stock> promotioningStocks = stocks.stream()
            .filter(Stock::isPromotioning)
            .toList();
        promotioningStocks.stream()
            .filter(promotioningStock -> !containsNormalStock(promotioningStock))
            .forEach(promotioningStock -> {
                stocks.add(Stock.normalEmptyStockFrom(promotioningStock));
            });
    }

    private boolean containsNormalStock(Stock promotioningStock) {
        return findByName(promotioningStock.getName()).stream()
            .anyMatch(stock -> !stock.isPromotioning());
    }

    private List<Stock> findByName(String stockName) {
        return stocks.stream()
            .filter(stock -> Objects.equals(stock.getName(), stockName))
            .toList();
    }
}
