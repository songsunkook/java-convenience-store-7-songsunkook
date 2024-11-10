package store.exception.state;

import static store.exception.ExceptionMessage.INVALID_NOTICE_TYPE;

public class InvalidNoticeTypeException extends StoreIllegalStateException {

    private static final String DEFAULT_MESSAGE = INVALID_NOTICE_TYPE.getMessage();

    public InvalidNoticeTypeException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidNoticeTypeException(String message) {
        super(message);
    }
}
