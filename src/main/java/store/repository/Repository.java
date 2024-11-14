package store.repository;

import java.util.Collection;

public interface Repository<T> {

    Collection<T> getCopiedCollection();

    void clear();

    void addAll(Collection<T> collection);
}
