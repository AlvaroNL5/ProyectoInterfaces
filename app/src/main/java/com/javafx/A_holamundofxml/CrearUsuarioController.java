package com.javafx.A_holamundofxml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrearUsuarioController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> comboRol;
    @FXML private TextField txtEdad;
    @FXML private Button btnCrear;
    
    private CursosController cursosController;
    
    public void setCursosController(CursosController controller) {
        this.cursosController = controller;
    }
    
    @FXML
    public void initialize() {
        ObservableList<String> roles = FXCollections.observableArrayList("profesor", "alumno");
        comboRol.setItems(roles);
        comboRol.setValue("alumno");
    }
    
    @FXML
    void handleCancelar(ActionEvent event) {
        ((Stage) txtNombre.getScene().getWindow()).close();
    }
    
    @FXML
    void handleCrear(ActionEvent event) {
        if (validarCampos()) {
            crearUsuario();
            ((Stage) txtNombre.getScene().getWindow()).close();
        }
    }
    
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio");
            return false;
        }
        
        if (txtApellidos.getText().trim().isEmpty()) {
            mostrarError("Los apellidos son obligatorios");
            return false;
        }
        
        if (txtEmail.getText().trim().isEmpty()) {
            mostrarError("El email es obligatorio");
            return false;
        }
        
        if (comboRol.getValue() == null) {
            mostrarError("El rol es obligatorio");
            return false;
        }
        
        if (txtEdad.getText().trim().isEmpty()) {
            mostrarError("La edad es obligatoria");
            return false;
        }
        
        try {
            int edad = Integer.parseInt(txtEdad.getText().trim());
            if (edad < 0 || edad > 150) {
                mostrarError("La edad debe estar entre 0 y 150 años");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("La edad debe ser un número válido");
            return false;
        }
        
        String email = txtEmail.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            mostrarError("El email debe tener un formato válido (ejemplo: usuario@dominio.com)");
            return false;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM USUARIO WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                mostrarError("Ya existe un usuario con este email");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al verificar el email");
            return false;
        }
        
        return true;
    }
    
    private void crearUsuario() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO USUARIO (nombre, apellido, email, contraseña, tipo_usuario, edad) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, txtNombre.getText().trim());
            stmt.setString(2, txtApellidos.getText().trim());
            stmt.setString(3, txtEmail.getText().trim());
            stmt.setString(4, "password123");
            stmt.setString(5, comboRol.getValue());
            stmt.setInt(6, Integer.parseInt(txtEdad.getText().trim()));
            
            stmt.executeUpdate();
            
            if (cursosController != null) {
                cursosController.cargarUsuarios();
            }
            
            mostrarExito("Usuario creado correctamente");
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al crear el usuario: " + e.getMessage());
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