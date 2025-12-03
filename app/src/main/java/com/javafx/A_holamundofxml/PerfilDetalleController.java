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

public class PerfilDetalleController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField txtEdad;
    
    private CursosController cursosController;
    private CursosController.Usuario usuario;
    
    public void setCursosController(CursosController controller) {
        this.cursosController = controller;
    }
    
    @FXML
    public void initialize() {
        ObservableList<String> tipos = FXCollections.observableArrayList("profesor", "alumno");
        comboTipo.setItems(tipos);
    }
    
    public void cargarDatosUsuario(CursosController.Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            txtNombre.setText(usuario.getNombre());
            txtApellidos.setText(usuario.getApellidos());
            txtEmail.setText(usuario.getEmail());
            comboTipo.setValue(usuario.getTipo());
            txtEdad.setText(String.valueOf(usuario.getEdad()));
        } else {
            txtNombre.setText("No disponible");
            txtApellidos.setText("No disponible");
            txtEmail.setText("No disponible");
            comboTipo.setValue("alumno");
            txtEdad.setText("0");
        }
    }
    
    @FXML
    void handleCerrar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void handleAplicarCambios(ActionEvent event) {
        actualizarUsuario();
    }
    
    @FXML
    void handleGuardarCambios(ActionEvent event) {
        if (actualizarUsuario()) {
            mostrarExito("Cambios guardados correctamente");
        }
    }
    
    private boolean actualizarUsuario() {
        if (usuario == null) {
            mostrarError("No hay usuario seleccionado");
            return false;
        }
        
        String nuevoNombre = txtNombre.getText().trim();
        String nuevosApellidos = txtApellidos.getText().trim();
        String nuevoEmail = txtEmail.getText().trim();
        String nuevoTipo = comboTipo.getValue();
        String nuevaEdadStr = txtEdad.getText().trim();
        
        if (nuevoNombre.isEmpty() || nuevosApellidos.isEmpty() || nuevoEmail.isEmpty() || nuevoTipo == null || nuevaEdadStr.isEmpty()) {
            mostrarError("Todos los campos son obligatorios");
            return false;
        }
        
        int nuevaEdad;
        try {
            nuevaEdad = Integer.parseInt(nuevaEdadStr);
            if (nuevaEdad < 0 || nuevaEdad > 150) {
                mostrarError("La edad debe estar entre 0 y 150 años");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("La edad debe ser un número válido");
            return false;
        }
        
        if (!nuevoEmail.contains("@") || !nuevoEmail.contains(".")) {
            mostrarError("El email debe tener un formato válido (ejemplo: usuario@dominio.com)");
            return false;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (!nuevoEmail.equals(usuario.getEmail())) {
                String checkQuery = "SELECT COUNT(*) FROM USUARIO WHERE email = ? AND id_usuario != ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, nuevoEmail);
                checkStmt.setInt(2, usuario.getIdUsuario());
                var rs = checkStmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    mostrarError("Ya existe otro usuario con este email");
                    return false;
                }
            }
            
            String query = "UPDATE USUARIO SET nombre = ?, apellido = ?, email = ?, tipo_usuario = ?, edad = ? WHERE id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nuevoNombre);
            stmt.setString(2, nuevosApellidos);
            stmt.setString(3, nuevoEmail);
            stmt.setString(4, nuevoTipo);
            stmt.setInt(5, nuevaEdad);
            stmt.setInt(6, usuario.getIdUsuario());
            
            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas > 0) {
                if (cursosController != null) {
                    cursosController.cargarUsuarios();
                    cursosController.cargarAsistencias();
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al actualizar el usuario: " + e.getMessage());
        }
        return false;
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