package store.exception.state;

import static store.exception.ExceptionMessage.REQUEST_BONUS_FOR_NO_PROMOTION_STOCK;

public class RequestBonusForNoPromotionStock extends IllegalStateException {

    private static final String DEFAULT_MESSAGE = REQUEST_BONUS_FOR_NO_PROMOTION_STOCK.getMessage();

    public RequestBonusForNoPromotionStock() {
        super(DEFAULT_MESSAGE);
    }

    public RequestBonusForNoPromotionStock(String message) {
        super(message);
    }
}
