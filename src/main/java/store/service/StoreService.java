package store.service;

import java.util.List;

import store.domain.customer.Customer;
import store.domain.notice.Notice;
import store.domain.store.Promotion;
import store.domain.store.Store;
import store.dto.NoticeResponse;
import store.dto.OrderRequest;
import store.dto.ReceiptResponse;
import store.dto.StocksResponse;
import store.repository.NoticeRepository;
import store.util.FileParser;

public class StoreService {

    private final NoticeRepository noticeRepository = new NoticeRepository();

    private Customer customer = new Customer();
    private Store store = new Store();
    private List<Promotion> promotions;

    public void preSetting() {
        promotions = FileParser.readPromotions();
        FileParser.readProducts(promotions).forEach(store::addStock);
    }

    public StocksResponse stocks() {
        return new StocksResponse(store.getStocks());
    }

    public void order(List<OrderRequest> orders) {
        orders.forEach(request -> store.buy(customer, request.name(), request.quantity()));
        customer.flushNotices(noticeRepository);
    }

    public boolean hasNotice() {
        return customer.hasNotice();
    }

    public NoticeResponse nextNotice() {
        Notice nextNotice = customer.popNotice();
        return NoticeResponse.from(nextNotice);
    }

    public void noticeAnswer(int noticeId, boolean confirm) {
        Notice notice = noticeRepository.findById(noticeId);
        customer.noticeAnswer(notice, confirm);
        noticeRepository.remove(notice);
    }

    public void membershipAnswer(boolean confirm) {
        customer.useMembership(confirm);
    }

    public ReceiptResponse receipt() {
        return new ReceiptResponse(
            customer.getOrders().stream()
                .filter(order -> !order.isPromotioning())
                .map(order -> new ReceiptResponse.InnerOrder(
                    order.getStock().getName(),
                    order.getQuantity(),
                    order.price()
                )).toList(),
            customer.getOrders().stream()
                .filter(order -> order.isPromotioning())
                .map(order -> new ReceiptResponse.InnerOrder(
                    order.getStock().getName(),
                    order.getQuantity(),
                    order.price()
                )).toList(),
            customer.getTotalPrice(),
            customer.getPromotionDiscount(),
            customer.getMembershipDiscount(customer.getTotalPrice()),
            customer.payment()
        );
    }
}
