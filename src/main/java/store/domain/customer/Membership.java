package store.domain.customer;

public class Membership {

    private static final int NO_DISCOUNT = 0;
    private static final double DISCOUNT_RATE = 0.3;
    private static final int MAXIMUM_DISCOUNT = 8000;

    private final boolean membership;

    public Membership(boolean membership) {
        this.membership = membership;
    }

    public Membership use(boolean membership) {
        return new Membership(membership);
    }

    public int getDiscount(int totalPrice) {
        if (!membership) {
            return NO_DISCOUNT;
        }
        if (totalPrice * DISCOUNT_RATE >= MAXIMUM_DISCOUNT) {
            return MAXIMUM_DISCOUNT;
        }
        return (int)(totalPrice * DISCOUNT_RATE);
    }
}
