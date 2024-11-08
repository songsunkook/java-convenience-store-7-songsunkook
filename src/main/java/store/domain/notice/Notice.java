package store.domain.notice;

public interface Notice {

    void save(int id);
    Integer getId();
    NoticeType getType();
}
