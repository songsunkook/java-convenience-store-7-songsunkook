package store.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import store.domain.customer.Customer;
import store.domain.store.Promotion;
import store.domain.store.Stock;
import store.domain.store.Store;

class MembershipTest {

    @Test
    void 결제_금액의_30퍼센트를_할인한다() {
        Stock stock = new Stock("콜라", 1000, 10, null);
        Store store = new Store();
        store.addStock(stock);
        Customer customer = new Customer();
        store.buy(customer, "콜라", 10);
        customer.useMembership(true);
        assertThat(customer.payment()).isEqualTo((int)(1000 * 10 * 0.7));
    }

    @Test
    void 프로모션_미적용_금액에_대해_할인한다() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12, 31));
        Stock noPromotionStock = new Stock("아이스크림", 1000, 10, null);
        Stock promotionStock = new Stock("콜라", 1000, 10, promotion);
        Store store = new Store();
        store.addStock(noPromotionStock);
        store.addStock(promotionStock);
        Customer customer = new Customer();
        store.buy(customer, "콜라", 9);
        store.buy(customer, "아이스크림", 3);
        customer.useMembership(true);
        assertThat(customer.payment()).isEqualTo((int)(1000 * 6 + 1000 * 3 * 0.7));
    }

    @Test
    void 프로모션_적용_후_남은_금액에_대해_할인한다() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12, 31));
        Stock promotionStock = new Stock("콜라", 1000, 10, promotion);
        Stock noPromotionStock = new Stock("콜라", 1000, 10, null);
        Store store = new Store();
        store.addStock(promotionStock);
        store.addStock(noPromotionStock);
        Customer customer = new Customer();
        store.buy(customer, "콜라", 4);
        customer.useMembership(true);
        assertThat(customer.payment()).isEqualTo((int)(1000 * 2 + 1000 * 0.7));
    }

    @Test
    void 멤버십_할인의_최대_한도는_8000원이다() {
        Stock noPromotionStock = new Stock("콜라", 1000, 100, null);
        Store store = new Store();
        store.addStock(noPromotionStock);
        Customer customer = new Customer();
        store.buy(customer, "콜라", 100);
        customer.useMembership(true);
        assertThat(customer.payment()).isEqualTo((int)(1000 * 100 - 8000));
    }
}
