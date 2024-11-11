package store.domain.store;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Promotion {

    private static final int NEXT_DAY = 1;

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        startDateTime = startDate.atStartOfDay();
        endDateTime = endDate.plusDays(NEXT_DAY).atStartOfDay();
    }

    public boolean inProgress(LocalDateTime now) {
        return now.isAfter(startDateTime) && now.isBefore(endDateTime);
    }

    public int bonus(int buy) {
        return buy / this.buy * get;
    }

    public int buyAndGet() {
        return buy + get;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public String getName() {
        return name;
    }
}
