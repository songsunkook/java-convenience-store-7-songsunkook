package store;

import store.controller.StoreController;
import store.service.StoreService;
import store.util.TransactionTemplate;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = new StoreController(
            new OutputView(),
            new StoreService(),
            TransactionTemplate.getInstance());
        storeController.run();
    }
}
