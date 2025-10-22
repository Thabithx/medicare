package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectdb {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/medicare";
    private static final String USER = "root";
    private static final String PASSWORD = "thabith"; // your MySQL root password

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
