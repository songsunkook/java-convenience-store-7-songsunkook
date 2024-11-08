package store.service;

import java.util.List;

import store.domain.store.Promotion;
import store.domain.store.Stock;
import store.dto.NoticeResponse;
import store.dto.OrderRequest;
import store.dto.ReceiptResponse;
import store.dto.StocksResponse;
import store.util.FileParser;

public class StoreService {

    private List<Promotion> promotions;
    private List<Stock> stocks;

    public void preSetting() {
        promotions = FileParser.readPromotions();
        stocks = FileParser.readProducts(promotions);
        System.out.println();
    }

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
