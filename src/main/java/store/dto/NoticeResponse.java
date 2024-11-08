package store.dto;

import store.domain.notice.CantPromotionNotice;
import store.domain.notice.FreeBonusNotice;
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
            FreeBonusNotice freeBonusNotice = (FreeBonusNotice)notice;
            return new NoticeResponse(
                freeBonusNotice.getId(),
                freeBonusNotice.getType(),
                freeBonusNotice.getStock().getName(),
                freeBonusNotice.getMoreQuantity()
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
