package store.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import store.domain.store.Promotion;
import store.domain.store.Stock;
import store.exception.state.InvalidFileException;
import store.exception.state.InvalidPromotionException;

public class FileParser {

    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";

    private static final String DELIMITER = ",";
    private static final String EMPTY_PROMOTION = "null";

    private FileParser() {
    }

    private static FileReader openFile(String path) {
        try {
            File file = new File(path);
            return new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new InvalidFileException();
        }
    }

    public static List<Promotion> readPromotions() {
        FileReader fileReader = openFile(PROMOTION_FILE_PATH);
        List<Promotion> promotions = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                promotions.add(readPromotion(line));
            }
        } catch (IOException e) {

        }
        return promotions;
    }

    private static Promotion readPromotion(String line) {
        FilePromotion filePromotion = FilePromotion.of(line.split(DELIMITER));
        return new Promotion(
            filePromotion.name,
            filePromotion.buy,
            filePromotion.get,
            filePromotion.startDate,
            filePromotion.endDate
        );
    }

    public static List<Stock> readProducts(List<Promotion> promotions) {
        FileReader fileReader = openFile(PRODUCT_FILE_PATH);
        List<Stock> stocks = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                stocks.add(readProduct(line, promotions));
            }
        } catch (IOException e) {

        }
        return stocks;
    }

    private static Stock readProduct(String line, List<Promotion> promotions) {
        String[] split = line.split(DELIMITER);
        if (Objects.equals(split[3], EMPTY_PROMOTION)) {
            FileStock fileStock = FileStock.of(split, null);
            return convertToStock(fileStock);
        }
        Promotion existPromotion = findExistPromotion(promotions, split);
        FileStock fileStock = FileStock.of(split, existPromotion);
        return convertToStock(fileStock);
    }

    private static Promotion findExistPromotion(List<Promotion> promotions, String[] split) {
        return promotions.stream()
            .filter(promotion -> Objects.equals(promotion.getName(), split[3]))
            .findAny()
            .orElseThrow(InvalidPromotionException::new);
    }

    private static Stock convertToStock(FileStock fileStock) {
        return new Stock(
            fileStock.name,
            fileStock.price,
            fileStock.quantity,
            fileStock.promotion
        );
    }

    private record FilePromotion(
        String name,
        int buy,
        int get,
        LocalDate startDate,
        LocalDate endDate
    ) {

        private static FilePromotion of(String[] split) {
            return new FilePromotion(
                split[0],
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]),
                LocalDate.parse(split[3]),
                LocalDate.parse(split[4])
            );
        }
    }

    private record FileStock(
        String name,
        int price,
        int quantity,
        Promotion promotion
    ) {

        private static FileStock of(String[] split, Promotion promotion) {
            return new FileStock(
                split[0],
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]),
                promotion
            );
        }
    }
}
