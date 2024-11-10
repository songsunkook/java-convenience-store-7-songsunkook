package store.view;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import camp.nextstep.edu.missionutils.Console;
import store.dto.OrderRequest;
import store.exception.argument.InvalidInputFormatException;

public class InputView {

    private static final String ORDERS_DELIMITER = ",";
    private static final String YES_ANSWER = "Y";
    private static final String NO_ANSWER = "N";

    private InputView() {
    }

    public static List<OrderRequest> orders() {
        return Arrays.stream(input().split(ORDERS_DELIMITER))
            .map(OrderRequest::from)
            .toList();
    }

    public static boolean confirm() {
        String input = Console.readLine();
        if (Objects.equals(input, YES_ANSWER)) {
            return true;
        }
        if (Objects.equals(input, NO_ANSWER)) {
            return false;
        }
        throw new InvalidInputFormatException();
    }

    private static String input() {
        return Console.readLine();
    }

    public static void close() {
        Console.close();
    }
}
