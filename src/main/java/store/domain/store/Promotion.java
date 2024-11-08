package store.domain.store;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Promotion {
    // name,buy,get,start_date,end_date
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean inProgress(LocalDateTime now) {
        // TODO: 종료일이 포함되는지 확인하기
        return now.isAfter(startDate.atStartOfDay()) && now.isBefore(endDate.atStartOfDay());
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
