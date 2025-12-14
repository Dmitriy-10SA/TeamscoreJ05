package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private DatabaseConnector() {
    }

    private static final String URL = "jdbc:postgresql://localhost:5432/demo";
    private static final String USER = "demo_user";
    private static final String PASSWORD = "demo_password";

    /**
     * Получение соединения с БД
     * url: "jdbc:postgresql://localhost:5432/demo"
     * user: demo_user
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
