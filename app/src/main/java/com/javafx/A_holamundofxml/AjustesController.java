package com.javafx.A_holamundofxml;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
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
    
    @FXML
    public void initialize() {
        cargarDatosUsuario();
        actualizarTextoBotonTema();
        
        btnTema.setTooltip(new Tooltip("Cambiar entre tema claro y oscuro"));
        txtNuevaPassword.setTooltip(new Tooltip("Introduce una nueva contraseña"));
        txtConfirmarPassword.setTooltip(new Tooltip("Confirma la nueva contraseña"));
        txtEdad.setTooltip(new Tooltip("Tu edad (0-150)"));
        txtEmail.setTooltip(new Tooltip("Tu correo electronico"));
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), lblNombre.getParent().getParent());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    
    private void cargarDatosUsuario() {
        int idUsuario = Configuracion.getIdUsuarioActual();
        if (idUsuario <= 0) {
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT nombre, apellido, email, tipo_usuario, edad FROM USUARIO WHERE id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idUsuario);
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
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleTema(ActionEvent event) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), btnTema);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.9);
        scale.setToY(0.9);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        
        FadeTransition fade = new FadeTransition(Duration.millis(150), btnTema);
        fade.setFromValue(1.0);
        fade.setToValue(0.7);
        fade.setAutoReverse(true);
        fade.setCycleCount(2);
        
        ParallelTransition parallel = new ParallelTransition(scale, fade);
        parallel.setOnFinished(e -> {
            Configuracion.setTemaOscuro(!Configuracion.isTemaOscuro());
            aplicarTemaAVentanaActual();
            actualizarTextoBotonTema();
        });
        parallel.play();
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
        boolean cambiosRealizados = aplicarCambiosDatos();
        if (cambiosRealizados) {
            mostrarExito("Datos personales guardados correctamente.");
        }
    }
    
    private boolean aplicarCambiosDatos() {
        boolean cambiosRealizados = false;
        int idUsuario = Configuracion.getIdUsuarioActual();
        
        if (idUsuario <= 0) {
            return false;
        }
        
        String nuevaPassword = txtNuevaPassword.getText();
        String confirmarPassword = txtConfirmarPassword.getText();
        String nuevaEdadStr = txtEdad.getText();
        String nuevoEmail = txtEmail.getText();

        boolean hayNuevaPassword = !nuevaPassword.isEmpty();
        boolean hayNuevoEmail = !nuevoEmail.equals(lblEmail.getText());
        boolean hayNuevaEdad = !nuevaEdadStr.equals(lblEdad.getText());
        
        if (!hayNuevaPassword && !hayNuevoEmail && !hayNuevaEdad) {
            return false; 
        }
        
        if (hayNuevaPassword) {
            if (!nuevaPassword.equals(confirmarPassword)) {
                mostrarError("Las contraseñas no coinciden");
                return false;
            }
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE USUARIO SET contraseña = ? WHERE id_usuario = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nuevaPassword);
                stmt.setInt(2, idUsuario);
                stmt.executeUpdate();
                stmt.close();
                
                txtNuevaPassword.clear();
                txtConfirmarPassword.clear();
                cambiosRealizados = true;
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarError("Error al actualizar la contraseña");
                return false;
            }
        }
        
        if (hayNuevaEdad) {
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
                    stmt.setInt(2, idUsuario);
                    stmt.executeUpdate();
                    stmt.close();
                    
                    lblEdad.setText(String.valueOf(nuevaEdad));
                    cambiosRealizados = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    mostrarError("Error al actualizar la edad");
                    return false;
                }
            } catch (NumberFormatException e) {
                mostrarError("La edad debe ser un numero valido");
                return false;
            }
        }
        
        if (hayNuevoEmail) {
            if (!validarEmail(nuevoEmail)) {
                mostrarError("El email debe ser valido (formato: usuario@dominio.com)");
                return false;
            }
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE USUARIO SET email = ? WHERE id_usuario = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nuevoEmail);
                stmt.setInt(2, idUsuario);
                stmt.executeUpdate();
                stmt.close();
                
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
            
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), btnTema.getScene().getRoot());
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
    
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        agregarIconoAlerta(alert);
        alert.showAndWait();
    }
    
    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exito");
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