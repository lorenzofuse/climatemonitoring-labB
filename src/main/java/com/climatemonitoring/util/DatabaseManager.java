package com.climatemonitoring.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    //fisso 5433 msi 5432
    private static final String DB_URL = "jdbc:postgresql://localhost:5433/ClimateMonitoring";
    private static final String DB_USER = "postgres";
    private static final String DB_PSW = "postgre";
    private static DatabaseManager instance;
    private Connection connection;

    public DatabaseManager() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        try {
            Connection conn = dbManager.getConnection();
            if (conn != null) {
                System.out.println("Connection to the database was successful!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection();
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PSW);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
