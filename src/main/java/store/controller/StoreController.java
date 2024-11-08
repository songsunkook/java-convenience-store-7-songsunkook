package store.controller;

import java.util.function.Supplier;

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
        do {
            process(this::preSetting);
            process(this::showStocks);
            process(this::inputOrders);
            process(this::notice);
            process(this::noticeMembership);
            process(this::receipt);
        } while (processAndGet(this::rerun));
    }

    private void preSetting() {
        storeService.preSetting();
    }

    private void showStocks() {
        outputView.welcome();
        outputView.showStocks(storeService.stocks());
    }

    private void inputOrders() {
        outputView.inputOrders();
        storeService.order(InputView.orders());
    }

    private void notice() {
        while (storeService.hasNotice()) {
            outputView.notice(storeService.nextNotice());
            storeService.noticeAnswer(InputView.confirm());
        }
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

    private boolean processAndGet(Supplier action) {
        try {
            return (boolean)action.get();
        } catch (IllegalArgumentException e) {
            outputView.exception(e);
            return processAndGet(action);
        }
    }
}
