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
        response.stocks().stream()
            .map(stock -> STOCK.getMessage(
                stock.name(),
                stock.price(),
                stock.outputQuantity(),
                stock.promotion()
            )).forEach(this::print);
        flush();
    }

    public void inputOrders() {
        printWithFlush(INPUT_ORDERS.getMessage());
    }

    public void notice(NoticeResponse response) {
        printWithFlush(
            NoticeMessage.from(response.noticeType())
                .getMessage(
                    response.stockName(),
                    response.stockQuantity()
                ));
    }

    public void noticeMembership() {
        printWithFlush(MEMBERSHIP_CONDITION.getMessage());
    }

    public void receipt(ReceiptResponse response) {
        print(RECEIPT.getMessage());
        printReceiptOrders(response);
        printReceiptBonus(response);
        printReceiptTotalPrice(response);
        flush();
    }

    private void printReceiptOrders(ReceiptResponse response) {
        response.orders().forEach(order ->
            print(RECEIPT_STOCK.getMessage(
                order.name(),
                order.quantity(),
                order.price()
            )));
    }

    private void printReceiptBonus(ReceiptResponse response) {
        print(RECEIPT_BONUS.getMessage());
        response.bonusOrders().forEach(order ->
            print(RECEIPT_BONUS_STOCK.getMessage(
                order.name(),
                order.quantity()
            )));
    }

    private void printReceiptTotalPrice(ReceiptResponse response) {
        print(RECEIPT_LINE.getMessage());
        print(RECEIPT_MONEY_WITH_COUNT.getMessage("총구매액", response.orders().size(), response.totalPrice()));
        print(RECEIPT_DISCOUNT_MONEY.getMessage("행사할인", discountMoneyMessage(response.promotionDiscount())));
        print(RECEIPT_DISCOUNT_MONEY.getMessage("멤버십할인", discountMoneyMessage(response.membershipDiscount())));
        print(RECEIPT_MONEY.getMessage("내실돈", response.payment()));
    }

    private String discountMoneyMessage(int discount) {
        return "-" + discount;
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
