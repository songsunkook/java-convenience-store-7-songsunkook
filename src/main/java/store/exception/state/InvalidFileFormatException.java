package store.exception.state;

import static store.exception.ExceptionMessage.INVALID_FILE_FORMAT;

public class InvalidFileFormatException extends StoreIllegalStateException {

    private static final String DEFAULT_MESSAGE = INVALID_FILE_FORMAT.getMessage();

    public InvalidFileFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidFileFormatException(String message) {
        super(message);
    }
}
