package price.change;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class PriceDao {
    /**
     * Обновление price по дате, flight_id и fare_conditions
     */
    private static final String UPDATE_PRICE_IN_SEGMENT = """
            UPDATE segments
            SET price = ?
            FROM tickets
                     JOIN bookings.bookings ON tickets.book_ref = bookings.book_ref
            WHERE segments.ticket_no = tickets.ticket_no
              AND DATE(bookings.book_date) = ?
              AND segments.flight_id = ?
              AND segments.fare_conditions = ?;
            """;

    /**
     * Обновление total_amount по дате
     */
    private static final String UPDATE_TOTAL_AMOUNT = """
            UPDATE bookings
            SET total_amount = sum_query.total_sum
            FROM (SELECT tickets.book_ref AS tickets_book_ref, SUM(segments.price) AS total_sum
                  FROM tickets
                           JOIN segments ON tickets.ticket_no = segments.ticket_no
                           JOIN bookings.bookings on bookings.book_ref = tickets.book_ref
                  WHERE DATE(bookings.book_date) = ?
                  GROUP BY tickets.book_ref) AS sum_query
            WHERE bookings.book_ref = sum_query.tickets_book_ref;
            """;

    private final Connection connection;

    public PriceDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Метод для обновления total_amount после обновления price
     */
    private void updateTotalAmount(LocalDate date) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TOTAL_AMOUNT)) {
            statement.setDate(1, java.sql.Date.valueOf(date));
            statement.executeUpdate();
        }
    }

    /**
     * Метод для обновления price по дате, flight_id и fare_conditions и (после) total_amount
     */
    public void updatePriceInSegmentAndTotalAmount(
            BigDecimal price,
            LocalDate date,
            int flightId,
            FareCondition fareConditions
    ) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PRICE_IN_SEGMENT)) {
            statement.setBigDecimal(1, price);
            statement.setDate(2, java.sql.Date.valueOf(date));
            statement.setInt(3, flightId);
            statement.setString(4, fareConditions.getValue());
            System.out.println("Обновление может занять некоторое время...");
            statement.executeUpdate();
            updateTotalAmount(date);
            System.out.println("Обновление успешно завершено.");
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка. Откат транзакции.");
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }
}
