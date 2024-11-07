package store.domain.stock;

public class Stock {

    private final String name;
    private int price;
    private int quantity;
    private Object promotion;

    public Stock(String name, int price, int quantity, Object promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public void buy(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
        this.quantity -= quantity;
    }
}
