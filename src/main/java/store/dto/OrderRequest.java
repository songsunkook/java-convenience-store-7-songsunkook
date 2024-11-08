package store.dto;

public record OrderRequest(String name, int quantity) {

    public static OrderRequest from(String input) {
        String[] split = input.split("-");
        String name = split[0];
        int count = Integer.parseInt(split[1]);
        return new OrderRequest(name, count);
    }
}
