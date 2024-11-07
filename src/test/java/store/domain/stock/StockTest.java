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
}
