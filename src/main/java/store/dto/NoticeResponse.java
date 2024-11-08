package store.dto;

import store.domain.notice.CantPromotionNotice;
import store.domain.notice.FreePromotionNotice;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;

public record NoticeResponse(
    Integer id,
    NoticeType noticeType,
    String stockName,
    int stockQuantity
) {

    public static NoticeResponse from(Notice notice) {
        if (notice.getType() == NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY) {
            FreePromotionNotice freePromotionNotice = (FreePromotionNotice)notice;
            return new NoticeResponse(
                freePromotionNotice.getId(),
                freePromotionNotice.getType(),
                freePromotionNotice.getStock().getName(),
                freePromotionNotice.getMoreQuantity()
            );
        }
        if (notice.getType() == NoticeType.CANT_PROMOTION_SOME_STOCKS) {
            CantPromotionNotice cantPromotionNotice = (CantPromotionNotice)notice;
            return new NoticeResponse(
                cantPromotionNotice.getId(),
                cantPromotionNotice.getType(),
                cantPromotionNotice.getStockName(),
                cantPromotionNotice.getQuantity()
            );
        }
        throw new IllegalStateException("");
        // TODO: 예외수정
    }
}
