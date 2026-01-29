package com.javafx.A_holamundofxml;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmarPassword;
    @FXML private ComboBox<String> comboRol;
    @FXML private TextField txtEdad;
    @FXML private Button btnCrear;
    
    private CursosController cursosController;
    private boolean esVentanaModal = false;
    
    public void setCursosController(CursosController controller) {
        this.cursosController = controller;
        this.esVentanaModal = true;
    }
    
    @FXML
    public void initialize() {
        ObservableList<String> roles = FXCollections.observableArrayList("profesor", "alumno");
        comboRol.setItems(roles);
        comboRol.setValue("alumno");
        
        // Animacion de entrada
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), txtNombre.getParent());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
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
        if (esVentanaModal) {
            // Si es ventana modal (abierta desde Cursos), simplemente cerrar
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.close();
        } else {
            // Si viene del Login, volver al Login
            volverAlLogin();
        }
    }
    
    private void volverAlLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            
            // Animacion de transicion
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), txtNombre.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                stage.setScene(scene);
                stage.setTitle("Login - Muudle");
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
        String password = txtPassword.getText().trim();
        String confirmarPassword = txtConfirmarPassword.getText().trim();
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
            errores.append("El email debe tener un formato valido (ejemplo: usuario@dominio.com)\n");
            shakeNode(txtEmail);
        }
        
        if (password.isEmpty()) {
            errores.append("La contraseña es obligatoria\n");
            shakeNode(txtPassword);
        } else if (password.length() < 4) {
            errores.append("La contraseña debe tener al menos 4 caracteres\n");
            shakeNode(txtPassword);
        }
        
        if (!password.equals(confirmarPassword)) {
            errores.append("Las contraseñas no coinciden\n");
            shakeNode(txtConfirmarPassword);
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
                errores.append("La edad debe ser un numero valido\n");
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
        String password = txtPassword.getText().trim();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkQuery = "SELECT COUNT(*) FROM USUARIO WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                mostrarError("Ya existe un usuario con este email");
                shakeNode(txtEmail);
                rs.close();
                checkStmt.close();
                return;
            }
            rs.close();
            checkStmt.close();
            
            String query = "INSERT INTO USUARIO (nombre, apellido, email, contraseña, tipo_usuario, edad) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, txtNombre.getText().trim());
            stmt.setString(2, txtApellidos.getText().trim());
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, comboRol.getValue());
            stmt.setInt(6, Integer.parseInt(txtEdad.getText().trim()));
            
            stmt.executeUpdate();
            stmt.close();
            
            mostrarExito("Usuario creado correctamente");
            
            if (esVentanaModal) {
                // Si es ventana modal, actualizar la lista y cerrar
                if (cursosController != null) {
                    cursosController.cargarUsuarios();
                }
                Stage stage = (Stage) txtNombre.getScene().getWindow();
                stage.close();
            } else {
                // Si viene del Login, volver al Login
                volverAlLogin();
            }
            
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