package com.javafx.A_holamundofxml;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    
    private static Connection connection = null;
    
    // Bloque estatico que carga la configuracion al iniciar la clase
    static {
        cargarConfiguracion();
    }
    
    private static void cargarConfiguracion() {
        Properties properties = new Properties();
        
        try (InputStream input = DatabaseConnection.class.getResourceAsStream("/ip.properties")) {
            if (input != null) {
                properties.load(input);
                
                String ip = properties.getProperty("db.ip", "localhost");
                String port = properties.getProperty("db.port", "3306");
                String dbName = properties.getProperty("db.name", "Muudle");
                
                URL = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
                USER = properties.getProperty("db.user", "admin");
                PASSWORD = properties.getProperty("db.password", "");
                
                System.out.println("Conectando a: " + URL);
            } else {
                // Si no encuentra el archivo, usa valores por defecto (localhost)
                System.out.println("No se encontro ip.properties, usando configuracion por defecto (localhost)");
                URL = "jdbc:mysql://localhost:3306/Muudle";
                USER = "admin";
                PASSWORD = "KkKdPfWukwB3";
            }
        } catch (IOException e) {
            System.err.println("Error al cargar ip.properties: " + e.getMessage());
            // Valores por defecto en caso de error
            URL = "jdbc:mysql://localhost:3306/Muudle";
            USER = "admin";
            PASSWORD = "KkKdPfWukwB3";
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}