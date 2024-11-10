package store.domain.store;

import java.util.List;

import store.domain.customer.Customer;

public class Store {

    private final Stocks stocks = new Stocks();
    private final Promotions promotions = new Promotions();

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    /**
     * 프로모션 상품 재고보다 요구 수량이 많으면
     *  - 전체 상품 재고보다도 요구 수량이 많으면 -> 예외 반환
     *  - 프로모션 상품 재고 + 일반 상품 재고로 감당되면 -> notice
     *
     * 프로모션 상품 재고로 감당 가능되면
     *  - 증정품 안가져왔으면 -> notice (즉시구매를 하지 않는 이유는 프로모션 적용 여부가 이후 결정되기 때문)
     *  - 이상없으면 즉시 구매
     *
     * 일반 재고밖에 없으면
     *  - 일반 재고로 구매
     *  - 일반 재고로 감당이 안되면 -> 예외 반환
     */
    public void buy(Customer customer, String stockName, int stockQuantity) {
        Cashier cashier = new Cashier(stocks, customer);
        cashier.calculate(stockName, stockQuantity);
    }

    public List<Stock> getStocks() {
        return stocks.get();
    }

    public void prepareOpen() {
        stocks.prepareOpen();
    }

    public void addPromotions(List<Promotion> promotions) {
        this.promotions.addAll(promotions);
    }

    public List<Promotion> getPromotions() {
        return promotions.get();
    }
}
