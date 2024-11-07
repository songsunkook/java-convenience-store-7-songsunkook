package store.domain.promotion;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.stock.Stock;

class PromotionTest {

    @Test
    void 오늘_날짜가_프로모션_기간_내에_포함된_경우에만_할인을_적용한다() {
        // name,buy,get,start_date,end_date
        // 탄산2+1,2,1,2024-01-01,2024-12-31
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12, 31));
        assertThat(promotion.inProgress(DateTimes.now())).isTrue();
    }

    @Test
    void N개_구매_시_1개_무료_증정한다() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12, 31));
        Stock stock = new Stock("콜라", 1000, 10, promotion);
        stock.buy(2);
        assertThat(stock.bonus()).isEqualTo(1);
        Stock stock2 = new Stock("콜라", 1000, 10, promotion);
        stock2.buy(5);
        assertThat(stock2.bonus()).isEqualTo(2);
    }
}
