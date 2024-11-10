package store.exception.state;

import static store.exception.ExceptionMessage.REQUEST_BONUS_FOR_NON_PROMOTIONAL_STOCK;

public class RequestBonusForNonPromotionalStock extends IllegalStateException {

    private static final String DEFAULT_MESSAGE = REQUEST_BONUS_FOR_NON_PROMOTIONAL_STOCK.getMessage();

    public RequestBonusForNonPromotionalStock() {
        super(DEFAULT_MESSAGE);
    }

    public RequestBonusForNonPromotionalStock(String message) {
        super(message);
    }
}
