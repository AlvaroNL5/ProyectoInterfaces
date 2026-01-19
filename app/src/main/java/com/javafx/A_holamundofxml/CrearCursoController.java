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
import java.util.HashSet;
import java.util.Set;

public class CrearCursoController {
    
    @FXML private TextField txtNombreCurso;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtBuscarAlumno;
    @FXML private ListView<UsuarioItem> listaAlumnosDisponibles;
    @FXML private ListView<UsuarioItem> listaAlumnosSeleccionados;
    @FXML private Button btnCrear;
    
    private ObservableList<UsuarioItem> alumnosDisponibles = FXCollections.observableArrayList();
    private ObservableList<UsuarioItem> alumnosSeleccionados = FXCollections.observableArrayList();
    private Set<Integer> idsAlumnosSeleccionados = new HashSet<>();
    
    private CursosController cursosController;
    
    public void setCursosController(CursosController controller) {
        this.cursosController = controller;
    }
    
    public static class UsuarioItem {
        private final int id;
        private final String nombre;
        private final String apellidos;
        private final String email;
        private final int cursosAsignados;
        
        public UsuarioItem(int id, String nombre, String apellidos, String email, int cursosAsignados) {
            this.id = id;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.email = email;
            this.cursosAsignados = cursosAsignados;
        }
        
        public int getId() { return id; }
        public int getCursosAsignados() { return cursosAsignados; }
        
        @Override
        public String toString() {
            return nombre + " " + apellidos + " (" + email + ") - Cursos: " + cursosAsignados;
        }
    }
    
    @FXML
    public void initialize() {
        listaAlumnosDisponibles.setItems(alumnosDisponibles);
        listaAlumnosSeleccionados.setItems(alumnosSeleccionados);
        
        txtNombreCurso.setTooltip(new Tooltip("Introduzca el nombre del curso"));
        txtDescripcion.setTooltip(new Tooltip("Introduzca una descripcion del curso"));
        txtBuscarAlumno.setTooltip(new Tooltip("Escriba para filtrar alumnos"));
        
        txtBuscarAlumno.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarAlumnos(newValue);
        });
        
        cargarAlumnosDisponibles();
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
    void handleAnadirAlumno(ActionEvent event) {
        UsuarioItem seleccionado = listaAlumnosDisponibles.getSelectionModel().getSelectedItem();
        if (seleccionado != null && !idsAlumnosSeleccionados.contains(seleccionado.getId())) {
            int cursosActuales = seleccionado.getCursosAsignados();
            if (cursosActuales + 1 <= 2) {
                alumnosSeleccionados.add(seleccionado);
                idsAlumnosSeleccionados.add(seleccionado.getId());
            } else {
                mostrarError("Este alumno ya esta en 2 cursos.");
                shakeNode(listaAlumnosDisponibles);
            }
        }
    }
    
    @FXML
    void handleQuitarAlumno(ActionEvent event) {
        UsuarioItem seleccionado = listaAlumnosSeleccionados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            alumnosSeleccionados.remove(seleccionado);
            idsAlumnosSeleccionados.remove(seleccionado.getId());
        }
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
    
    private void cargarAlumnosDisponibles() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT u.id_usuario, u.nombre, u.apellido, u.email, " +
                          "COUNT(a.id_curso) as cursos_actuales " +
                          "FROM USUARIO u " +
                          "LEFT JOIN ASISTENCIA a ON u.id_usuario = a.id_usuario " +
                          "WHERE u.tipo_usuario = 'alumno' " +
                          "GROUP BY u.id_usuario, u.nombre, u.apellido, u.email " +
                          "HAVING cursos_actuales < 2 " +
                          "ORDER BY u.nombre, u.apellido";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            alumnosDisponibles.clear();
            while (rs.next()) {
                alumnosDisponibles.add(new UsuarioItem(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getInt("cursos_actuales")
                ));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al cargar alumnos: " + e.getMessage());
        }
    }
    
    private void buscarAlumnos(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            cargarAlumnosDisponibles();
            return;
        }
        
        String filtroBusqueda = "%" + filtro.toLowerCase() + "%";
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT u.id_usuario, u.nombre, u.apellido, u.email, " +
                          "COUNT(a.id_curso) as cursos_actuales " +
                          "FROM USUARIO u " +
                          "LEFT JOIN ASISTENCIA a ON u.id_usuario = a.id_usuario " +
                          "WHERE u.tipo_usuario = 'alumno' " +
                          "AND (LOWER(u.nombre) LIKE ? OR LOWER(u.apellido) LIKE ? OR LOWER(u.email) LIKE ?) " +
                          "GROUP BY u.id_usuario, u.nombre, u.apellido, u.email " +
                          "HAVING cursos_actuales < 2 " +
                          "ORDER BY u.nombre, u.apellido";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, filtroBusqueda);
            stmt.setString(2, filtroBusqueda);
            stmt.setString(3, filtroBusqueda);
            
            ResultSet rs = stmt.executeQuery();
            
            alumnosDisponibles.clear();
            while (rs.next()) {
                alumnosDisponibles.add(new UsuarioItem(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getInt("cursos_actuales")
                ));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al buscar alumnos: " + e.getMessage());
        }
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
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            String nombreCurso = txtNombreCurso.getText().trim();
            
            String checkQuery = "SELECT COUNT(*) FROM CURSO WHERE nombre_curso = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, nombreCurso);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next() && checkRs.getInt(1) > 0) {
                mostrarError("Ya existe un curso con este nombre");
                shakeNode(txtNombreCurso);
                conn.rollback();
                checkRs.close();
                checkStmt.close();
                return;
            }
            checkRs.close();
            checkStmt.close();
            
            String queryCurso = "INSERT INTO CURSO (nombre_curso, descripcion, cant_usuarios) VALUES (?, ?, ?)";
            PreparedStatement stmtCurso = conn.prepareStatement(queryCurso, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtCurso.setString(1, nombreCurso);
            stmtCurso.setString(2, txtDescripcion.getText().trim());
            stmtCurso.setInt(3, idsAlumnosSeleccionados.size());
            stmtCurso.executeUpdate();
            
            ResultSet rs = stmtCurso.getGeneratedKeys();
            int idCurso = -1;
            if (rs.next()) {
                idCurso = rs.getInt(1);
            }
            rs.close();
            stmtCurso.close();
            
            if (idCurso != -1 && !idsAlumnosSeleccionados.isEmpty()) {
                String insertQuery = "INSERT INTO ASISTENCIA (id_usuario, id_curso, apellidos, nFaltas, nota, fecha_registro) " +
                                    "SELECT u.id_usuario, ?, u.apellido, 0, 0.0, CURDATE() " +
                                    "FROM USUARIO u WHERE u.id_usuario = ?";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                
                for (Integer idUsuario : idsAlumnosSeleccionados) {
                    insertStmt.setInt(1, idCurso);
                    insertStmt.setInt(2, idUsuario);
                    insertStmt.addBatch();
                }
                
                insertStmt.executeBatch();
                insertStmt.close();
            }
            
            conn.commit();
            
            if (cursosController != null) {
                cursosController.cargarCursos();
                cursosController.cargarAsistencias(); 
            }
            
            mostrarExito("Curso creado correctamente");
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            mostrarError("Error al crear el curso: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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