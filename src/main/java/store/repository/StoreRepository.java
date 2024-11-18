package store.repository;

import java.util.Collection;
import java.util.List;

import store.domain.store.Store;

public class StoreRepository implements Repository<Store> {

    private Store store = new Store();

    public Store get() {
        return store;
    }

    @Override
    public Collection<Store> getCopiedCollection() {
        return List.of(Store.copyOf(store));
    }

    @Override
    public void clear() {
        store = null;
    }

    @Override
    public void addAll(Collection<Store> collection) {
        if (collection != null && !collection.isEmpty()) {
            store = collection.iterator().next();
        }
    }
}
