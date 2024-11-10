package store.view;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import camp.nextstep.edu.missionutils.Console;
import store.dto.OrderRequest;
import store.exception.argument.InvalidInputFormatException;

public class InputView {

    public static List<OrderRequest> orders() {
        return Arrays.stream(input().split(","))
            .map(OrderRequest::from)
            .toList();
    }

    public static boolean confirm() {
        String input = Console.readLine();
        if (Objects.equals(input, "Y")) {
            return true;
        }
        if (Objects.equals(input, "N")) {
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
