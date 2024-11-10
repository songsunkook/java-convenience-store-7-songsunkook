package store.dto;

import store.exception.argument.InvalidInputFormatException;

public record OrderRequest(String name, int quantity) {

    public static OrderRequest from(String input) {
        // TODO: 정규식
        try {
            String[] split = input.split("-");
            String name = split[0].substring(1);
            int count = Integer.parseInt(split[1].substring(0, split[1].length() - 1));
            return new OrderRequest(name, count);
        } catch (Exception e) {
            throw new InvalidInputFormatException();
        }
    }
}
