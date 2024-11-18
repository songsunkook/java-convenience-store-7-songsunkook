package store.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import store.domain.customer.Customer;
import store.domain.notice.Notice;

public class NoticeRepository implements Repository<Notice> {

    private static final int DEFAULT_AUTO_INCREMENT = 1;

    private final Map<Integer, Notice> db = new HashMap<>();
    private int autoIncrement = DEFAULT_AUTO_INCREMENT;

    public void save(Notice notice) {
        if (db.containsKey(notice.getId())) {
            db.replace(notice.getId(), notice);
            return;
        }
        db.put(autoIncrement, notice);
        notice.save(autoIncrement);
        autoIncrement++;
    }

    public Notice findById(int id) {
        return db.get(id);
    }

    public void remove(Notice notice) {
        db.remove(notice.getId());
    }

    public void saveAllByCustomer(Customer customer) {
        customer.getNotices().forEach(this::save);
    }

    @Override
    public Collection<Notice> getCopiedCollection() {
        return db.values().stream()
            .map(Notice::copyOf)
            .toList();
    }

    @Override
    public void clear() {
        db.clear();
    }

    @Override
    public void addAll(Collection<Notice> collection) {
        if (collection != null && !collection.isEmpty()) {
            collection.forEach(content -> db.put(content.getId(), content));
        }
    }
}
