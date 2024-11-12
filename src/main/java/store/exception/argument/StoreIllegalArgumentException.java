package store.exception.argument;

import static store.exception.ExceptionMessage.STORE_ILLEGAL_ARGUMENT;

public class StoreIllegalArgumentException extends IllegalArgumentException {

    private static final String DEFAULT_MESSAGE = STORE_ILLEGAL_ARGUMENT.getMessage();

    public StoreIllegalArgumentException() {
        super(DEFAULT_MESSAGE);
    }

    public StoreIllegalArgumentException(String message) {
        super(message);
    }
}
