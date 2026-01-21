package com.javafx.A_holamundofxml;

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
    @FXML private Button btnCancelar;
    
    private boolean modoRegistro = false;
    private CursosController cursosController;
    
    public void setCursosController(CursosController controller) {
        this.cursosController = controller;
    }
    
    public void setModoRegistro(boolean modo) {
        this.modoRegistro = modo;
    }
    
    @FXML
    public void initialize() {
        ObservableList<String> roles = FXCollections.observableArrayList("profesor", "alumno");
        comboRol.setItems(roles);
        comboRol.setValue("alumno");
        
        txtNombre.setTooltip(new Tooltip("Introduzca el nombre del usuario"));
        txtApellidos.setTooltip(new Tooltip("Introduzca los apellidos del usuario"));
        txtEmail.setTooltip(new Tooltip("Introduzca un email valido"));
        txtPassword.setTooltip(new Tooltip("Introduzca una contrasena (minimo 4 caracteres)"));
        txtConfirmarPassword.setTooltip(new Tooltip("Repita la contrasena"));
        comboRol.setTooltip(new Tooltip("Seleccione el rol: Profesor (gestion completa) o Alumno (solo consulta)"));
        txtEdad.setTooltip(new Tooltip("Introduzca la edad (0-150)"));
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
        if (modoRegistro) {
            volverAlLogin();
        } else {
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.close();
        }
    }
    
    private void volverAlLogin() {
        try {
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            stage.setTitle("Login - Muudle");
            stage.setScene(scene);
            stage.show();
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
        } else if (!email.contains("@") || !email.contains(".")) {
            errores.append("El email debe tener un formato valido\n");
            shakeNode(txtEmail);
        }
        
        if (password.isEmpty()) {
            errores.append("La contrasena es obligatoria\n");
            shakeNode(txtPassword);
        } else if (password.length() < 4) {
            errores.append("La contrasena debe tener al menos 4 caracteres\n");
            shakeNode(txtPassword);
        }
        
        if (!password.equals(confirmarPassword)) {
            errores.append("Las contrasenas no coinciden\n");
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
                    errores.append("La edad debe estar entre 0 y 150\n");
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
    
    private void crearUsuario() {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            
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
            
            if (modoRegistro) {
                volverAlLogin();
            } else {
                if (cursosController != null) {
                    cursosController.cargarUsuarios();
                }
                Stage stage = (Stage) txtNombre.getScene().getWindow();
                stage.close();
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