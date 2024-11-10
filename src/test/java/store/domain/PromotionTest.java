package store.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.customer.Customer;
import store.domain.notice.NoticeType;
import store.domain.store.Promotion;
import store.domain.store.Stock;
import store.domain.store.Store;

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

    @Test
    void 프로모션_재고를_우선적으로_차감한다() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12, 31));
        Stock stock = new Stock("콜라", 1000, 10, promotion);
        Stock stock2 = new Stock("콜라", 1000, 10, null);
        Store store = new Store();
        store.addStock(stock);
        store.addStock(stock2);
        Customer customer = new Customer();
        store.buy(customer, "콜라", 3);
        assertThat(stock.getQuantity()).isEqualTo(10 - 3);
        assertThat(stock2.getQuantity()).isEqualTo(10);
    }

    @Test
    void 프로모션_재고가_부족할_경우_일반_재고를_사용한다() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12, 31));
        Stock stock = new Stock("콜라", 1000, 0, promotion);
        Stock stock2 = new Stock("콜라", 1000, 10, null);
        Store store = new Store();
        store.addStock(stock);
        store.addStock(stock2);
        Customer customer = new Customer();
        store.buy(customer, "콜라", 3);
        customer.noticeAnswer(customer.getNotices().get(0), true);
        assertThat(stock2.getQuantity()).isEqualTo(10 - 3);
    }

    @Test
    void 프로모션_적용_대상_상품보다_고객_수량이_적으면_혜택받을_수_있음을_안내한다() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12, 31));
        Stock stock = new Stock("콜라", 1000, 10, promotion);
        Store store = new Store();
        store.addStock(stock);
        Customer customer = new Customer();
        store.buy(customer, "콜라", 2);
        assertThat(customer.getNotices().size()).isEqualTo(1);
        assertThat(customer.getNotices().get(0).getType()).isEqualTo(NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY);

        // TODO: 수정. 증정의 경우에만 안내한다. 2+1 상품을 1개 가져오면 안내하면안됨. 2개 가져와야 1개 증정 여부 물음

        // 구입확정 로직. 나중에 필요할 곳이 있을 것
        // customer.answer(new Notice(NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY, stock, 0));
    }

    @Test
    void 프로모션_재고가_부족하여_일부_수량을_혜택없이_결제해야_하는_경우_정가로_결제함을_안내한다() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12, 31));
        Stock stock = new Stock("콜라", 1000, 7, promotion);
        Stock stock2 = new Stock("콜라", 1000, 10, null);
        Store store = new Store();
        store.addStock(stock);
        store.addStock(stock2);
        Customer customer = new Customer();
        store.buy(customer, "콜라", 10);
        assertThat(customer.getNotices().size()).isEqualTo(1);
        assertThat(customer.getNotices().get(0).getType()).isEqualTo(NoticeType.CANT_PROMOTION_SOME_STOCKS);
    }
}
