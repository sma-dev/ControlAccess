package org.example;

import java.sql.*;

public class RoomController {
    private static final String DATABASE_URL = "jdbc:postgresql://jdroid.ru/access_db";

    private static final String USER = "operator1";
    private static final String PASSWORD = "Ja8UAw8YTQ";

    private static Connection connectionInstance;

    public Connection getInstance() throws SQLException, ClassNotFoundException {
        if (connectionInstance == null) {
            System.out.println("Creating connection to database...");
            Class.forName("org.postgresql.Driver");
            connectionInstance = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        }
        return connectionInstance;
    }


    public void closeConnection() {
        try {
            if (connectionInstance != null && !connectionInstance.isClosed()) {
                connectionInstance.close();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
