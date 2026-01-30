package com.javafx.A_holamundofxml;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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
    
    @FXML private Label lblIdUsuario;
    @FXML private Label lblTipo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;
    @FXML private TextField txtEdad;
    @FXML private PasswordField txtNuevaPassword;
    @FXML private PasswordField txtConfirmarPassword;
    @FXML private Button btnTema;
    
    private String nombreOriginal;
    private String apellidosOriginal;
    private String emailOriginal;
    private String edadOriginal;
    
    @FXML
    public void initialize() {
        cargarDatosUsuario();
        actualizarTextoBotonTema();
        
        btnTema.setTooltip(new Tooltip("Cambiar entre tema claro y oscuro"));
        txtNombre.setTooltip(new Tooltip("Tu nombre"));
        txtApellidos.setTooltip(new Tooltip("Tus apellidos"));
        txtNuevaPassword.setTooltip(new Tooltip("Introduce una nueva contrasena"));
        txtConfirmarPassword.setTooltip(new Tooltip("Confirma la nueva contrasena"));
        txtEdad.setTooltip(new Tooltip("Tu edad (0-150)"));
        txtEmail.setTooltip(new Tooltip("Tu correo electronico"));
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), lblIdUsuario.getParent().getParent());
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
                nombreOriginal = rs.getString("nombre");
                apellidosOriginal = rs.getString("apellido");
                emailOriginal = rs.getString("email");
                edadOriginal = String.valueOf(rs.getInt("edad"));
                
                lblIdUsuario.setText(String.valueOf(idUsuario));
                lblTipo.setText(rs.getString("tipo_usuario"));
                txtNombre.setText(nombreOriginal);
                txtApellidos.setText(apellidosOriginal);
                txtEmail.setText(emailOriginal);
                txtEdad.setText(edadOriginal);
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
    
    private void shakeNode(javafx.scene.Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.playFromStart();
    }
    
    @FXML
    public void handleGuardarCambios(ActionEvent event) {
        if (validarCampos()) {
            boolean cambiosRealizados = aplicarCambiosDatos();
            if (cambiosRealizados) {
                mostrarExito("Datos personales guardados correctamente.");
                cargarDatosUsuario();
            }
        }
    }
    
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();
        
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String email = txtEmail.getText().trim();
        String edadStr = txtEdad.getText().trim();
        String nuevaPassword = txtNuevaPassword.getText();
        String confirmarPassword = txtConfirmarPassword.getText();
        
        if (nombre.isEmpty()) {
            errores.append("El nombre no puede estar vacio\n");
            shakeNode(txtNombre);
        } else if (nombre.length() > 100) {
            errores.append("El nombre no puede exceder los 100 caracteres\n");
            shakeNode(txtNombre);
        }
        
        if (apellidos.isEmpty()) {
            errores.append("Los apellidos no pueden estar vacios\n");
            shakeNode(txtApellidos);
        } else if (apellidos.length() > 100) {
            errores.append("Los apellidos no pueden exceder los 100 caracteres\n");
            shakeNode(txtApellidos);
        }
        
        if (email.isEmpty()) {
            errores.append("El email no puede estar vacio\n");
            shakeNode(txtEmail);
        } else if (!validarEmail(email)) {
            errores.append("El email debe ser valido (formato: usuario@dominio.com)\n");
            shakeNode(txtEmail);
        } else if (email.length() > 255) {
            errores.append("El email no puede exceder los 255 caracteres\n");
            shakeNode(txtEmail);
        }
        
        if (edadStr.isEmpty()) {
            errores.append("La edad no puede estar vacia\n");
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
        
        if (!nuevaPassword.isEmpty()) {
            if (nuevaPassword.length() < 4) {
                errores.append("La contrasena debe tener al menos 4 caracteres\n");
                shakeNode(txtNuevaPassword);
            } else if (!nuevaPassword.equals(confirmarPassword)) {
                errores.append("Las contrasenas no coinciden\n");
                shakeNode(txtConfirmarPassword);
            }
        }
        
        if (errores.length() > 0) {
            mostrarError(errores.toString().trim());
            return false;
        }
        
        return true;
    }
    
    private boolean aplicarCambiosDatos() {
        int idUsuario = Configuracion.getIdUsuarioActual();
        
        if (idUsuario <= 0) {
            return false;
        }
        
        String nuevoNombre = txtNombre.getText().trim();
        String nuevosApellidos = txtApellidos.getText().trim();
        String nuevoEmail = txtEmail.getText().trim();
        String nuevaEdadStr = txtEdad.getText().trim();
        String nuevaPassword = txtNuevaPassword.getText();
        
        boolean hayNuevoNombre = !nuevoNombre.equals(nombreOriginal);
        boolean hayNuevosApellidos = !nuevosApellidos.equals(apellidosOriginal);
        boolean hayNuevoEmail = !nuevoEmail.equals(emailOriginal);
        boolean hayNuevaEdad = !nuevaEdadStr.equals(edadOriginal);
        boolean hayNuevaPassword = !nuevaPassword.isEmpty();
        
        if (!hayNuevoNombre && !hayNuevosApellidos && !hayNuevoEmail && !hayNuevaEdad && !hayNuevaPassword) {
            return false;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            if (hayNuevoEmail && !nuevoEmail.equals(emailOriginal)) {
                String checkQuery = "SELECT COUNT(*) FROM USUARIO WHERE email = ? AND id_usuario != ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, nuevoEmail);
                checkStmt.setInt(2, idUsuario);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    mostrarError("Ya existe otro usuario con este email");
                    shakeNode(txtEmail);
                    rs.close();
                    checkStmt.close();
                    conn.rollback();
                    return false;
                }
                rs.close();
                checkStmt.close();
            }
            
            StringBuilder queryBuilder = new StringBuilder("UPDATE USUARIO SET ");
            java.util.List<Object> params = new java.util.ArrayList<>();
            boolean primero = true;
            
            if (hayNuevoNombre) {
                queryBuilder.append("nombre = ?");
                params.add(nuevoNombre);
                primero = false;
            }
            
            if (hayNuevosApellidos) {
                if (!primero) queryBuilder.append(", ");
                queryBuilder.append("apellido = ?");
                params.add(nuevosApellidos);
                primero = false;
            }
            
            if (hayNuevoEmail) {
                if (!primero) queryBuilder.append(", ");
                queryBuilder.append("email = ?");
                params.add(nuevoEmail);
                primero = false;
            }
            
            if (hayNuevaEdad) {
                if (!primero) queryBuilder.append(", ");
                queryBuilder.append("edad = ?");
                params.add(Integer.parseInt(nuevaEdadStr));
                primero = false;
            }
            
            if (hayNuevaPassword) {
                if (!primero) queryBuilder.append(", ");
                queryBuilder.append("contraseña = ?");
                params.add(nuevaPassword);
            }
            
            queryBuilder.append(" WHERE id_usuario = ?");
            params.add(idUsuario);
            
            PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString());
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    stmt.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) param);
                }
            }
            
            int filas = stmt.executeUpdate();
            stmt.close();
            
            if (filas > 0) {
                if (hayNuevosApellidos) {
                    String updateAsistencia = "UPDATE ASISTENCIA SET apellidos = ? WHERE id_usuario = ?";
                    PreparedStatement asistStmt = conn.prepareStatement(updateAsistencia);
                    asistStmt.setString(1, nuevosApellidos);
                    asistStmt.setInt(2, idUsuario);
                    asistStmt.executeUpdate();
                    asistStmt.close();
                }
                
                conn.commit();
                
                if (hayNuevoNombre) {
                    Configuracion.setNombreUsuarioActual(nuevoNombre);
                }
                
                txtNuevaPassword.clear();
                txtConfirmarPassword.clear();
                
                return true;
            }
            
            conn.rollback();
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al actualizar los datos: " + e.getMessage());
            return false;
        }
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