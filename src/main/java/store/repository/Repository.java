package store.repository;

import java.util.Collection;

public interface Repository {

    Collection<?> getOriginalCollection();

    Collection<?> getCopiedCollection();
}
