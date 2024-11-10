package store.exception.state;

import static store.exception.ExceptionMessage.STORE_ILLEGAL_STATE;

public class StoreIllegalStateException extends IllegalStateException {

    private static final String DEFAULT_MESSAGE = STORE_ILLEGAL_STATE.getMessage();

    public StoreIllegalStateException() {
        super(DEFAULT_MESSAGE);
    }

    public StoreIllegalStateException(String message) {
        super(message);
    }
}
