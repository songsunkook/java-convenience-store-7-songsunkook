package store.repository;

import store.domain.store.Store;

public class StoreRepository {

    private Store store = new Store();

    public Store get() {
        return store;
    }
}
