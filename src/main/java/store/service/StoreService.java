package store.service;

import java.util.List;

import store.domain.customer.Customer;
import store.domain.notice.Notice;
import store.domain.store.Store;
import store.dto.NoticeResponse;
import store.dto.OrderRequest;
import store.dto.ReceiptResponse;
import store.dto.StocksResponse;
import store.repository.CustomerRepository;
import store.repository.NoticeRepository;
import store.repository.StoreRepository;
import store.util.FileParser;

public class StoreService {

    private final NoticeRepository noticeRepository = new NoticeRepository();
    private final StoreRepository storeRepository = new StoreRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    public void preSetting() {
        Store store = storeRepository.get();
        store.addPromotions(FileParser.readPromotions());
        FileParser.readStocks(store.getPromotions()).forEach(store::addStock);
        store.prepareOpen();
    }

    public void newCustomer() {
        customerRepository.reset();
    }

    public StocksResponse stocks() {
        Store store = storeRepository.get();
        return StocksResponse.of(store.getStocks());
    }

    public void order(List<OrderRequest> orders) {
        Store store = storeRepository.get();
        Customer customer = customerRepository.get();
        // validate

        orders.forEach(request -> store.buy(customer, request.name(), request.quantity()));
        noticeRepository.saveAllByCustomer(customer);
    }

    public boolean hasNotice() {
        Customer customer = customerRepository.get();
        return customer.hasNotice();
    }

    public NoticeResponse nextNotice() {
        Customer customer = customerRepository.get();
        Notice nextNotice = customer.popNotice();
        return NoticeResponse.from(nextNotice);
    }

    public void noticeAnswer(int noticeId, boolean confirm) {
        Customer customer = customerRepository.get();
        Notice notice = noticeRepository.findById(noticeId);
        customer.noticeAnswer(notice, confirm);
        noticeRepository.remove(notice);
    }

    public void membershipAnswer(boolean confirm) {
        Customer customer = customerRepository.get();
        customer.useMembership(confirm);
    }

    public ReceiptResponse receipt() {
        Customer customer = customerRepository.get();
        return ReceiptResponse.from(customer);
    }
}
