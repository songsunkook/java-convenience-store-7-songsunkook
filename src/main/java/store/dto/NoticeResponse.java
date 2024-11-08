package store.dto;

import store.domain.customer.NoticeType;

public record NoticeResponse(
    Integer id,
    NoticeType noticeType,
    String stockName,
    int stockQuantity
) {
}
