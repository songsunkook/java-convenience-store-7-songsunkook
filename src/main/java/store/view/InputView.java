package store.view;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import camp.nextstep.edu.missionutils.Console;
import store.dto.OrderRequest;

public class InputView {

    // [콜라-3],[에너지바-5]
    public static List<OrderRequest> orders() {
        return Arrays.stream(input().split(","))
            .map(OrderRequest::from)
            .toList();
    }

    public static boolean confirm() {
        return Objects.equals(Console.readLine(), "Y");
    }

    private static String input() {
        return Console.readLine();
    }

    public static void close() {
        Console.close();
    }
}
