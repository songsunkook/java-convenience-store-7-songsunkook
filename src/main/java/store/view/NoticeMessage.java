package store.view;

import static store.domain.notice.NoticeType.CANT_PROMOTION_SOME_STOCKS;
import static store.domain.notice.NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY;

import java.util.Arrays;

import store.domain.notice.NoticeType;

public enum NoticeMessage {
    FREE_BONUS("%n현재 %s은(는) %s개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n", CAN_PROMOTION_WITH_MORE_QUANTITY),
    CANT_PROMOTION("%n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n", CANT_PROMOTION_SOME_STOCKS),
    ;

    private final String message;
    private final NoticeType noticeType;

    NoticeMessage(String message, NoticeType noticeType) {
        this.message = message;
        this.noticeType = noticeType;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }

    public static NoticeMessage from(NoticeType noticeType) {
        return Arrays.stream(values())
            .filter(noticeMessage -> noticeMessage.noticeType == noticeType)
            .findAny()
            .orElseThrow();
        // TODO: 구체적 예외처리
    }
}
