package electronic.scoreboard;

import electronic.scoreboard.utils.language.Language;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static Language getLanguageByLanguageCode(int languageCode) {
        return switch (languageCode) {
            case 1 -> Language.RU;
            case 2 -> Language.EN;
            default -> throw new IllegalArgumentException("Неверный выбор языка/Wrong language choice");
        };
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Выберите язык для вывода электронного табло/" +
                    "Choose the language for the electronic scoreboard (1 - RU, 2 - EN): ");
            Language language = getLanguageByLanguageCode(scanner.nextInt());
            FlightBoard printer = new FlightBoard(scanner, language);
            try {
                printer.start();
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            } finally {
                printer.stop();
            }
        }
    }
}
