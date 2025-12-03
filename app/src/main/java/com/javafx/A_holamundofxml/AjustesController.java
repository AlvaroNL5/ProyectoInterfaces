package com.javafx.A_holamundofxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjustesController {
    
    @FXML
    private Label lblNombre;
    
    @FXML
    private Label lblApellidos;
    
    @FXML
    private Label lblEmail;
    
    @FXML
    private Label lblTipo;
    
    @FXML
    private Label lblEdad;
    
    @FXML
    private PasswordField txtNuevaPassword;
    
    @FXML
    private PasswordField txtConfirmarPassword;
    
    @FXML
    private TextField txtEdad;
    
    @FXML
    private TextField txtEmail;
    
    private String passwordActual = "12345";
    private int edadActual = 19;
    private String emailActual = "admin@gmail.com";
    
    @FXML
    public void initialize() {
        cargarDatosUsuario();
    }
    
    private void cargarDatosUsuario() {
        lblNombre.setText("Admin");
        lblApellidos.setText("Algar Morales");
        lblEmail.setText(emailActual);
        lblTipo.setText("Profesor");
        lblEdad.setText(String.valueOf(edadActual));
        txtEdad.setText(String.valueOf(edadActual));
        txtEmail.setText(emailActual);
    }
    
    @FXML
    public void handleAplicarPersonalizacion(ActionEvent event) {
        aplicarCambios();
    }
    
    @FXML
    public void handleGuardarCambios(ActionEvent event) {
        aplicarCambios();
        mostrarExito("Todos los cambios han sido guardados y aplicados.");
    }
    
    private void aplicarCambios() {
        String nuevaPassword = txtNuevaPassword.getText();
        String confirmarPassword = txtConfirmarPassword.getText();
        String nuevaEdad = txtEdad.getText();
        String nuevoEmail = txtEmail.getText();
        
        if (!nuevaPassword.isEmpty()) {
            if (!nuevaPassword.equals(confirmarPassword)) {
                mostrarError("Las contraseñas no coinciden");
                return;
            }
            passwordActual = nuevaPassword;
            mostrarExito("Contraseña actualizada. La próxima vez que inicies sesión deberás usar: " + nuevaPassword);
            txtNuevaPassword.clear();
            txtConfirmarPassword.clear();
        }
        
        if (!nuevaEdad.isEmpty()) {
            try {
                int edad = Integer.parseInt(nuevaEdad);
                if (edad < 0 || edad > 150) {
                    mostrarError("La edad debe estar entre 0 y 150");
                    return;
                }
                edadActual = edad;
                lblEdad.setText(String.valueOf(edad));
                mostrarExito("Edad actualizada a: " + edad);
            } catch (NumberFormatException e) {
                mostrarError("La edad debe ser un número válido");
                return;
            }
        }
        
        if (!nuevoEmail.isEmpty()) {
            if (!nuevoEmail.contains("@") || !nuevoEmail.contains(".")) {
                mostrarError("El email debe ser válido (debe contener @ y .)");
                return;
            }
            emailActual = nuevoEmail;
            lblEmail.setText(nuevoEmail);
            mostrarExito("Email actualizado a: " + nuevoEmail);
        }
    }
    
    @FXML
    public void handleCerrar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cursos.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}