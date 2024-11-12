package store.domain.store;

import static store.domain.notice.NoticeType.CANT_PROMOTION_SOME_STOCKS;
import static store.domain.notice.NoticeType.CAN_GET_FREE_BONUS;

import store.domain.customer.Customer;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;
import store.exception.argument.OverStockQuantityException;

public class Cashier {

    private static final int NO_BONUS = 0;

    private final Stocks stocks;
    private final Customer customer;

    private Stock onPromotionStock;
    private Stock noPromotionStock;
    private boolean finishCalculate;
    private int leftQuantity;

    public Cashier(Stocks stocks, Customer customer) {
        this.stocks = stocks;
        this.customer = customer;
    }

    public void calculate(String requestName, int requestQuantity) {
        setupBeforeCalculate(requestName, requestQuantity);
        calculateIfNotFinish(this::requestIsBiggerThanPromotionQuantity);
        calculateIfNotFinish(this::requestInPromotionQuantity);
        calculateIfNotFinish(this::onlyHaveNormalStock);
    }

    private void setupBeforeCalculate(String requestName, int requestQuantity) {
        onPromotionStock = stocks.findByNameAndPromotion(requestName, true);
        noPromotionStock = stocks.findByNameAndPromotion(requestName, false);
        finishCalculate = false;
        leftQuantity = requestQuantity;
        validateTotalQuantity();
    }

    private void requestIsBiggerThanPromotionQuantity() {
        if (onPromotionStock == null || onPromotionStock.getQuantity() >= leftQuantity) {
            return;
        }
        int passToNormalQuantity = onPromotionStock.getQuantity() % onPromotionStock.getPromotion().buyAndGet() +
            leftQuantity - onPromotionStock.getQuantity();
        sendNotice(CANT_PROMOTION_SOME_STOCKS, onPromotionStock, noPromotionStock, leftQuantity,
            passToNormalQuantity);
        finishCalculate = true;
    }

    private void requestInPromotionQuantity() {
        if (onPromotionStock == null) {
            return;
        }
        if (noticeFreeBonus()) {
            return;
        }
        buyPromotionStock();
    }

    private static int getBonusQuantity(int buyCount, Promotion promotion) {
        return buyCount / promotion.buyAndGet() * promotion.getGet();
    }

    private boolean noticeFreeBonus() {
        int onPromotionQuantity = leftQuantity % onPromotionStock.getPromotion().buyAndGet();
        if (buyQuantityEqualsPromotion(onPromotionQuantity) && canGetFreeBonus()) {
            sendNotice(CAN_GET_FREE_BONUS, onPromotionStock, leftQuantity);
            finishCalculate = true;
            return true;
        }
        return false;
    }

    private boolean buyQuantityEqualsPromotion(int onPromotionQuantity) {
        return onPromotionQuantity == onPromotionStock.getPromotion().getBuy();
    }

    private boolean canGetFreeBonus() {
        return onPromotionStock.getQuantity() >= leftQuantity + onPromotionStock.getPromotion().getGet();
    }

    private void buyPromotionStock() {
        Promotion promotion = onPromotionStock.getPromotion();
        int buyCount = leftQuantity / promotion.buyAndGet() * promotion.buyAndGet();
        if (buyCount > 0) {
            buyStock(customer, onPromotionStock, buyCount, getBonusQuantity(buyCount, promotion), true);
            leftQuantity -= buyCount;
            if (noLeftQuantity()) {
                finishCalculate = true;
            }
        }
    }

    private boolean noLeftQuantity() {
        return leftQuantity == 0;
    }

    private void onlyHaveNormalStock() {
        if (onPromotionStock != null) {
            int useQuantity = onPromotionStock.getQuantity();
            buyStock(customer, onPromotionStock, Math.min(leftQuantity, useQuantity), NO_BONUS, false);
            leftQuantity -= useQuantity;
        }
        buyStock(customer, noPromotionStock, leftQuantity, NO_BONUS, false);
        finishCalculate = true;
    }

    private void validateTotalQuantity() {
        int totalQuantity = 0;
        if (noPromotionStock != null) {
            totalQuantity += noPromotionStock.getQuantity();
        }
        if (onPromotionStock != null) {
            totalQuantity += onPromotionStock.getQuantity();
        }
        if (totalQuantity < leftQuantity) {
            throw new OverStockQuantityException();
        }
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
