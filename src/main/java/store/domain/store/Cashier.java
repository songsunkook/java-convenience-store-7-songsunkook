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
    private Stock onPromotion;
    private Stock notPromotionStock;
    private int leftRequestQuantity;

    public Cashier(Stocks stocks, Customer customer) {
        this.stocks = stocks;
        this.customer = customer;
    }

    public void calculate(String requestName, int requestQuantity) {
        validate(requestName, requestQuantity);
        finishCalculate = false;
        onPromotion = findStockByNameAndPromotionIs(requestName, true);
        notPromotionStock = findStockByNameAndPromotionIs(requestName, false);
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
        if (onPromotion == null || onPromotion.getQuantity() >= leftRequestQuantity) {
            return;
        }
        validateRequestOverTotalQuantity(leftRequestQuantity);
        int dropQuantityToNormal =
            leftRequestQuantity / onPromotion.getPromotion().buyAndGet() * onPromotion.getPromotion().buyAndGet();
        if (onPromotion.getQuantity() > dropQuantityToNormal) {
            buyStock(customer, onPromotion, onPromotion.getQuantity() - dropQuantityToNormal, NO_BONUS, false);
        }
        sendNotice(CANT_PROMOTION_SOME_STOCKS, notPromotionStock, dropQuantityToNormal);
        finishCalculate = true;
    }

    private void requestInPromotionQuantity() {
        if (onPromotion == null) {
            return;
        }
        int notPromotionCount = leftRequestQuantity % onPromotion.getPromotion().buyAndGet();
        if (notPromotionCount == onPromotion.getPromotion().getBuy()) {
            sendNotice(CAN_PROMOTION_WITH_MORE_QUANTITY, onPromotion, leftRequestQuantity);
            finishCalculate = true;
            return;
        }

        Promotion promotion = onPromotion.getPromotion();
        int buyCount =
            leftRequestQuantity / promotion.buyAndGet() * promotion
                .buyAndGet();
        buyStock(customer, onPromotion, buyCount, buyCount / promotion.buyAndGet() * promotion.getGet(),
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
        if (notPromotionStock == null ||
            notPromotionStock.isEmpty() ||
            notPromotionStock.getQuantity() < leftRequestQuantity) {
            throw new OverStockQuantityException();
        }
        buyStock(customer, notPromotionStock, leftRequestQuantity, NO_BONUS, false);
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
        if (onPromotion != null) {
            totalQuantity += onPromotion.getQuantity();
        }
        if (notPromotionStock != null) {
            totalQuantity += notPromotionStock.getQuantity();
        }
        if (totalQuantity < requestQuantity) {
            throw new OverStockQuantityException();
        }
    }

    private void sendNotice(NoticeType noticeType, Stock stock, int dropQuantityToNormal) {
        customer.notice(Notice.of(noticeType, stock, dropQuantityToNormal));
    }

    private Stock findStockByNameAndPromotionIs(String name, boolean promotioning) {
        return stocks.findByName(name).stream()
            .filter(stock -> stock.isPromotioning() == promotioning)
            .findAny()
            .orElse(null);
    }

    private void buyStock(Customer customer, Stock stock, int quantity, int bonusQuantity, boolean isPromotioning) {
        customer.order(stock, quantity, bonusQuantity, isPromotioning);
    }
}
