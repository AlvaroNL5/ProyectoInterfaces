package com.javafx.A_holamundofxml;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class CrearMatriculaController {

    @FXML private ComboBox<UsuarioItem> comboUsuario;
    @FXML private ComboBox<CursoItem> comboCurso;
    @FXML private TextField txtFaltas;
    @FXML private TextField txtNota;
    @FXML private Button btnCrear;
    
    private CursosController cursosController;
    private ObservableList<UsuarioItem> usuarios = FXCollections.observableArrayList();
    private ObservableList<CursoItem> cursos = FXCollections.observableArrayList();
    
    public static class UsuarioItem {
        private final int id;
        private final String nombre;
        private final String apellidos;
        
        public UsuarioItem(int id, String nombre, String apellidos) {
            this.id = id;
            this.nombre = nombre;
            this.apellidos = apellidos;
        }
        
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getApellidos() { return apellidos; }
        
        @Override
        public String toString() {
            return nombre + " " + apellidos + " (ID: " + id + ")";
        }
    }
    
    public static class CursoItem {
        private final int id;
        private final String nombre;
        
        public CursoItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
        
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        
        @Override
        public String toString() {
            return nombre + " (ID: " + id + ")";
        }
    }
    
    public void setCursosController(CursosController controller) {
        this.cursosController = controller;
    }
    
    @FXML
    public void initialize() {
        comboUsuario.setItems(usuarios);
        comboCurso.setItems(cursos);
        
        txtFaltas.setText("0");
        txtNota.setText("0.0");
        
        comboUsuario.setTooltip(new Tooltip("Seleccione el alumno a matricular"));
        comboCurso.setTooltip(new Tooltip("Seleccione el curso"));
        txtFaltas.setTooltip(new Tooltip("Numero de faltas inicial (por defecto 0)"));
        txtNota.setTooltip(new Tooltip("Nota inicial del alumno (0-10)"));
        
        cargarUsuarios();
        cargarCursos();
    }
    
    private void cargarUsuarios() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT id_usuario, nombre, apellido FROM USUARIO WHERE tipo_usuario = 'alumno' ORDER BY nombre, apellido";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            usuarios.clear();
            while (rs.next()) {
                usuarios.add(new UsuarioItem(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido")
                ));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al cargar usuarios: " + e.getMessage());
        }
    }
    
    private void cargarCursos() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT id_curso, nombre_curso FROM CURSO ORDER BY nombre_curso";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            cursos.clear();
            while (rs.next()) {
                cursos.add(new CursoItem(
                    rs.getInt("id_curso"),
                    rs.getString("nombre_curso")
                ));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al cargar cursos: " + e.getMessage());
        }
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
            crearMatricula();
        }
    }
    
    @FXML
    void handleCancelar(ActionEvent event) {
        ((Stage) btnCrear.getScene().getWindow()).close();
    }
    
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();
        
        if (comboUsuario.getValue() == null) {
            errores.append("Debe seleccionar un alumno\n");
            shakeNode(comboUsuario);
        }
        
        if (comboCurso.getValue() == null) {
            errores.append("Debe seleccionar un curso\n");
            shakeNode(comboCurso);
        }
        
        String faltasStr = txtFaltas.getText().trim();
        if (faltasStr.isEmpty()) {
            errores.append("Las faltas son obligatorias\n");
            shakeNode(txtFaltas);
        } else {
            try {
                int faltas = Integer.parseInt(faltasStr);
                if (faltas < 0) {
                    errores.append("Las faltas no pueden ser negativas\n");
                    shakeNode(txtFaltas);
                }
            } catch (NumberFormatException e) {
                errores.append("Las faltas deben ser un numero entero\n");
                shakeNode(txtFaltas);
            }
        }
        
        String notaStr = txtNota.getText().trim();
        if (notaStr.isEmpty()) {
            errores.append("La nota es obligatoria\n");
            shakeNode(txtNota);
        } else {
            try {
                double nota = Double.parseDouble(notaStr);
                if (nota < 0 || nota > 10) {
                    errores.append("La nota debe estar entre 0 y 10\n");
                    shakeNode(txtNota);
                }
            } catch (NumberFormatException e) {
                errores.append("La nota debe ser un numero valido\n");
                shakeNode(txtNota);
            }
        }
        
        if (errores.length() > 0) {
            mostrarError(errores.toString().trim());
            return false;
        }
        
        return true;
    }
    
    private void crearMatricula() {
        UsuarioItem usuario = comboUsuario.getValue();
        CursoItem curso = comboCurso.getValue();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String checkQuery = "SELECT COUNT(*) FROM ASISTENCIA WHERE id_usuario = ? AND id_curso = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, usuario.getId());
            checkStmt.setInt(2, curso.getId());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                mostrarError("Este alumno ya esta matriculado en este curso");
                rs.close();
                checkStmt.close();
                return;
            }
            rs.close();
            checkStmt.close();
            
            String countQuery = "SELECT COUNT(*) FROM ASISTENCIA WHERE id_usuario = ?";
            PreparedStatement countStmt = conn.prepareStatement(countQuery);
            countStmt.setInt(1, usuario.getId());
            ResultSet countRs = countStmt.executeQuery();
            
            if (countRs.next() && countRs.getInt(1) >= 2) {
                mostrarError("Este alumno ya esta en 2 cursos. No puede matricularse en mas.");
                countRs.close();
                countStmt.close();
                return;
            }
            countRs.close();
            countStmt.close();
            
            conn.setAutoCommit(false);
            
            String insertQuery = "INSERT INTO ASISTENCIA (id_usuario, id_curso, apellidos, nFaltas, nota, fecha_registro) " +
                                "VALUES (?, ?, ?, ?, ?, CURDATE())";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, usuario.getId());
            insertStmt.setInt(2, curso.getId());
            insertStmt.setString(3, usuario.getApellidos());
            insertStmt.setInt(4, Integer.parseInt(txtFaltas.getText().trim()));
            insertStmt.setDouble(5, Double.parseDouble(txtNota.getText().trim()));
            insertStmt.executeUpdate();
            insertStmt.close();
            
            String updateQuery = "UPDATE CURSO SET cant_usuarios = cant_usuarios + 1 WHERE id_curso = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, curso.getId());
            updateStmt.executeUpdate();
            updateStmt.close();
            
            conn.commit();
            conn.setAutoCommit(true);
            
            mostrarExito("Matriculacion creada correctamente");
            
            if (cursosController != null) {
                cursosController.cargarAsistencias();
                cursosController.cargarCursos();
            }
            
            ((Stage) btnCrear.getScene().getWindow()).close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al crear la matriculacion: " + e.getMessage());
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