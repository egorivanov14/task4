package com.innowise.web.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private final static String URL = "jdbc:postgresql://localhost:5432/users"; //todo change url, create new DB
    private final static String USER = "postgres";
    private final static String PASSWORD = "admin";

    private Connection connection;

    public DataBaseConnection() {

        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Failed to connect."); //todo
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to close connection."); //todo
        }
    }
}