package store.domain;

import java.util.ArrayList;
import java.util.List;

import store.domain.stock.Stock;

public class Customer {

    private final List<Notice> notices = new ArrayList<>();

    public List<Notice> getNotices() {
        return new ArrayList<>(notices);
    }

    public void notice(NoticeType noticeType, Stock target, int needQuantityForBonus) {
        notices.add(new Notice(noticeType, target, needQuantityForBonus));
    }
}
