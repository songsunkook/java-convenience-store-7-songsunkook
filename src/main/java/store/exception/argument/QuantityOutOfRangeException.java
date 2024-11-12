package store.exception.argument;

import static store.exception.ExceptionMessage.QUANTITY_OUT_OF_RANGE;

public class QuantityOutOfRangeException extends StoreIllegalArgumentException {

    private static final String DEFAULT_MESSAGE = QUANTITY_OUT_OF_RANGE.getMessage();

    public QuantityOutOfRangeException() {
        super(DEFAULT_MESSAGE);
    }

    public QuantityOutOfRangeException(String message) {
        super(message);
    }
}
