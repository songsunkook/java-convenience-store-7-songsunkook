package store.domain.store;

import static store.domain.notice.NoticeType.CANT_PROMOTION_SOME_STOCKS;
import static store.domain.notice.NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import store.domain.customer.Customer;

public class Store {

    private List<Stock> stocks = new ArrayList<>();

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public void buy(Customer customer, String stockName, int stockQuantity) {
        List<Stock> targets = findStocksByName(stockName);
        Optional<Stock> promotioningStock = findStockInStocksByPromotionIs(targets, true);
        Optional<Stock> notPromotioningStock = findStockInStocksByPromotionIs(targets, false);

        if (promotioningStock.isPresent() && promotioningStock.get().getQuantity() > 0) {
            Stock target = promotioningStock.get();
            // 프로모션중인 상품 재고보다 요구 수량이 많은 경우
            if (target.getQuantity() < stockQuantity) {
                if (!notPromotioningStock.isEmpty()) {
                    // 일반 재고 + 프로모션 재고 보다 요청 수량이 많은 경우
                    if (promotioningStock.stream().mapToInt(Stock::getQuantity).sum() +
                        notPromotioningStock.stream().mapToInt(Stock::getQuantity).sum() < stockQuantity) {
                        throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
                    }

                    int dropQuantityToNormal = stockQuantity / target.getPromotion().buyAndGet() * target.getPromotion()
                        .buyAndGet();
                    buyStock(customer, promotioningStock.get(), target.getQuantity() - dropQuantityToNormal,
                        0, false);
                    customer.notice(CANT_PROMOTION_SOME_STOCKS, target, dropQuantityToNormal);
                    return;
                }
                throw new IllegalArgumentException("[ERROR] 재고부족");
            }
            // 프로모션 대상인데 증정품을 안가져온 경우
            int notPromotionCount = stockQuantity % target.getPromotion().buyAndGet();
            if (notPromotionCount == target.getPromotion().getBuy()) {
                customer.notice(CAN_PROMOTION_WITH_MORE_QUANTITY, target, stockQuantity);
                return;
            }

            Promotion promotion = promotioningStock.get().getPromotion();
            int buyCount =
                stockQuantity / promotion.buyAndGet() * promotion
                    .buyAndGet();
            stockQuantity -= buyCount;
            buyStock(customer, promotioningStock.get(), buyCount,
                buyCount / promotion.buyAndGet() * promotion.getGet()
                , true);
            if (stockQuantity == 0) {
                return;
            }
        }

        // 프로모션 상품을 구매하고 남는건 일반 상품으로 구매
        if (notPromotioningStock.isEmpty() ||
            notPromotioningStock.stream().mapToInt(Stock::getQuantity).sum() < stockQuantity) {
            throw new IllegalArgumentException("[ERROR] 재고부족");
        }
        buyStock(customer, notPromotioningStock.get(), stockQuantity, 0, false);
    }

    private List<Stock> findStocksByName(String stockName) {
        return stocks.stream()
            .filter(stock -> stock.equalsName(stockName))
            .toList();
    }

    private Optional<Stock> findStockInStocksByPromotionIs(List<Stock> targets, boolean promotioning) {
        return targets.stream()
            .filter(stock -> promotioning == stock.isPromotioning())
            .findAny();
    }

    private void buyStock(Customer customer, Stock stock, int quantity, int bonusQuantity, boolean isPromotioning) {
        customer.order(stock, quantity, bonusQuantity, isPromotioning);
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void readyStocks() {
        List<Stock> onlyPromotionStocks = stocks.stream()
            .filter(Stock::isPromotioning)
            .filter(promotioningStock -> !containsNormalStock(promotioningStock))
            .toList();
        onlyPromotionStocks.forEach(promotioningStock ->
            stocks.add(Stock.normalEmptyStockFrom(promotioningStock)));
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
