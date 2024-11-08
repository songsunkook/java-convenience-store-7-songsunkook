package store.view;

public enum OutputMessage {
    WELCOME("안녕하세요. W편의점입니다.%n현재 보유하고 있는 상품입니다.%n"),
    /**
     * - 콜라 1,000원 10개 탄산2+1
     * - 콜라 1,000원 10개
     */
    STOCK("- %s %s원 %s개 %s"),
    INPUT_STOCKS("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    NOTICE_FREE_BONUS("현재 %s은(는) %s개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    NOTICE_MEMBERSHIP("멤버십 할인을 받으시겠습니까? (Y/N)"),
    NOTICE_CANT_PROMOTION("현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    MORE_BUY("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"),
    RECEIPT("""
        ==============W 편의점================
        상품명		수량	금액
        """),
    RECEIPT_STOCK("%-10s\t%4s\t%7s%n"),
    RECEIPT_BONUS("=============증	정===============%n"),
    RECEIPT_BONUS_STOCK("%-10s\t%4s"),
    RECEIPT_LINE("===================================="),
    RECEIPT_MONEY_WITH_COUNT("%-6s\t%4s\t%7s%n"),
    RECEIPT_MONEY("%-6s\t    \t%7s%n"),
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
        return String.format(message, args);
    }
}
