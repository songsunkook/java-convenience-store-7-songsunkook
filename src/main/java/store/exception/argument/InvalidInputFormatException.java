package store.exception.argument;

import static store.exception.ExceptionMessage.INVALID_INPUT_FORMAT;

public class InvalidInputFormatException extends StoreIllegalArgumentException {

    private static final String DEFAULT_MESSAGE = INVALID_INPUT_FORMAT.getMessage();

    public InvalidInputFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidInputFormatException(String message) {
        super(message);
    }
}
