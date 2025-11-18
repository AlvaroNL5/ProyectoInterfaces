package com.javafx.A_holamundofxml;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
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
    private ColorPicker colorPicker;
    
    @FXML
    private ComboBox<String> comboFuente;
    
    @FXML
    private ComboBox<String> comboEstilo;
    
    @FXML
    private PasswordField txtNuevaPassword;
    
    @FXML
    private PasswordField txtConfirmarPassword;
    
    @FXML
    private TextField txtEdad;
    
    @FXML
    public void initialize() {
        cargarDatosUsuario();
    }
    
    private void cargarDatosUsuario() {
        lblNombre.setText("Manuel");
        lblApellidos.setText("García"); //Ejemplo
        lblEmail.setText("prof.manuel@muudle.com");
        lblTipo.setText("Profesor");
        lblEdad.setText("35");
    }
    
    @FXML
    public void handleAplicarPersonalizacion() {
    }
    
    @FXML
    public void handleGuardarCambios() {
    }
    
    @FXML
    public void handleCerrar() {
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
}