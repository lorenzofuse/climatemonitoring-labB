package com.climatemonitoring.util;


import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/ClimateMonitoring";
    private static final String DB_USER = "postgres";
    private static final String DB_PSW="postgre";

    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection != null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PSW);
        }
        return connection;
    }

    public void closeConnection(){
        try{
            if(connection !=null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query, Object... params)throws SQLException{
     Connection conn = getConnection();
     PreparedStatement pstmt = conn.prepareStatement(query);
     for(int i=0;i<params.length;i++){
         pstmt.setObject(i+1,params[i]);
     }
     return pstmt.executeQuery();
    }

    public int executeUpdate(String query, Object... params)throws SQLException{
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);
        for(int i=0;i<params.length;i++){
            pstmt.setObject(i+1,params[i]);
        }
        return pstmt.executeUpdate();
    }

}
