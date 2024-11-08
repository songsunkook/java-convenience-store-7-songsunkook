package store.service;

import java.util.List;

import store.dto.NoticeResponse;
import store.dto.OrderRequest;
import store.dto.ReceiptResponse;
import store.dto.StocksResponse;

public class StoreService {

    public StocksResponse stocks() {
        return null;
    }

    public void order(List<OrderRequest> orders) {

    }

    public boolean hasNotice() {
        return false;
    }

    public NoticeResponse nextNotice() {
        return null;
    }

    public void noticeAnswer(boolean confirm) {

    }

    public void membershipAnswer(boolean confirm) {

    }

    public ReceiptResponse receipt() {
        return null;
    }
}
