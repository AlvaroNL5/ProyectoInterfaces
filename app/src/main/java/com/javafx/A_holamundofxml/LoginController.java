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
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        
        if (email.equals("Admin") && password.equals("12345")) {
            abrirCursosComoAdmin();
        } else {
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
                    abrirCursos(idUsuario, nombre, tipo);
                } else {
                    shakeNode(txtEmail);
                    shakeNode(txtPassword);
                    
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Credenciales incorrectas");
                    alert.setContentText("Usuario o contraseña incorrectos");
                    
                    javafx.scene.control.DialogPane dialogPane = alert.getDialogPane();
                    FadeTransition fadeInAlert = new FadeTransition(Duration.millis(200), dialogPane);
                    fadeInAlert.setFromValue(0.0);
                    fadeInAlert.setToValue(1.0);
                    alert.showAndWait();
                    fadeInAlert.play();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarError("Error de base de datos: " + e.getMessage());
            }
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
            
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void abrirCursosComoAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cursos.fxml"));
            Parent root = loader.load();
            
            CursosController cursosController = loader.getController();
            cursosController.setUsuarioActual(0, "Admin", "profesor");
            
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), btnLogin.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                try {
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    Scene scene = new Scene(root);
                    
                    Main.aplicarTema(scene);
                    
                    stage.setScene(scene);
                    
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
    
    private void abrirCursos(int idUsuario, String nombre, String tipo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cursos.fxml"));
            Parent root = loader.load();
            
            CursosController cursosController = loader.getController();
            cursosController.setUsuarioActual(idUsuario, nombre, tipo);
            
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), btnLogin.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                try {
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    Scene scene = new Scene(root);
                    
                    Main.aplicarTema(scene);
                    
                    stage.setScene(scene);
                    
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
        alert.showAndWait();
    }
}