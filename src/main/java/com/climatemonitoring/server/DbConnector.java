package com.climatemonitoring.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    private static final String DB_URL="jdbc:postgresql://localhost:5432/ClimateMonitoring";
    private static final String DB_USER="postgres";
    private static final String DB_PSW="postgre";

    public static Connection connect() throws SQLException{
        try{
            // Carica il driver PostgreSQL
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            throw new SQLException("Driver PostgreSQL not found >",e);
        }

        //connessione al db
        return DriverManager.getConnection(DB_URL,DB_USER,DB_PSW);
    }
}
