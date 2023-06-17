package delivery.backend.dal;

import java.sql.*;

public class DataBase {
    private static final String DB_URL = "jdbc:sqlite:delivery_database.sqlite";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
