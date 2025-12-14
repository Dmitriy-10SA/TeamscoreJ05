package price.change;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            PriceUpdater priceUpdater = new PriceUpdater(scanner);
            priceUpdater.inputDataAndExecuteUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
