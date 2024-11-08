package store.view;

import java.util.Arrays;
import java.util.List;

import camp.nextstep.edu.missionutils.Console;
import store.domain.customer.OrderRequest;

public class InputView {

    // [콜라-3],[에너지바-5]
    public List<OrderRequest> orders() {
        return Arrays.stream(input().split(","))
            .map(OrderRequest::from)
            .toList();
    }

    public boolean confirm() {
        return Console.readLine() == "Y";
    }

    private String input() {
        return Console.readLine();
    }

    public void close() {
        Console.close();
    }
}
