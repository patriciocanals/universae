package com.universae;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connect() {
        String url = "jdbc:sqlite:src/main/resources/db/meditrack.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("conn a sqlite exitosa");
        } catch (SQLException e) {
            System.out.println("error " + e.getMessage());
        }
        return conn;
    }
}