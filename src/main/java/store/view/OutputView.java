package store.view;

import static store.view.OutputMessage.*;

import store.dto.NoticeResponse;
import store.dto.ReceiptResponse;
import store.dto.StocksResponse;

public class OutputView {

    private StringBuilder buffer;

    public OutputView() {
        setupBuffer();
    }

    public void welcome() {
        print(WELCOME.getMessage());
    }

    public void showStocks(StocksResponse response) {

    }

    public void inputOrders() {
        print(INPUT_ORDERS.getMessage());
    }

    public void notice(NoticeResponse response) {

    }

    public void noticeMembership() {
        printWithFlush(NOTICE_MEMBERSHIP.getMessage());
    }

    public void receipt(ReceiptResponse response) {

    }

    public void inputRerun() {
        printWithFlush(RERUN.getMessage());
    }

    public void exception(Exception e) {
        printWithFlush(e.getMessage());
    }

    private void setupBuffer() {
        buffer = new StringBuilder();
    }

    private void flush() {
        System.out.print(buffer);
        setupBuffer();
    }

    private void print(String content) {
        buffer.append(content);
    }

    private void printWithFlush(String content) {
        buffer.append(content);
        flush();
    }
}
