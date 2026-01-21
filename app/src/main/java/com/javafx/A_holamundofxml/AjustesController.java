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
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmailEditar;
    @FXML private PasswordField txtNuevaPassword;
    @FXML private PasswordField txtConfirmarPassword;
    @FXML private TextField txtEdad;
    @FXML private Button btnTema;
    
    @FXML
    public void initialize() {
        cargarDatosUsuario();
        actualizarTextoBotonTema();
        
        if (txtNombre != null) txtNombre.setTooltip(new Tooltip("Introduzca su nombre"));
        if (txtApellidos != null) txtApellidos.setTooltip(new Tooltip("Introduzca sus apellidos"));
        if (txtEmailEditar != null) txtEmailEditar.setTooltip(new Tooltip("Introduzca su email"));
        if (txtNuevaPassword != null) txtNuevaPassword.setTooltip(new Tooltip("Nueva contrasena (dejar vacio para no cambiar)"));
        if (txtConfirmarPassword != null) txtConfirmarPassword.setTooltip(new Tooltip("Confirmar nueva contrasena"));
        if (txtEdad != null) txtEdad.setTooltip(new Tooltip("Edad (0-150)"));
        if (btnTema != null) btnTema.setTooltip(new Tooltip("Cambiar entre tema claro y oscuro"));
    }
    
    private void cargarDatosUsuario() {
        int idUsuario = Configuracion.getIdUsuarioActual();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT nombre, apellido, email, tipo_usuario, edad FROM USUARIO WHERE id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellido");
                String email = rs.getString("email");
                String tipo = rs.getString("tipo_usuario");
                int edad = rs.getInt("edad");
                
                if (lblNombre != null) lblNombre.setText(nombre);
                if (lblApellidos != null) lblApellidos.setText(apellidos);
                if (lblEmail != null) lblEmail.setText(email);
                if (lblTipo != null) lblTipo.setText(tipo.substring(0, 1).toUpperCase() + tipo.substring(1));
                if (lblEdad != null) lblEdad.setText(String.valueOf(edad));
                
                if (txtNombre != null) txtNombre.setText(nombre);
                if (txtApellidos != null) txtApellidos.setText(apellidos);
                if (txtEmailEditar != null) txtEmailEditar.setText(email);
                if (txtEdad != null) txtEdad.setText(String.valueOf(edad));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleTema(ActionEvent event) {
        RotateTransition rotate = new RotateTransition(Duration.millis(300), btnTema);
        rotate.setByAngle(360);
        rotate.setOnFinished(e -> {
            Configuracion.setTemaOscuro(!Configuracion.isTemaOscuro());
            aplicarTemaAVentanaActual();
            actualizarTextoBotonTema();
        });
        rotate.play();
    }
    
    private void aplicarTemaAVentanaActual() {
        Scene escena = btnTema.getScene();
        if (escena != null) {
            Main.aplicarTema(escena);
        }
    }
    
    private void actualizarTextoBotonTema() {
        if (btnTema != null) {
            if (Configuracion.isTemaOscuro()) {
                btnTema.setText("Cambiar a Modo Claro");
            } else {
                btnTema.setText("Cambiar a Modo Oscuro");
            }
        }
    }
    
    @FXML
    public void handleGuardarCambios(ActionEvent event) {
        if (validarCampos()) {
            if (guardarCambios()) {
                mostrarExito("Datos guardados correctamente.");
                cargarDatosUsuario();
                
                if (txtNombre != null && !txtNombre.getText().trim().isEmpty()) {
                    Configuracion.setNombreUsuarioActual(txtNombre.getText().trim());
                }
            }
        }
    }
    
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();
        
        if (txtNombre != null && txtNombre.getText().trim().isEmpty()) {
            errores.append("El nombre es obligatorio\n");
        }
        
        if (txtApellidos != null && txtApellidos.getText().trim().isEmpty()) {
            errores.append("Los apellidos son obligatorios\n");
        }
        
        if (txtEmailEditar != null) {
            String email = txtEmailEditar.getText().trim();
            if (email.isEmpty()) {
                errores.append("El email es obligatorio\n");
            } else if (!email.contains("@") || !email.contains(".")) {
                errores.append("El email debe tener un formato valido\n");
            }
        }
        
        if (txtNuevaPassword != null && txtConfirmarPassword != null) {
            String nuevaPassword = txtNuevaPassword.getText();
            String confirmarPassword = txtConfirmarPassword.getText();
            
            if (!nuevaPassword.isEmpty()) {
                if (nuevaPassword.length() < 4) {
                    errores.append("La contrasena debe tener al menos 4 caracteres\n");
                }
                if (!nuevaPassword.equals(confirmarPassword)) {
                    errores.append("Las contrasenas no coinciden\n");
                }
            }
        }
        
        if (txtEdad != null) {
            String edadStr = txtEdad.getText().trim();
            if (!edadStr.isEmpty()) {
                try {
                    int edad = Integer.parseInt(edadStr);
                    if (edad < 0 || edad > 150) {
                        errores.append("La edad debe estar entre 0 y 150\n");
                    }
                } catch (NumberFormatException e) {
                    errores.append("La edad debe ser un numero valido\n");
                }
            }
        }
        
        if (errores.length() > 0) {
            mostrarError(errores.toString().trim());
            return false;
        }
        
        return true;
    }
    
    private boolean guardarCambios() {
        int idUsuario = Configuracion.getIdUsuarioActual();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            StringBuilder queryBuilder = new StringBuilder("UPDATE USUARIO SET ");
            boolean hayActualizaciones = false;
            
            if (txtNombre != null && !txtNombre.getText().trim().isEmpty()) {
                queryBuilder.append("nombre = ?, ");
                hayActualizaciones = true;
            }
            
            if (txtApellidos != null && !txtApellidos.getText().trim().isEmpty()) {
                queryBuilder.append("apellido = ?, ");
                hayActualizaciones = true;
            }
            
            if (txtEmailEditar != null && !txtEmailEditar.getText().trim().isEmpty()) {
                queryBuilder.append("email = ?, ");
                hayActualizaciones = true;
            }
            
            if (txtNuevaPassword != null && !txtNuevaPassword.getText().isEmpty()) {
                queryBuilder.append("contraseña = ?, ");
                hayActualizaciones = true;
            }
            
            if (txtEdad != null && !txtEdad.getText().trim().isEmpty()) {
                queryBuilder.append("edad = ?, ");
                hayActualizaciones = true;
            }
            
            if (!hayActualizaciones) {
                mostrarError("No hay cambios que guardar");
                return false;
            }
            
            String query = queryBuilder.substring(0, queryBuilder.length() - 2) + " WHERE id_usuario = ?";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            
            int paramIndex = 1;
            
            if (txtNombre != null && !txtNombre.getText().trim().isEmpty()) {
                stmt.setString(paramIndex++, txtNombre.getText().trim());
            }
            
            if (txtApellidos != null && !txtApellidos.getText().trim().isEmpty()) {
                stmt.setString(paramIndex++, txtApellidos.getText().trim());
            }
            
            if (txtEmailEditar != null && !txtEmailEditar.getText().trim().isEmpty()) {
                stmt.setString(paramIndex++, txtEmailEditar.getText().trim());
            }
            
            if (txtNuevaPassword != null && !txtNuevaPassword.getText().isEmpty()) {
                stmt.setString(paramIndex++, txtNuevaPassword.getText());
            }
            
            if (txtEdad != null && !txtEdad.getText().trim().isEmpty()) {
                stmt.setInt(paramIndex++, Integer.parseInt(txtEdad.getText().trim()));
            }
            
            stmt.setInt(paramIndex, idUsuario);
            
            int filasActualizadas = stmt.executeUpdate();
            stmt.close();
            
            if (txtNuevaPassword != null) txtNuevaPassword.clear();
            if (txtConfirmarPassword != null) txtConfirmarPassword.clear();
            
            return filasActualizadas > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al guardar los cambios: " + e.getMessage());
            return false;
        }
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