package electronic.scoreboard.utils;

import electronic.scoreboard.FlightInfo;
import electronic.scoreboard.utils.language.Language;
import electronic.scoreboard.utils.language.LanguageTextPrinter;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlightBoardPrinter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final String[] HEADERS = {"Departure", "Arrival", "From", "To", "Airplane", "Status"};

    /**
     * Расчет ширины столбцов
     */
    private static int[] calculateFlightBoardColumnWidths(List<FlightInfo> flightInfoList) {
        int[] width = new int[6];
        for (int i = 0; i < HEADERS.length; i++) {
            width[i] = HEADERS[i].length();
        }
        for (FlightInfo f : flightInfoList) {
            width[0] = Math.max(width[0], f.scheduledDeparture().format(DATE_TIME_FORMATTER).length());
            width[1] = Math.max(width[1], f.scheduledArrival().format(DATE_TIME_FORMATTER).length());
            width[2] = Math.max(width[2], (f.departureCityName() + " (" + f.departureAirportName() + ")").length());
            width[3] = Math.max(width[3], (f.arrivalCityName() + " (" + f.arrivalAirportName() + ")").length());
            width[4] = Math.max(width[4], f.airplaneModel().length());
            width[5] = Math.max(width[5], f.status().length());
        }
        return width;
    }

    /**
     * Разделитель
     */
    private static String separator(int[] w) {
        StringBuilder sb = new StringBuilder();
        sb.append('+');
        for (int width : w) {
            sb.append("-".repeat(width + 2)).append('+');
        }
        return sb.toString();
    }

    /**
     * Получение формата строки по максимальной ширине столбцов
     */
    private static String getFlightBoardFormat(int[] width) {
        return String.format(
                "| %%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds |%n",
                width[0],
                width[1],
                width[2],
                width[3],
                width[4],
                width[5]
        );
    }

    /**
     * Печать электронного табло
     */
    public static void printFlightBoard(Language language, List<FlightInfo> flights) {
        if (flights.isEmpty()) {
            LanguageTextPrinter.printTextWithNewLine(
                    language,
                    "Нет рейсов на указанную дату и аэропорт.",
                    "No flights for the specified date and airport."
            );
        } else {
            int[] width = calculateFlightBoardColumnWidths(flights);
            String separator = separator(width);
            String format = getFlightBoardFormat(width);
            System.out.println(separator);
            System.out.printf(format, (Object[]) HEADERS);
            System.out.println(separator);
            for (FlightInfo f : flights) {
                System.out.printf(
                        format,
                        f.scheduledDeparture().format(DATE_TIME_FORMATTER),
                        f.scheduledArrival().format(DATE_TIME_FORMATTER),
                        f.departureCityName() + " (" + f.departureAirportName() + ")",
                        f.arrivalCityName() + " (" + f.arrivalAirportName() + ")",
                        f.airplaneModel(),
                        f.status()
                );
            }
            System.out.println(separator);
        }
    }
}
