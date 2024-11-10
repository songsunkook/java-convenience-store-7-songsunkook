package store.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public enum OutputMessage {
    WELCOME("안녕하세요. W편의점입니다.%n현재 보유하고 있는 상품입니다.%n%n"),
    /**
     * - 콜라 1,000원 10개 탄산2+1
     * - 콜라 1,000원 10개
     */
    STOCK("- %s %s원 %s %s%n"),
    INPUT_ORDERS("%n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])%n"),
    MEMBERSHIP_CONDITION("%n멤버십 할인을 받으시겠습니까? (Y/N)%n"),
    MORE_BUY("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)%n"),
    RECEIPT(String.format("""
        %n==============W 편의점================
        %-10s%20s%20s
        """, "상품명", "수량", "금액")),
    RECEIPT_STOCK("%-10s%20s%30s%n"),
    RECEIPT_BONUS(String.format("=============증%10s정===============%n", "")),
    RECEIPT_BONUS_STOCK("%-10s%20s%n"),
    RECEIPT_LINE("====================================%n"),
    RECEIPT_MONEY_WITH_COUNT("%-10s%20s%20s%n"),
    RECEIPT_MONEY("%-10s%40s%n"),
    RECEIPT_DISCOUNT_MONEY("%-10s%40s%n"),
    RERUN("%n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)%n"),
    ;
    /**
     * ==============W 편의점================
     * 상품명		수량	금액
     * 콜라		3 	3,000
     * 에너지바 		5 	10,000
     * =============증	정===============
     * 콜라		1
     * ====================================
     * 총구매액		8	13,000
     * 행사할인			-1,000
     * 멤버십할인			-3,000
     * 내실돈			 9,000
     */

    private final String message;

    OutputMessage(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        List<String> formats = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof Integer) {
                formats.add(new DecimalFormat("###,###").format(arg));
                continue;
            }
            formats.add(String.valueOf(arg));
        }
        return String.format(message, formats.toArray());
    }
}
