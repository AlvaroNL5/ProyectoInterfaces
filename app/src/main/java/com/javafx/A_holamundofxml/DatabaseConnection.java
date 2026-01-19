package com.javafx.A_holamundofxml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Muudle";
    private static final String USER = "admin";
    private static final String PASSWORD = "swY2LIZjYlvf";
    private static Connection conexion = null;

    public static Connection getConnection() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión realizada");
            }
        } catch (SQLException e) {
            System.out.println("No se conectó con la base de datos, error: " + e.getMessage());
        }
        return conexion;
    }
}