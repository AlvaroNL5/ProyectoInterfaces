package com.javafx.A_holamundofxml;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrearCursoController {
    
    @FXML private TextField txtNombreCurso;
    @FXML private TextArea txtDescripcion;
    @FXML private Button btnCrear;
    
    private CursosController cursosController;
    
    public void setCursosController(CursosController controller) {
        this.cursosController = controller;
    }
    
    @FXML
    public void initialize() {
        txtNombreCurso.setTooltip(new Tooltip("Introduzca el nombre del curso"));
        txtDescripcion.setTooltip(new Tooltip("Introduzca una descripcion del curso"));
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
    void handleCrear(ActionEvent event) {
        if (validarCampos()) {
            crearCurso();
            ((Stage) txtNombreCurso.getScene().getWindow()).close();
        }
    }
    
    @FXML
    void handleCancelar(ActionEvent event) {
        ((Stage) txtNombreCurso.getScene().getWindow()).close();
    }
    
    private boolean validarCampos() {
        String nombreCurso = txtNombreCurso.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        StringBuilder errores = new StringBuilder();
        
        if (nombreCurso.isEmpty()) {
            errores.append("El nombre del curso es obligatorio\n");
            shakeNode(txtNombreCurso);
        } else if (nombreCurso.length() > 255) {
            errores.append("El nombre del curso no puede exceder los 255 caracteres\n");
            shakeNode(txtNombreCurso);
        }
        
        if (descripcion.isEmpty()) {
            errores.append("La descripcion es obligatoria\n");
            shakeNode(txtDescripcion);
        }
        
        if (errores.length() > 0) {
            mostrarError(errores.toString().trim());
            return false;
        }
        
        return true;
    }
    
    private void crearCurso() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String nombreCurso = txtNombreCurso.getText().trim();
            
            // Verificar si ya existe un curso con ese nombre
            String checkQuery = "SELECT COUNT(*) FROM CURSO WHERE nombre_curso = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, nombreCurso);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next() && checkRs.getInt(1) > 0) {
                mostrarError("Ya existe un curso con este nombre");
                shakeNode(txtNombreCurso);
                checkRs.close();
                checkStmt.close();
                return;
            }
            checkRs.close();
            checkStmt.close();
            
            // Crear el curso SIN alumnos (cant_usuarios = 0)
            // Las matriculas se crean MANUALMENTE por el profesor
            String queryCurso = "INSERT INTO CURSO (nombre_curso, descripcion, cant_usuarios) VALUES (?, ?, 0)";
            PreparedStatement stmtCurso = conn.prepareStatement(queryCurso);
            stmtCurso.setString(1, nombreCurso);
            stmtCurso.setString(2, txtDescripcion.getText().trim());
            stmtCurso.executeUpdate();
            stmtCurso.close();
            
            if (cursosController != null) {
                cursosController.cargarCursos();
            }
            
            mostrarExito("Curso creado correctamente.\nUse 'Nueva Matricula' para anadir alumnos.");
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al crear el curso: " + e.getMessage());
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