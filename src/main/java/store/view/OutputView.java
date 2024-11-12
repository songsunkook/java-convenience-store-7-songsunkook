package store.view;

import static store.view.OutputMessage.*;

import store.dto.NoticeResponse;
import store.dto.ReceiptResponse;
import store.dto.StocksResponse;

public class OutputView {

    private static final String TOTAL_PRICE = "총구매액";
    private static final String DISCOUNT_PROMOTION = "행사할인";
    private static final String DISCOUNT_MEMBERSHIP = "멤버십할인";
    private static final String PAYMENT = "내실돈";
    private static final String DISCOUNT_MONEY_PREFIX = "-";

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
        print(RECEIPT_MONEY_WITH_COUNT.getMessage(
            TOTAL_PRICE, response.totalQuantity(), response.totalPrice()));
        print(RECEIPT_DISCOUNT_MONEY.getMessage(
            DISCOUNT_PROMOTION, discountMoneyMessage(response.promotionDiscount())));
        print(RECEIPT_DISCOUNT_MONEY.getMessage(
            DISCOUNT_MEMBERSHIP, discountMoneyMessage(response.membershipDiscount())));
        print(RECEIPT_MONEY.getMessage(PAYMENT, response.payment()));
    }

    private String discountMoneyMessage(int discount) {
        return DISCOUNT_MONEY_PREFIX + discount;
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
