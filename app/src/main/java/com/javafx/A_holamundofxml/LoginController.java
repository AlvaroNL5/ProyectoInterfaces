package com.javafx.A_holamundofxml;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    
    private void handleLogin() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        
        if (email.equals("Admin") && password.equals(passwordGuardada)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cursos.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Credenciales incorrectas");
            alert.setContentText("Usuario o contraseña incorrectos");
            alert.showAndWait();
        }
    }
    
    public void setPasswordGuardada(String nuevaPassword) {
        this.passwordGuardada = nuevaPassword;
    }
}