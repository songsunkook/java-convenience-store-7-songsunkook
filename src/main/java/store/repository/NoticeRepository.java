package store.repository;

import java.util.HashMap;
import java.util.Map;

import store.domain.notice.Notice;

public class NoticeRepository {

    private final Map<Integer, Notice> db = new HashMap<>();
    private int autoIncrement = 1;

    public Notice save(Notice notice) {
        if (db.containsKey(notice.getId())) {
            db.replace(notice.getId(), notice);
            return notice;
        }
        db.put(autoIncrement, notice);
        notice.save(autoIncrement);
        autoIncrement++;
        return notice;
    }

    public Notice findById(int id) {
        return db.get(id);
    }

    public void remove(Notice notice) {
        db.remove(notice.getId());
    }
}
