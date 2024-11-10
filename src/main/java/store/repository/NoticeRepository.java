package store.repository;

import java.util.HashMap;
import java.util.Map;

import store.domain.customer.Customer;
import store.domain.notice.Notice;

public class NoticeRepository {

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
}
