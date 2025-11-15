package com.hospital.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static Properties properties = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    private static Properties loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (input == null) {
                    throw new RuntimeException("Unable to find db.properties");
                }
                properties.load(input);
            } catch (IOException e) {
                throw new RuntimeException("Error loading properties file", e);
            }
        }
        return properties;
    }

    /**
     * Returns a new, dedicated Connection for each call.
     * Callers should close the Connection (try-with-resources) when done.
     */
    public static Connection getConnection() {
        Properties props = loadProperties();
        try {
            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            if (url == null || username == null) {
                throw new RuntimeException("Database configuration properties are missing");
            }

            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }
}

