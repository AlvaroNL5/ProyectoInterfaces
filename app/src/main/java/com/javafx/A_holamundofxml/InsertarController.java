package com.javafx.A_holamundofxml;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertarController {

    @FXML private Label lblNombreAlumno;
    @FXML private TextField txtFaltas;
    @FXML private TextField txtNota;
    @FXML private Button btnActualizar;
    @FXML private ComboBox<CursoItem> comboCurso;
    
    private CursosController cursosController;
    private int idUsuario;
    private int idCursoActual;
    private ObservableList<CursoItem> cursos = FXCollections.observableArrayList();
    
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
            return nombre;
        }
    }
    
    public void setDatosAlumno(int idUsuario, String nombre, String apellidos, int idCurso, String nombreCurso, int faltas, double nota) {
        this.idUsuario = idUsuario;
        this.idCursoActual = idCurso;
        
        lblNombreAlumno.setText(nombre + " " + apellidos + " (ID: " + idUsuario + ")");
        txtFaltas.setText(String.valueOf(faltas));
        txtNota.setText(String.valueOf(nota));
        
        cargarCursos(idCurso);
    }
    
    public void setCursosController(CursosController controller) {
        this.cursosController = controller;
    }
    
    @FXML
    void initialize() {
        comboCurso.setItems(cursos);
        
        comboCurso.setTooltip(new Tooltip("Seleccione el curso para el usuario"));
        txtFaltas.setTooltip(new Tooltip("Numero de faltas (entero positivo)"));
        txtNota.setTooltip(new Tooltip("Nota del alumno (0-10)"));
    }
    
    private void cargarCursos(int idCursoActual) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT id_curso, nombre_curso FROM CURSO ORDER BY nombre_curso";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            cursos.clear();
            CursoItem cursoSeleccionado = null;
            
            while (rs.next()) {
                int id = rs.getInt("id_curso");
                String nombre = rs.getString("nombre_curso");
                CursoItem item = new CursoItem(id, nombre);
                cursos.add(item);
                
                if (id == idCursoActual) {
                    cursoSeleccionado = item;
                }
            }
            
            if (cursoSeleccionado != null) {
                comboCurso.setValue(cursoSeleccionado);
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al cargar los cursos: " + e.getMessage());
        }
    }
    
    @FXML
    void handleCancelar(ActionEvent event) {
        ((Stage) lblNombreAlumno.getScene().getWindow()).close();
    }

    @FXML
    void handleActualizar(ActionEvent event) {
        if (validarCampos()) {
            actualizarAsistencia();
            ((Stage) btnActualizar.getScene().getWindow()).close();
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
    
    private boolean validarCampos() {
        String faltasStr = txtFaltas.getText().trim();
        String notaStr = txtNota.getText().trim();
        
        StringBuilder errores = new StringBuilder();
        
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
        
        if (comboCurso.getValue() == null) {
            errores.append("Debe seleccionar un curso\n");
            shakeNode(comboCurso);
        }
        
        if (errores.length() > 0) {
            mostrarError(errores.toString().trim());
            return false;
        }
        
        return true;
    }
    
    private void actualizarAsistencia() {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            CursoItem cursoSeleccionado = comboCurso.getValue();
            int nuevoIdCurso = cursoSeleccionado.getId();
            
            if (nuevoIdCurso != idCursoActual) {
                String checkQuery = "SELECT COUNT(*) FROM ASISTENCIA WHERE id_usuario = ? AND id_curso = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setInt(1, idUsuario);
                checkStmt.setInt(2, nuevoIdCurso);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    mostrarError("Este usuario ya esta inscrito en el curso: " + cursoSeleccionado.getNombre());
                    conn.rollback();
                    rs.close();
                    checkStmt.close();
                    return;
                }
                rs.close();
                checkStmt.close();
                
                String deleteQuery = "DELETE FROM ASISTENCIA WHERE id_usuario = ? AND id_curso = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setInt(1, idUsuario);
                deleteStmt.setInt(2, idCursoActual);
                deleteStmt.executeUpdate();
                deleteStmt.close();
                
                String insertQuery = "INSERT INTO ASISTENCIA (id_usuario, id_curso, apellidos, nFaltas, nota, fecha_registro) " +
                                    "SELECT ?, ?, apellido, ?, ?, CURDATE() FROM USUARIO WHERE id_usuario = ?";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, idUsuario);
                insertStmt.setInt(2, nuevoIdCurso);
                insertStmt.setInt(3, Integer.parseInt(txtFaltas.getText().trim()));
                insertStmt.setDouble(4, Double.parseDouble(txtNota.getText().trim()));
                insertStmt.setInt(5, idUsuario);
                insertStmt.executeUpdate();
                insertStmt.close();
                
                actualizarContadoresCursos(conn, idCursoActual, nuevoIdCurso);
                conn.commit();
                
                mostrarExito("Alumno movido al curso: " + cursoSeleccionado.getNombre());
                
                if (cursosController != null) {
                    cursosController.cargarAsistencias();
                    cursosController.cargarCursos();
                }
                
            } else {
                String updateQuery = "UPDATE ASISTENCIA SET nFaltas = ?, nota = ? WHERE id_usuario = ? AND id_curso = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, Integer.parseInt(txtFaltas.getText().trim()));
                updateStmt.setDouble(2, Double.parseDouble(txtNota.getText().trim()));
                updateStmt.setInt(3, idUsuario);
                updateStmt.setInt(4, idCursoActual);
                
                int filasAfectadas = updateStmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    conn.commit();
                    mostrarExito("Informacion actualizada correctamente");
                    
                    if (cursosController != null) {
                        cursosController.cargarAsistencias();
                    }
                } else {
                    conn.rollback();
                    mostrarError("No se pudo actualizar la informacion");
                }
                
                updateStmt.close();
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            mostrarError("Error al actualizar: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void actualizarContadoresCursos(Connection conn, int idCursoViejo, int idCursoNuevo) throws SQLException {
        String updateCurso = "UPDATE CURSO SET cant_usuarios = " +
                             "(SELECT COUNT(DISTINCT id_usuario) FROM ASISTENCIA WHERE id_curso = ?) " +
                             "WHERE id_curso = ?";
        PreparedStatement stmt = conn.prepareStatement(updateCurso);
        stmt.setInt(1, idCursoViejo);
        stmt.setInt(2, idCursoViejo);
        stmt.executeUpdate();
        
        stmt.setInt(1, idCursoNuevo);
        stmt.setInt(2, idCursoNuevo);
        stmt.executeUpdate();
        stmt.close();
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