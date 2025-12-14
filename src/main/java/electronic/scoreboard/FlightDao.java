package electronic.scoreboard;

import electronic.scoreboard.utils.FlightInfoMapper;
import electronic.scoreboard.utils.language.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightDao {
    private static final String AIRPORT_CODE_REGEX = "[A-Z]{3}";

    /**
     * Запрос на получение списка рейсов, включающем информацию:
     * - время вылета/прилета
     * - из/в какой аэропорт и город
     * - название самолета
     * - статус вылета
     * по введенному пользователем коду аэропорта (вылета/прилета) и дате (вылета/прилета)
     */
    private static final String GET_FLIGHT_BOARD = """
            SELECT flights.scheduled_departure,
                   flights.scheduled_arrival,
                   dep_airport.city AS dep_city,
                   dep_airport.airport_name AS dep_airport_name,
                   arr_airport.city AS arr_city,
                   arr_airport.airport_name AS arr_airport_name,
                   airplanes_data.model,
                   flights.status
            FROM flights
                     JOIN routes ON flights.route_no = routes.route_no
                     JOIN airports_data AS dep_airport ON routes.departure_airport = dep_airport.airport_code
                     JOIN airports_data AS arr_airport ON routes.arrival_airport = arr_airport.airport_code
                     JOIN airplanes_data ON routes.airplane_code = airplanes_data.airplane_code
            WHERE (dep_airport.airport_code = ? OR arr_airport.airport_code = ?)
              AND (DATE(flights.scheduled_departure) = ? OR DATE(flights.scheduled_arrival) = ?)
            ORDER BY flights.scheduled_departure
            """;

    private final Connection connection;
    private final FlightInfoMapper flightInfoMapper;

    public FlightDao(Connection connection, Language language) {
        this.connection = connection;
        this.flightInfoMapper = new FlightInfoMapper(language);
    }

    /**
     * Получение списка, содержащего информацию о рейсах по коду аэропорта (вылета/прилета) и дате (вылета/прилета)
     */
    public List<FlightInfo> getFlightInfoList(String airportCode, LocalDate date) throws SQLException {
        if (airportCode != null && !airportCode.matches(AIRPORT_CODE_REGEX)) {
            throw new IllegalArgumentException("Not valid airport code");
        }
        try (PreparedStatement statement = connection.prepareStatement(GET_FLIGHT_BOARD)) {
            statement.setString(1, airportCode);
            statement.setString(2, airportCode);
            statement.setDate(3, java.sql.Date.valueOf(date));
            statement.setDate(4, java.sql.Date.valueOf(date));
            ResultSet resultSet = statement.executeQuery();
            List<FlightInfo> flightInfos = new ArrayList<>();
            while (resultSet.next()) {
                flightInfos.add(flightInfoMapper.map(resultSet));
            }
            return flightInfos;
        }
    }
}