package electronic.scoreboard.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import electronic.scoreboard.FlightInfo;
import electronic.scoreboard.utils.language.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FlightInfoMapper {
    private static final String SCHEDULED_DEPARTURE = "scheduled_departure";
    private static final String SCHEDULED_ARRIVAL = "scheduled_arrival";
    private static final String DEPARTURE_CITY = "dep_city";
    private static final String DEPARTURE_AIRPORT_NAME = "dep_airport_name";
    private static final String ARRIVAL_CITY = "arr_city";
    private static final String ARRIVAL_AIRPORT_NAME = "arr_airport_name";
    private static final String AIRPLANE_MODEL = "model";
    private static final String FLIGHT_STATUS = "status";

    private final Language language;
    private final Gson gson;

    public FlightInfoMapper(Language language) {
        this.language = language;
        this.gson = new Gson();
    }

    /**
     * Получение из Json нужной строки по выбранному языку
     */
    private String getStringInNeedLanguageFromJson(String json) {
        return switch (language) {
            case RU -> gson.fromJson(json, JsonObject.class).get("ru").getAsString();
            case EN -> gson.fromJson(json, JsonObject.class).get("en").getAsString();
        };
    }

    /**
     * Маппинг в FlightInfo из ResultSet
     */
    public FlightInfo map(ResultSet resultSet) throws SQLException {
        LocalDateTime scheduledDeparture = resultSet.getTimestamp(SCHEDULED_DEPARTURE).toLocalDateTime();
        LocalDateTime scheduledArrival = resultSet.getTimestamp(SCHEDULED_ARRIVAL).toLocalDateTime();
        String jsonDepCity = resultSet.getString(DEPARTURE_CITY);
        String jsonDepAirport = resultSet.getString(DEPARTURE_AIRPORT_NAME);
        String jsonArrivalCity = resultSet.getString(ARRIVAL_CITY);
        String jsonArrivalAirport = resultSet.getString(ARRIVAL_AIRPORT_NAME);
        String jsonAirplane = resultSet.getString(AIRPLANE_MODEL);
        String status = resultSet.getString(FLIGHT_STATUS);
        return new FlightInfo(
                scheduledDeparture,
                scheduledArrival,
                getStringInNeedLanguageFromJson(jsonDepCity),
                getStringInNeedLanguageFromJson(jsonArrivalCity),
                getStringInNeedLanguageFromJson(jsonDepAirport),
                getStringInNeedLanguageFromJson(jsonArrivalAirport),
                getStringInNeedLanguageFromJson(jsonAirplane),
                status
        );
    }
}
