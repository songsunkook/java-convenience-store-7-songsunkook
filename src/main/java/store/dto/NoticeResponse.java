package store.dto;

import store.domain.notice.CantPromotionNotice;
import store.domain.notice.FreePromotionNotice;
import store.domain.notice.Notice;
import store.domain.notice.NoticeType;
import store.exception.state.InvalidNoticeTypeException;

public record NoticeResponse(
    Integer id,
    NoticeType noticeType,
    String stockName,
    int stockQuantity
) {

    public static NoticeResponse from(Notice notice) {
        if (notice.getType() == NoticeType.CAN_PROMOTION_WITH_MORE_QUANTITY) {
            return getResponseFrom((FreePromotionNotice)notice);
        }
        if (notice.getType() == NoticeType.CANT_PROMOTION_SOME_STOCKS) {
            return getResponseFrom((CantPromotionNotice)notice);
        }
        throw new InvalidNoticeTypeException();
    }

    private static NoticeResponse getResponseFrom(FreePromotionNotice notice) {
        return new NoticeResponse(
            notice.getId(),
            notice.getType(),
            notice.getStock().getName(),
            notice.getFreeBonusQuantity()
        );
    }

    private static NoticeResponse getResponseFrom(CantPromotionNotice notice) {
        return new NoticeResponse(
            notice.getId(),
            notice.getType(),
            notice.getStockName(),
            notice.getNoPromotionQuantity()
        );
    }
}
