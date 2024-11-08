package store.view;

public enum OutputMessage {
    WELCOME("안녕하세요. W편의점입니다.%n현재 보유하고 있는 상품입니다.%n%n"),
    /**
     * - 콜라 1,000원 10개 탄산2+1
     * - 콜라 1,000원 10개
     */
    STOCK("- %s %d원 %d개 %s%n"),
    INPUT_ORDERS("%n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])%n"),
    MEMBERSHIP_CONDITION("%n멤버십 할인을 받으시겠습니까? (Y/N)%n"),
    MORE_BUY("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)%n"),
    RECEIPT("""
        %n==============W 편의점================
        상품명\t\t수량\t\t금액
        """),
    RECEIPT_STOCK("%-10s\t\t%4d\t\t%7d%n"),
    RECEIPT_BONUS("=============증	정===============%n"),
    RECEIPT_BONUS_STOCK("%-10s\t%4d%n"),
    RECEIPT_LINE("====================================%n"),
    RECEIPT_MONEY_WITH_COUNT("%-6s\t\t%4s\t\t%7s%n"),
    RECEIPT_MONEY("%-6s\t\t%7s%n"),
    RERUN("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)%n"),
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
