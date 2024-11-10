package store.exception;

public enum ExceptionMessage {

    STORE_ILLEGAL_ARGUMENT("잘못된 입력입니다.", IllegalArgumentException.class),
    INVALID_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다.", IllegalArgumentException.class),
    STOCK_NOT_FOUND("존재하지 않는 상품입니다.", IllegalArgumentException.class),
    QUANTITY_OUT_OF_RANGE("상품 수량은 1 이상이어야 합니다.", IllegalArgumentException.class),
    OVER_STOCK_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다.", IllegalArgumentException.class),

    STORE_ILLEGAL_STATE("잘못된 상황에 대한 요청입니다.", IllegalStateException.class),
    INVALID_FILE("잘못된 File에 대한 요청입니다.", IllegalStateException.class),
    INVALID_NOTICE_TYPE("잘못된 NoticeType에 대한 요청입니다.", IllegalStateException.class),
    INVALID_PROMOTION("잘못된 Promotion에 대한 요청입니다.", IllegalStateException.class),
    REQUEST_BONUS_FOR_NON_PROMOTIONAL_STOCK("프로모션중이지 않은 재고에 대한 보너스 요청입니다.", IllegalStateException.class),
    ;

    public static final String PREFIX = "[ERROR] ";
    public static final String ILLEGAL_ARGUMENT_POSTFIX = " 다시 입력해 주세요.";

    private final String message;
    private final Class<?> exceptionType;

    ExceptionMessage(String message, Class<?> exceptionType) {
        this.message = message;
        this.exceptionType = exceptionType;
    }

    public String getMessage() {
        if (exceptionType == IllegalArgumentException.class) {
            return PREFIX + message + ILLEGAL_ARGUMENT_POSTFIX;
        }
        return PREFIX + message;
    }
}
