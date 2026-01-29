package com.javafx.A_holamundofxml;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Button btnLogin;
    
    @FXML
    private Button btnRegistro;
    
    @FXML
    public void initialize() {
        btnLogin.setOnAction(event -> handleLogin());
        btnRegistro.setOnAction(event -> handleRegistro());
        
        // Animacion de entrada
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), txtEmail.getParent());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    
    private void shakeNode(Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.playFromStart();
    }
    
    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) shakeNode(txtEmail);
            if (password.isEmpty()) shakeNode(txtPassword);
            mostrarError("Por favor, introduce email y contraseña");
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id_usuario, nombre, tipo_usuario FROM USUARIO WHERE email = ? AND contraseña = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo_usuario");
                
                // Guardar datos en Configuracion
                Configuracion.setIdUsuarioActual(idUsuario);
                Configuracion.setNombreUsuarioActual(nombre);
                Configuracion.setTipoUsuarioActual(tipo);
                
                rs.close();
                stmt.close();
                
                abrirCursos();
            } else {
                shakeNode(txtEmail);
                shakeNode(txtPassword);
                mostrarError("Usuario o contraseña incorrectos");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error de base de datos: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRegistro() {
        try {
            Stage stage = (Stage) btnRegistro.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearUsuario.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            
            // Animacion de transicion
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), btnRegistro.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                stage.setScene(scene);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(200), root);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void abrirCursos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cursos.fxml"));
            Parent root = loader.load();
            
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), btnLogin.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                try {
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    Scene scene = new Scene(root);
                    Main.aplicarTema(scene);
                    stage.setScene(scene);
                    stage.setTitle("Muudle - " + Configuracion.getNombreUsuarioActual());
                    
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();
                    stage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            fadeOut.play();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        agregarIconoAlerta(alert);
        alert.showAndWait();
    }
    
    private void agregarIconoAlerta(Alert alert) {
        try {
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/muudle.png")));
        } catch (Exception e) {
        }
    }
}