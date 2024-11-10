package store.controller;

import java.util.function.Consumer;
import java.util.function.Supplier;

import store.dto.NoticeResponse;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final OutputView outputView;
    private final StoreService storeService;

    public StoreController(OutputView outputView, StoreService storeService) {
        this.outputView = outputView;
        this.storeService = storeService;
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
            action.run();
        } catch (IllegalArgumentException e) {
            outputView.exception(e);
            process(action);
        }
    }

    private <T> void process(Consumer<T> action, T arg) {
        try {
            action.accept(arg);
        } catch (IllegalArgumentException e) {
            outputView.exception(e);
            process(action, arg);
        }
    }

    private boolean processAndGet(Supplier action) {
        try {
            return (boolean)action.get();
        } catch (IllegalArgumentException e) {
            outputView.exception(e);
            return processAndGet(action);
        }
    }
}
