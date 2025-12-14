package price.change;

import common.DatabaseConnector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Класс для изменения цен
 */
public class PriceUpdater {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Scanner scanner;

    public PriceUpdater(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Получение класса обслуживания по коду (1 - Business, 2 - Economy, 3 - Comfort)
     */
    private FareCondition getFareConditionByCode(int code) {
        return switch (code) {
            case 1 -> FareCondition.BUSINESS;
            case 2 -> FareCondition.ECONOMY;
            case 3 -> FareCondition.COMFORT;
            default -> throw new IllegalArgumentException("Неверный номер класса обслуживания");
        };
    }

    /**
     * Получение пользовательского ввода (с проверкой валидности данных) для изменения цен
     */
    private UserInput getUserInput() {
        System.out.println("Здравствуйте! Для изменения price и total_amount следуйте инструкции: ");
        System.out.print("1. Ввести новое значение price. Введите price: ");
        BigDecimal price = scanner.nextBigDecimal();
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше 0");
        }
        System.out.print("2. Ввести дату в формате yyyy-MM-dd. Введите дату: ");
        LocalDate date = LocalDate.parse(scanner.next(), DATE_FORMATTER);
        System.out.print("3. Ввести номер рейса (flight_id). Введите номер рейса: ");
        int flightId = scanner.nextInt();
        System.out.println("4. Выбрать класс обслуживания.");
        System.out.print("Введите цифру из списка (1 - Business, 2 - Economy, 3 - Comfort): ");
        FareCondition fareCondition = getFareConditionByCode(scanner.nextInt());
        return new UserInput(price, date, flightId, fareCondition);
    }

    /**
     * Запустить процесс ввода данных (для изменения цен) и изменения цен
     */
    public void inputDataAndExecuteUpdate() throws SQLException {
        UserInput userInput = getUserInput();
        try (Connection connection = DatabaseConnector.getConnection()) {
            PriceDao priceDao = new PriceDao(connection);
            priceDao.updatePriceInSegmentAndTotalAmount(
                    userInput.price,
                    userInput.date,
                    userInput.flightId,
                    userInput.fareCondition
            );
        }
    }

    /**
     * Пользовательский ввод
     *
     * @param price         цена
     * @param date          дата
     * @param flightId      номер рейса
     * @param fareCondition класс обслуживания
     */
    private record UserInput(BigDecimal price, LocalDate date, int flightId, FareCondition fareCondition) {
    }
}
