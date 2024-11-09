package store.domain.customer;

public class Membership {

    private final boolean membership;

    public Membership(boolean membership) {
        this.membership = membership;
    }

    public Membership use(boolean membership) {
        return new Membership(membership);
    }

    public int getDiscount(int totalPrice) {
        if (!membership) {
            return 0;
        }
        if (totalPrice * 0.3 >= 8000) {
            return 8000;
        }
        return (int)(totalPrice * 0.3);
    }
}
