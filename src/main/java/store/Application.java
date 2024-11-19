package store;

import store.controller.StoreController;
import store.util.TransactionTemplate;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = new StoreController(
            new OutputView(),
            TransactionTemplate.getInstance());
        storeController.run();
    }
}
