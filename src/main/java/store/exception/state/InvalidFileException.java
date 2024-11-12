package store.exception.state;

import static store.exception.ExceptionMessage.INVALID_FILE;

public class InvalidFileException extends StoreIllegalStateException {

    private static final String DEFAULT_MESSAGE = INVALID_FILE.getMessage();

    public InvalidFileException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidFileException(String message) {
        super(message);
    }
}
