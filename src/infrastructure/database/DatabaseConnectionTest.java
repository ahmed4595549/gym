package infrastructure.database;

import java.sql.Connection;

public class DatabaseConnectionTest {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            System.out.println("Database connected successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}