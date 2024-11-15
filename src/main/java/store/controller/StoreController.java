package store.controller;

import java.util.function.Consumer;
import java.util.function.Supplier;

import store.dto.NoticeResponse;
import store.repository.CustomerRepository;
import store.repository.NoticeRepository;
import store.repository.StoreRepository;
import store.service.StoreService;
import store.util.TransactionTemplate;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final OutputView outputView;
    private final StoreService storeService;
    private final TransactionTemplate transactionTemplate;
    private final StoreRepository storeRepository = new StoreRepository();
    private final NoticeRepository noticeRepository = new NoticeRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    public StoreController(OutputView outputView, StoreService storeService, TransactionTemplate transactionTemplate) {
        this.outputView = outputView;
        this.storeService = storeService;
        this.transactionTemplate = transactionTemplate;
    }

    public void run() {
        process(this::preSetting);
        do {
            process(this::newCustomer);
            process(this::showStocks);
            process(this::order);
            process(this::notice);
            process(this::noticeMembership);
            process(this::receipt);
        } while (processAndGet(this::rerun));
        InputView.close();
    }

    private void preSetting() {
        transactionTemplate.addTarget(storeRepository);
        transactionTemplate.addTarget(noticeRepository);
        transactionTemplate.addTarget(customerRepository);
        storeService.preSetting();
    }

    private void newCustomer() {
        storeService.newCustomer();
    }

    private void showStocks() {
        outputView.welcome();
        outputView.showStocks(storeService.stocks());
    }

    private void order() {
        outputView.inputOrders();
        storeService.order(InputView.orders());
    }

    private void notice() {
        while (storeService.hasNotice()) {
            NoticeResponse response = storeService.nextNotice();
            process(this::processSingleNotice, response);
        }
    }

    private void processSingleNotice(NoticeResponse response) {
        outputView.notice(response);
        storeService.noticeAnswer(response.id(), InputView.confirm());
    }

    private void noticeMembership() {
        outputView.noticeMembership();
        storeService.membershipAnswer(InputView.confirm());
    }

    private void receipt() {
        outputView.receipt(storeService.receipt());
    }

    private boolean rerun() {
        outputView.inputRerun();
        return InputView.confirm();
    }

    private void process(Runnable action) {
        try {
            transactionTemplate.execute(action);
        } catch (IllegalArgumentException e) {
            outputView.exception(e);
            process(action);
        }
    }

    private <T> void process(Consumer<T> action, T arg) {
        try {
            transactionTemplate.execute(action, arg);
        } catch (IllegalArgumentException e) {
            outputView.exception(e);
            process(action, arg);
        }
    }

    private boolean processAndGet(Supplier<Boolean> action) {
        try {
            return transactionTemplate.executeAndGet(action);
        } catch (IllegalArgumentException e) {
            outputView.exception(e);
            return processAndGet(action);
        }
    }
}
