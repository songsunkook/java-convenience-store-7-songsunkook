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
        String[] split = line.split(",");
        return new Promotion(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]),
            LocalDate.parse(split[3]), LocalDate.parse(split[4]));
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
        String[] split = line.split(",");
        if (Objects.equals(split[3], "null")) {
            return new Stock(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), null);
        }
        Promotion found = promotions.stream()
            .filter(promotion -> Objects.equals(promotion.getName(), split[3]))
            .findAny()
            .orElseThrow(InvalidPromotionException::new);
        return new Stock(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), found);
    }
}
