package store.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import store.exception.argument.InvalidInputFormatException;

public record OrderRequest(String name, int quantity) {

    private static final String REGEX = "\\[(.*)-(.*)\\]";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static OrderRequest from(String input) {
        Matcher matcher = PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidInputFormatException();
        }
        return new OrderRequest(matcher.group(1), toInteger(matcher.group(2)));
    }

    private static int toInteger(String content) {
        try {
            return Integer.parseInt(content);
        } catch (NumberFormatException e) {
            throw new InvalidInputFormatException();
        }
    }
}
