package store.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import store.exception.argument.InvalidInputFormatException;

public record OrderRequest(String name, int quantity) {

    private static final String REGEX = "\\[(.*)-(.*)\\]";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private static final int STOCK_NAME_INDEX = 1;
    private static final int STOCK_QUANTITY_INDEX = 2;

    public static OrderRequest from(String input) {
        Matcher matcher = PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidInputFormatException();
        }
        return new OrderRequest(matcher.group(STOCK_NAME_INDEX), toInteger(matcher.group(STOCK_QUANTITY_INDEX)));
    }

    private static int toInteger(String content) {
        try {
            return Integer.parseInt(content);
        } catch (NumberFormatException e) {
            throw new InvalidInputFormatException();
        }
    }
}
