package store.dto;

import java.util.List;

import store.domain.store.Stock;

public record StocksResponse(
    List<InnerStock> stocks
) {

    private static final String NON_QUANTITY = "재고 없음";
    private static final String QUANTITY_UNIT = "개";
    private static final String PROMOTION_DEFAULT_TEXT = "";

    public static StocksResponse of(List<Stock> stocks) {
        return new StocksResponse(
            stocks.stream()
                .map(InnerStock::from)
                .toList()
        );
    }

    public record InnerStock(
        String name,
        int price,
        int quantity,
        String promotion
    ) {

        private static InnerStock from(Stock stock) {
            String promotion = PROMOTION_DEFAULT_TEXT;
            if (stock.getPromotion() != null) {
                promotion = stock.getPromotion().getName();
            }
            return new InnerStock(stock.getName(), stock.getPrice(), stock.getQuantity(), promotion);
        }

        public String outputQuantity() {
            if (quantity == 0) {
                return NON_QUANTITY;
            }
            return quantity + QUANTITY_UNIT;
        }
    }
}
