package store.domain.promotion;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import camp.nextstep.edu.missionutils.DateTimes;

class PromotionTest {

    @Test
    void 오늘_날짜가_프로모션_기간_내에_포함된_경우에만_할인을_적용한다() {
        // name,buy,get,start_date,end_date
        // 탄산2+1,2,1,2024-01-01,2024-12-31
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12, 31));
        assertThat(promotion.inProgress(DateTimes.now())).isTrue();
    }
}
