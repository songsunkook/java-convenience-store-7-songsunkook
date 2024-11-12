package store.domain.store;

import java.util.ArrayList;
import java.util.List;

public class Promotions {

    private List<Promotion> promotions = new ArrayList<>();

    public void addAll(List<Promotion> promotions) {
        this.promotions.addAll(promotions);
    }

    public List<Promotion> get() {
        return promotions;
    }
}
