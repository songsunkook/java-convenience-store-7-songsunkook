package store.exception.state;

import static store.exception.ExceptionMessage.INVALID_PROMOTION;

public class InvalidPromotionException extends StoreIllegalStateException {

    private static final String DEFAULT_MESSAGE = INVALID_PROMOTION.getMessage();

    public InvalidPromotionException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidPromotionException(String message) {
        super(message);
    }
}
