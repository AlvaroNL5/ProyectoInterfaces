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

public class LoginController {
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Button btnLogin;
    
    private String passwordGuardada = "12345";
    
    @FXML
    public void initialize() {
        btnLogin.setOnAction(event -> handleLogin());
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
        
        if (email.equals("Admin") && password.equals(passwordGuardada)) {
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
                        scene.getStylesheets().addAll(btnLogin.getScene().getStylesheets());
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
    }
    
    public void setPasswordGuardada(String nuevaPassword) {
        this.passwordGuardada = nuevaPassword;
    }
}