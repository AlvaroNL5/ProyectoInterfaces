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
    
    private String passwordActual = "12345";
    private int edadActual = 19;
    private String emailActual = "admin@gmail.com";
    
    @FXML
    public void initialize() {
        cargarDatosUsuario();
        actualizarTextoBotonTema();
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
    public void handleTema(ActionEvent event) {
        RotateTransition rotate = new RotateTransition(Duration.millis(300), btnTema);
        rotate.setFromAngle(0);
        rotate.setToAngle(360);
        rotate.play();
        
        Configuracion.setTemaOscuro(!Configuracion.isTemaOscuro());
        aplicarTemaATodasLasVentanas();
        actualizarTextoBotonTema();
        mostrarExito("Tema cambiado a " + (Configuracion.isTemaOscuro() ? "oscuro" : "claro") + " en todas las ventanas.");
    }
    
    private void aplicarTemaATodasLasVentanas() {
        aplicarTemaAVentanaActual();
    }
    
    private void aplicarTemaAVentanaActual() {
        Scene escena = btnTema.getScene();
        if (escena != null) {
            escena.getStylesheets().clear();
            if (Configuracion.isTemaOscuro()) {
                escena.getStylesheets().add(getClass().getResource("/estilos_oscuro.css").toExternalForm());
            } else {
                escena.getStylesheets().add(getClass().getResource("/estilos_claro.css").toExternalForm());
            }
        }
    }
    
    private void actualizarTextoBotonTema() {
        btnTema.setText(Configuracion.isTemaOscuro() ? "🌙 Cambiar a Modo Claro" : "☀️ Cambiar a Modo Oscuro");
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
            if (!nuevaPassword.equals(passwordActual)) {
                passwordActual = nuevaPassword;
                mostrarExito("Contraseña actualizada correctamente.");
                txtNuevaPassword.clear();
                txtConfirmarPassword.clear();
                cambiosRealizados = true;
            }
        }
        
        if (!nuevaEdadStr.isEmpty()) {
            try {
                int nuevaEdad = Integer.parseInt(nuevaEdadStr);
                if (nuevaEdad < 0 || nuevaEdad > 150) {
                    mostrarError("La edad debe estar entre 0 y 150");
                    return false;
                }
                if (nuevaEdad != edadActual) {
                    edadActual = nuevaEdad;
                    lblEdad.setText(String.valueOf(nuevaEdad));
                    mostrarExito("Edad actualizada a: " + nuevaEdad);
                    cambiosRealizados = true;
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
            if (!nuevoEmail.equals(emailActual)) {
                emailActual = nuevoEmail;
                lblEmail.setText(nuevoEmail);
                mostrarExito("Email actualizado a: " + nuevoEmail);
                cambiosRealizados = true;
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
            
            CursosController cursosController = loader.getController();
            Stage stage = (Stage) btnTema.getScene().getWindow();
            Scene scene = new Scene(root);
            
            if (Configuracion.isTemaOscuro()) {
                scene.getStylesheets().add(getClass().getResource("/estilos_oscuro.css").toExternalForm());
            } else {
                scene.getStylesheets().add(getClass().getResource("/estilos_claro.css").toExternalForm());
            }
            
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