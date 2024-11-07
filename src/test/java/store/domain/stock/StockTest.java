package store.domain.stock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class StockTest {

    @Test
    void 재고가_충분하면_구매가_정상적으로_진행된다() {
        // name,price,quantity,promotion
        Stock stock = new Stock("콜라", 1000, 10, null);
        stock.buy(5);
    }

    @Test
    void 재고가_부족하면_예외를_반환한다() {
        Stock stock = new Stock("콜라", 1000, 10, null);
        assertThatThrownBy(() -> stock.buy(15))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_구매_시_결제된_수량만큼_재고에서_수량을_차감한다() {
        Stock stock = new Stock("콜라", 1000, 10, null);
        stock.buy(5);
        assertThat(stock.getQuantity()).isEqualTo(5);
    }

    @Test
    void 다음_고객이_구매할_때_정확한_재고_정보를_제공한다() {
        Stock stock = new Stock("콜라", 1000, 10, null);
        stock.buy(5);
        assertThat(stock.getQuantity()).isEqualTo(5);
    }
}
