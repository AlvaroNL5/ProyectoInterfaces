package com.javafx.A_holamundofxml;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    
    private void shakeNode(javafx.scene.Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.playFromStart();
    }
    
    @FXML
    void handleCancelar(ActionEvent event) {
        ((Stage) txtNombre.getScene().getWindow()).close();
    }
    
    @FXML
    void handleCrear(ActionEvent event) {
        if (validarCampos()) {
            crearUsuario();
        }
    }
    
    private boolean validarCampos() {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String email = txtEmail.getText().trim();
        String rol = comboRol.getValue();
        String edadStr = txtEdad.getText().trim();
        
        StringBuilder errores = new StringBuilder();
        
        if (nombre.isEmpty()) {
            errores.append("El nombre es obligatorio\n");
            shakeNode(txtNombre);
        }
        
        if (apellidos.isEmpty()) {
            errores.append("Los apellidos son obligatorios\n");
            shakeNode(txtApellidos);
        }
        
        if (email.isEmpty()) {
            errores.append("El email es obligatorio\n");
            shakeNode(txtEmail);
        } else if (!validarEmail(email)) {
            errores.append("El email debe tener un formato válido (ejemplo: usuario@dominio.com)\n");
            shakeNode(txtEmail);
        }
        
        if (rol == null) {
            errores.append("El rol es obligatorio\n");
            shakeNode(comboRol);
        }
        
        if (edadStr.isEmpty()) {
            errores.append("La edad es obligatoria\n");
            shakeNode(txtEdad);
        } else {
            try {
                int edad = Integer.parseInt(edadStr);
                if (edad < 0 || edad > 150) {
                    errores.append("La edad debe estar entre 0 y 150 años\n");
                    shakeNode(txtEdad);
                }
            } catch (NumberFormatException e) {
                errores.append("La edad debe ser un número válido\n");
                shakeNode(txtEdad);
            }
        }
        
        if (errores.length() > 0) {
            mostrarError(errores.toString().trim());
            return false;
        }
        
        return true;
    }
    
    private boolean validarEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    private void crearUsuario() {
        String email = txtEmail.getText().trim();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkQuery = "SELECT COUNT(*) FROM USUARIO WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                mostrarError("Ya existe un usuario con este email");
                shakeNode(txtEmail);
                return;
            }
            
            String query = "INSERT INTO USUARIO (nombre, apellido, email, contraseña, tipo_usuario, edad) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, txtNombre.getText().trim());
            stmt.setString(2, txtApellidos.getText().trim());
            stmt.setString(3, email);
            stmt.setString(4, "password123");
            stmt.setString(5, comboRol.getValue());
            stmt.setInt(6, Integer.parseInt(txtEdad.getText().trim()));
            
            stmt.executeUpdate();
            
            if (cursosController != null) {
                cursosController.cargarUsuarios();
                cursosController.cargarAsistencias();
            }
            
            mostrarExito("Usuario creado correctamente");
            ((Stage) txtNombre.getScene().getWindow()).close();
            
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