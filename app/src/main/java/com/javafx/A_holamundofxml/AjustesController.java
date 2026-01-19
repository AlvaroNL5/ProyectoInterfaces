package com.javafx.A_holamundofxml;

import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AjustesController {
    
    @FXML private Label lblNombre;
    @FXML private Label lblApellidos;
    @FXML private Label lblEmail;
    @FXML private Label lblTipo;
    @FXML private Label lblEdad;
    @FXML private PasswordField txtNuevaPassword;
    @FXML private PasswordField txtConfirmarPassword;
    @FXML private TextField txtEdad;
    @FXML private TextField txtEmail;
    @FXML private Button btnTema;
    
    private int idUsuarioActual;
    
    @FXML
    public void initialize() {
        cargarDatosUsuario();
        actualizarTextoBotonTema();
    }
    
    private void cargarDatosUsuario() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT nombre, apellido, email, tipo_usuario, edad FROM USUARIO WHERE id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                lblNombre.setText(rs.getString("nombre"));
                lblApellidos.setText(rs.getString("apellido"));
                lblEmail.setText(rs.getString("email"));
                lblTipo.setText(rs.getString("tipo_usuario"));
                lblEdad.setText(String.valueOf(rs.getInt("edad")));
                txtEdad.setText(String.valueOf(rs.getInt("edad")));
                txtEmail.setText(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleTema(ActionEvent event) {
        RotateTransition rotate = new RotateTransition(Duration.millis(300), btnTema);
        rotate.setFromAngle(0);
        rotate.setToAngle(360);
        rotate.play();
        
        Configuracion.setTemaOscuro(!Configuracion.isTemaOscuro());
        aplicarTemaAVentanaActual();
        actualizarTextoBotonTema();
        mostrarExito("Tema cambiado a " + (Configuracion.isTemaOscuro() ? "oscuro" : "claro") + " en todas las ventanas.");
    }
    
    private void aplicarTemaAVentanaActual() {
        Scene escena = btnTema.getScene();
        if (escena != null) {
            Main.aplicarTema(escena);
        }
    }
    
    private void actualizarTextoBotonTema() {
        btnTema.setText(Configuracion.isTemaOscuro() ? "Cambiar a Modo Claro" : "Cambiar a Modo Oscuro");
    }
    
    @FXML
    public void handleGuardarCambios(ActionEvent event) {
        if (aplicarCambiosDatos()) {
            mostrarExito("Datos personales guardados correctamente.");
        }
    }
    
    private boolean aplicarCambiosDatos() {
        boolean cambiosRealizados = false;
        
        String nuevaPassword = txtNuevaPassword.getText();
        String confirmarPassword = txtConfirmarPassword.getText();
        String nuevaEdadStr = txtEdad.getText();
        String nuevoEmail = txtEmail.getText();
        
        if (!nuevaPassword.isEmpty()) {
            if (!nuevaPassword.equals(confirmarPassword)) {
                mostrarError("Las contraseñas no coinciden");
                return false;
            }
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE USUARIO SET contraseña = ? WHERE id_usuario = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nuevaPassword);
                stmt.setInt(2, 1);
                stmt.executeUpdate();
                
                txtNuevaPassword.clear();
                txtConfirmarPassword.clear();
                cambiosRealizados = true;
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarError("Error al actualizar la contraseña");
                return false;
            }
        }
        
        if (!nuevaEdadStr.isEmpty()) {
            try {
                int nuevaEdad = Integer.parseInt(nuevaEdadStr);
                if (nuevaEdad < 0 || nuevaEdad > 150) {
                    mostrarError("La edad debe estar entre 0 y 150");
                    return false;
                }
                
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "UPDATE USUARIO SET edad = ? WHERE id_usuario = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, nuevaEdad);
                    stmt.setInt(2, 1);
                    stmt.executeUpdate();
                    
                    lblEdad.setText(String.valueOf(nuevaEdad));
                    cambiosRealizados = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    mostrarError("Error al actualizar la edad");
                    return false;
                }
            } catch (NumberFormatException e) {
                mostrarError("La edad debe ser un número válido");
                return false;
            }
        }
        
        if (!nuevoEmail.isEmpty()) {
            if (!validarEmail(nuevoEmail)) {
                mostrarError("El email debe ser válido (formato: usuario@dominio.com)");
                return false;
            }
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE USUARIO SET email = ? WHERE id_usuario = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nuevoEmail);
                stmt.setInt(2, 1);
                stmt.executeUpdate();
                
                lblEmail.setText(nuevoEmail);
                cambiosRealizados = true;
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarError("Error al actualizar el email");
                return false;
            }
        }
        
        return cambiosRealizados;
    }
    
    private boolean validarEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    @FXML
    public void handleCerrar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cursos.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnTema.getScene().getWindow();
            Scene scene = new Scene(root);
            
            Main.aplicarTema(scene);
            
            stage.setScene(scene);
            stage.show();
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
    
    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}