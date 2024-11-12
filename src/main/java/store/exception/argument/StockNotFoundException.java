package store.exception.argument;

import static store.exception.ExceptionMessage.STOCK_NOT_FOUND;

public class StockNotFoundException extends StoreIllegalArgumentException {

    private static final String DEFAULT_MESSAGE = STOCK_NOT_FOUND.getMessage();

    public StockNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public StockNotFoundException(String message) {
        super(message);
    }
}
