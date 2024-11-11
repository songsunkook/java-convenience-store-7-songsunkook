package store.domain.store;

import static store.domain.notice.NoticeType.CANT_PROMOTION_SOME_STOCKS;
import static store.domain.notice.NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY;

import store.domain.customer.Customer;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;
import store.exception.argument.OverStockQuantityException;

public class Cashier {

    private static final int NO_BONUS = 0;

    private final Stocks stocks;
    private final Customer customer;
    private boolean finishCalculate;
    private Stock onPromotionStock;
    private Stock noPromotionStock;
    private int leftRequestQuantity;

    public Cashier(Stocks stocks, Customer customer) {
        this.stocks = stocks;
        this.customer = customer;
    }

    public void calculate(String requestName, int requestQuantity) {
        finishCalculate = false;
        onPromotionStock = stocks.findByNameAndPromotion(requestName, true);
        noPromotionStock = stocks.findByNameAndPromotion(requestName, false);
        leftRequestQuantity = requestQuantity;
        calculateIfNotFinish(this::requestIsBiggerThanPromotionQuantity);
        calculateIfNotFinish(this::requestInPromotionQuantity);
        calculateIfNotFinish(this::onlyHaveNormalStock);
    }

    private void requestIsBiggerThanPromotionQuantity() {
        if (onPromotionStock == null || onPromotionStock.getQuantity() >= leftRequestQuantity) {
            return;
        }
        int dropQuantityToNormal =
            onPromotionStock.getQuantity() % onPromotionStock.getPromotion().buyAndGet() +
                leftRequestQuantity - onPromotionStock.getQuantity();
        sendNotice(CANT_PROMOTION_SOME_STOCKS, onPromotionStock, noPromotionStock, leftRequestQuantity,
            dropQuantityToNormal);
        finishCalculate = true;
    }

    private void requestInPromotionQuantity() {
        if (onPromotionStock == null) {
            return;
        }
        int noPromotionCount = leftRequestQuantity % onPromotionStock.getPromotion().buyAndGet();
        if (noPromotionCount == onPromotionStock.getPromotion().getBuy() &&
            onPromotionStock.getQuantity() >= leftRequestQuantity + onPromotionStock.getPromotion().getGet()) {
            sendNotice(CAN_PROMOTION_WITH_MORE_QUANTITY, onPromotionStock, leftRequestQuantity);
            finishCalculate = true;
            return;
        }

        Promotion promotion = onPromotionStock.getPromotion();
        int buyCount =
            leftRequestQuantity / promotion.buyAndGet() * promotion
                .buyAndGet();
        if (buyCount > 0) {
            buyStock(customer, onPromotionStock, buyCount, buyCount / promotion.buyAndGet() * promotion.getGet(),
                true);
            leftRequestQuantity -= buyCount;
            if (noLeftQuantity()) {
                finishCalculate = true;
            }
        }
    }

    private boolean noLeftQuantity() {
        return leftRequestQuantity == 0;
    }

    private void onlyHaveNormalStock() {
        int totalQuantity = 0;
        boolean hasOnPromotionStock = false;
        if (noPromotionStock != null) {
            totalQuantity += noPromotionStock.getQuantity();
        }
        if (onPromotionStock != null) {
            hasOnPromotionStock = true;
            totalQuantity += onPromotionStock.getQuantity();
        }
        if (totalQuantity < leftRequestQuantity) {
            throw new OverStockQuantityException();
        }
        if (hasOnPromotionStock) {
            int useQuantity = onPromotionStock.getQuantity();
            buyStock(customer, onPromotionStock, Math.min(leftRequestQuantity, useQuantity), NO_BONUS,
                false);
            leftRequestQuantity -= useQuantity;
        }
        buyStock(customer, noPromotionStock, leftRequestQuantity, NO_BONUS, false);
        finishCalculate = true;
    }

    private void calculateIfNotFinish(Runnable runnable) {
        if (finishCalculate) {
            return;
        }
        runnable.run();
    }

    private void sendNotice(NoticeType noticeType, Stock stock, int quantity) {
        customer.notice(Notice.of(noticeType, stock, quantity));
    }

    private void sendNotice(NoticeType noticeType, Stock stock1, Stock stock2, int totalQuantity,
        int noPromotionQuantity) {
        customer.notice(Notice.of(noticeType, stock1, stock2, totalQuantity, noPromotionQuantity));
    }

    private void buyStock(Customer customer, Stock stock, int quantity, int bonusQuantity, boolean onPromotion) {
        customer.order(stock, quantity, bonusQuantity, onPromotion);
    }
}
