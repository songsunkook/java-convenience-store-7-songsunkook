package store.domain.customer;

import java.util.ArrayList;
import java.util.List;

import store.domain.notice.Notice;

public class Notices {

    private final List<Notice> notices = new ArrayList<>();

    public void add(Notice notice) {
        notices.add(notice);
    }

    public boolean hasNext() {
        return !notices.isEmpty();
    }

    public Notice pop() {
        return notices.removeFirst();
    }

    public List<Notice> getNotices() {
        return new ArrayList<>(notices);
    }
}
