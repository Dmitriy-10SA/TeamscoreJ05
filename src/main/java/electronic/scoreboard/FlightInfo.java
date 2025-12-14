package electronic.scoreboard;

import java.time.LocalDateTime;

/**
 * Информация о рейсе
 *
 * @param scheduledDeparture   ожидаемое время вылета
 * @param scheduledArrival     ожидаемое время прилета
 * @param departureCityName    город вылета
 * @param arrivalCityName      город прилета
 * @param departureAirportName аэропорт вылета
 * @param arrivalAirportName   аэропорт прилета
 * @param airplaneModel        модель самолета
 * @param status               статус
 */
public record FlightInfo(
        LocalDateTime scheduledDeparture,
        LocalDateTime scheduledArrival,
        String departureCityName,
        String arrivalCityName,
        String departureAirportName,
        String arrivalAirportName,
        String airplaneModel,
        String status
) {
}
