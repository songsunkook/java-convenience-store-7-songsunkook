package store.domain.store;

import static store.constant.StoreConstant.MINIMUM_STOCK_QUANTITY;
import static store.domain.notice.NoticeType.CANT_PROMOTION_SOME_STOCKS;
import static store.domain.notice.NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY;

import store.domain.customer.Customer;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;
import store.exception.argument.OverStockQuantityException;
import store.exception.argument.QuantityOutOfRangeException;
import store.exception.argument.StockNotFoundException;

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
        validate(requestName, requestQuantity);
        finishCalculate = false;
        onPromotionStock = findStockByNameAndPromotionIs(requestName, true);
        noPromotionStock = findStockByNameAndPromotionIs(requestName, false);
        leftRequestQuantity = requestQuantity;
        calculateIfNotFinish(this::requestIsBiggerThanPromotionQuantity);
        calculateIfNotFinish(this::requestInPromotionQuantity);
        calculateIfNotFinish(this::onlyHaveNormalStock);
    }

    private void validate(String name, int quantity) {
        if (stocks.findByName(name).isEmpty()) {
            throw new StockNotFoundException();
        }
        if (quantity < MINIMUM_STOCK_QUANTITY) {
            throw new QuantityOutOfRangeException();
        }
    }

    private void requestIsBiggerThanPromotionQuantity() {
        if (onPromotionStock == null || onPromotionStock.getQuantity() >= leftRequestQuantity) {
            return;
        }
        validateRequestOverTotalQuantity(leftRequestQuantity);
        int dropQuantityToNormal =
            leftRequestQuantity / onPromotionStock.getPromotion().buyAndGet() * onPromotionStock.getPromotion().buyAndGet();
        if (onPromotionStock.getQuantity() > dropQuantityToNormal) {
            buyStock(customer, onPromotionStock, onPromotionStock.getQuantity() - dropQuantityToNormal, NO_BONUS, false);
        }
        sendNotice(CANT_PROMOTION_SOME_STOCKS, noPromotionStock, dropQuantityToNormal);
        finishCalculate = true;
    }

    private void requestInPromotionQuantity() {
        if (onPromotionStock == null) {
            return;
        }
        int noPromotionCount = leftRequestQuantity % onPromotionStock.getPromotion().buyAndGet();
        if (noPromotionCount == onPromotionStock.getPromotion().getBuy()) {
            sendNotice(CAN_PROMOTION_WITH_MORE_QUANTITY, onPromotionStock, leftRequestQuantity);
            finishCalculate = true;
            return;
        }

        Promotion promotion = onPromotionStock.getPromotion();
        int buyCount =
            leftRequestQuantity / promotion.buyAndGet() * promotion
                .buyAndGet();
        buyStock(customer, onPromotionStock, buyCount, buyCount / promotion.buyAndGet() * promotion.getGet(),
            true);
        leftRequestQuantity -= buyCount;
        if (noLeftQuantity()) {
            finishCalculate = true;
        }
    }

    private boolean noLeftQuantity() {
        return leftRequestQuantity == 0;
    }

    private void onlyHaveNormalStock() {
        if (noPromotionStock == null ||
            noPromotionStock.isEmpty() ||
            noPromotionStock.getQuantity() < leftRequestQuantity) {
            throw new OverStockQuantityException();
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

    private void validateRequestOverTotalQuantity(int requestQuantity) {
        int totalQuantity = 0;
        if (onPromotionStock != null) {
            totalQuantity += onPromotionStock.getQuantity();
        }
        if (noPromotionStock != null) {
            totalQuantity += noPromotionStock.getQuantity();
        }
        if (totalQuantity < requestQuantity) {
            throw new OverStockQuantityException();
        }
    }

    private void sendNotice(NoticeType noticeType, Stock stock, int dropQuantityToNormal) {
        customer.notice(Notice.of(noticeType, stock, dropQuantityToNormal));
    }

    private Stock findStockByNameAndPromotionIs(String name, boolean onPromotion) {
        return stocks.findByName(name).stream()
            .filter(stock -> stock.onPromotion() == onPromotion)
            .findAny()
            .orElse(null);
    }

    private void buyStock(Customer customer, Stock stock, int quantity, int bonusQuantity, boolean onPromotion) {
        customer.order(stock, quantity, bonusQuantity, onPromotion);
    }
}
