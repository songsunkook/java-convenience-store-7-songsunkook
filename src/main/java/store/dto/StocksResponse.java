package store.dto;

import java.util.List;

import store.domain.store.Stock;

public record StocksResponse(
    List<InnerStock> stocks
) {

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
            String promotion = "";
            if (stock.getPromotion() != null) {
                promotion = stock.getPromotion().getName();
            }
            return new InnerStock(stock.getName(), stock.getPrice(), stock.getQuantity(), promotion);
        }

        public String outputQuantity() {
            if (quantity == 0) {
                return "재고 없음";
            }
            return String.valueOf(quantity);
        }
    }
}
