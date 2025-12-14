package electronic.scoreboard;

import electronic.scoreboard.utils.DatabaseConnector;
import electronic.scoreboard.utils.FlightBoardPrinter;
import electronic.scoreboard.utils.language.Language;
import electronic.scoreboard.utils.language.LanguageTextPrinter;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Электронное табло
 */
public class FlightBoard {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Scanner scanner;
    private final Language language;

    private boolean isRunning;

    public FlightBoard(Scanner scanner, Language language) {
        this.scanner = scanner;
        this.language = language;
        this.isRunning = true;
    }

    /**
     * Получение пользовательского ввода
     */
    private UserInput getUserInput(Scanner scanner) {
        LanguageTextPrinter.printTextWithoutNewLine(
                language,
                "Введите код аэропорта: ",
                "Enter the airport code: "
        );
        String airportCode = scanner.next();
        LanguageTextPrinter.printTextWithoutNewLine(
                language,
                "Введите дату (в формате ГГГГ-ММ-ДД): ",
                "Enter the date (in YYYY-MM-DD format): "
        );
        String dateString = scanner.next();
        LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
        return new UserInput(airportCode, date);
    }

    /**
     * Получение списка информации о рейсах
     *
     * @see FlightInfo
     */
    private List<FlightInfo> getFlightInfoList(String airportCode, LocalDate date) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            FlightDao flightDao = new FlightDao(connection, language);
            return flightDao.getFlightInfoList(airportCode, date);
        }
    }

    /**
     * Запуск электронного табло
     */
    public void start() throws SQLException {
        isRunning = true;
        while (isRunning) {
            LanguageTextPrinter.printTextWithoutNewLine(
                    language,
                    "Введите 1, если хотите получить электронное табло (или 0 для выхода): ",
                    "Enter 1 to get the electronic scoreboard (or 0 to exit): "
            );
            int choice = scanner.nextInt();
            if (choice == 0) {
                stop();
            } else if (choice == 1) {
                try {
                    UserInput userInput = getUserInput(scanner);
                    List<FlightInfo> flightInfoList = getFlightInfoList(userInput.airportCode, userInput.date);
                    FlightBoardPrinter.printFlightBoard(language, flightInfoList);
                } catch (DateTimeParseException e) {
                    LanguageTextPrinter.printTextWithNewLine(
                            language,
                            "Упс! Кажется, вы ввели неправильную дату. Попробуйте снова.",
                            "Oops! It seems you entered the wrong date. Try again."
                    );
                } catch (IllegalArgumentException e) {
                    LanguageTextPrinter.printTextWithNewLine(
                            language,
                            "Упс! Кажется, вы ввели неправильный код аэропорта. Попробуйте снова.",
                            "Oops! It seems you entered the wrong airport code. Try again."
                    );
                }
            } else {
                LanguageTextPrinter.printTextWithNewLine(
                        language,
                        "Можно вводить только 1 или 0. Попробуйте снова.",
                        "Only 1 or 0 can be entered. Try again."
                );
            }
        }
    }

    /**
     * Завершение работы электронного табло
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Пользовательский ввод
     *
     * @param airportCode код аэропорта
     * @param date        дата
     */
    private record UserInput(String airportCode, LocalDate date) {
    }
}