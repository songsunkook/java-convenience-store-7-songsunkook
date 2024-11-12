package store.exception.argument;

import static store.exception.ExceptionMessage.OVER_STOCK_QUANTITY;

public class OverStockQuantityException extends StoreIllegalArgumentException {

    private static final String DEFAULT_MESSAGE = OVER_STOCK_QUANTITY.getMessage();

    public OverStockQuantityException() {
        super(DEFAULT_MESSAGE);
    }

    public OverStockQuantityException(String message) {
        super(message);
    }
}
