package com.javafx.A_holamundofxml;

import javafx.animation.FadeTransition;
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

public class CursoDetalleController {

    @FXML private Button btnVolverCursos;
    @FXML private TableColumn<UsuarioCurso, String> colUsuarioApellidos;
    @FXML private TableColumn<UsuarioCurso, String> colUsuarioEmail;
    @FXML private TableColumn<UsuarioCurso, String> colUsuarioNombre;
    @FXML private TableColumn<UsuarioCurso, String> colUsuarioTipo;
    @FXML private TableColumn<UsuarioCurso, Integer> colUsuarioEdad;
    @FXML private Label lblInfoAlumnos;
    @FXML private Label lblInfoCursoId;
    @FXML private Label lblNombreCurso;
    @FXML private TableView<UsuarioCurso> tablaUsuarios;
    @FXML private TextArea txtInfoDescripcion;
    @FXML private TextField txtInfoNombre;
    @FXML private Button btnEliminarUsuarioCurso;
    @FXML private Button btnAnadirAlumno;
    @FXML private Button btnGuardar;
    @FXML private ComboBox<AlumnoItem> comboAlumnosDisponibles;

    private int idCurso;
    private ObservableList<UsuarioCurso> usuariosCurso = FXCollections.observableArrayList();
    private ObservableList<AlumnoItem> alumnosDisponibles = FXCollections.observableArrayList();
    private CursosController cursosController;

    public void setCursosController(CursosController cursosController) {
        this.cursosController = cursosController;
    }

    public static class UsuarioCurso {
        private final int idUsuario;
        private final String nombre;
        private final String apellidos;
        private final String email;
        private final String tipo;
        private final int edad;

        public UsuarioCurso(int idUsuario, String nombre, String apellidos, String email, String tipo, int edad) {
            this.idUsuario = idUsuario;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.email = email;
            this.tipo = tipo;
            this.edad = edad;
        }

        public int getIdUsuario() { return idUsuario; }
        public String getNombre() { return nombre; }
        public String getApellidos() { return apellidos; }
        public String getEmail() { return email; }
        public String getTipo() { return tipo; }
        public int getEdad() { return edad; }
    }

    public static class AlumnoItem {
        private final int id;
        private final String nombre;
        private final String apellidos;
        private final String email;
        private final int cursosAsignados;

        public AlumnoItem(int id, String nombre, String apellidos, String email, int cursosAsignados) {
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
        configurarColumnas();
        configurarPermisos();
    }
    
    private void configurarPermisos() {
        boolean esProfesor = Configuracion.esProfesor();
        
        if (txtInfoNombre != null) txtInfoNombre.setEditable(esProfesor);
        if (txtInfoDescripcion != null) txtInfoDescripcion.setEditable(esProfesor);
        
        if (btnEliminarUsuarioCurso != null) {
            btnEliminarUsuarioCurso.setVisible(esProfesor);
            btnEliminarUsuarioCurso.setManaged(esProfesor);
        }
        if (btnAnadirAlumno != null) {
            btnAnadirAlumno.setVisible(esProfesor);
            btnAnadirAlumno.setManaged(esProfesor);
        }
        if (btnGuardar != null) {
            btnGuardar.setVisible(esProfesor);
            btnGuardar.setManaged(esProfesor);
        }
        if (comboAlumnosDisponibles != null) {
            comboAlumnosDisponibles.setVisible(esProfesor);
            comboAlumnosDisponibles.setManaged(esProfesor);
        }
    }

    public void cargarDatosCurso(int idCurso) {
        this.idCurso = idCurso;
        cargarInfoCurso();
        cargarUsuariosCurso();
        cargarAlumnosDisponibles();
    }

    private void cargarInfoCurso() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT nombre_curso, descripcion, cant_usuarios FROM CURSO WHERE id_curso = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idCurso);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombreCurso = rs.getString("nombre_curso");
                if (lblNombreCurso != null) lblNombreCurso.setText(nombreCurso);
                if (txtInfoNombre != null) txtInfoNombre.setText(nombreCurso);
                if (txtInfoDescripcion != null) txtInfoDescripcion.setText(rs.getString("descripcion"));
                if (lblInfoAlumnos != null) lblInfoAlumnos.setText(String.valueOf(rs.getInt("cant_usuarios")));
                if (lblInfoCursoId != null) lblInfoCursoId.setText(String.valueOf(idCurso));
            }
            
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al cargar informacion del curso: " + e.getMessage());
        }
    }

    private void cargarUsuariosCurso() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT u.id_usuario, u.nombre, u.apellido, u.email, u.tipo_usuario, u.edad " +
                          "FROM USUARIO u " +
                          "JOIN ASISTENCIA a ON u.id_usuario = a.id_usuario " +
                          "WHERE a.id_curso = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idCurso);
            ResultSet rs = stmt.executeQuery();

            usuariosCurso.clear();
            while (rs.next()) {
                usuariosCurso.add(new UsuarioCurso(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("tipo_usuario"),
                    rs.getInt("edad")
                ));
            }

            if (tablaUsuarios != null) tablaUsuarios.setItems(usuariosCurso);
            
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al cargar usuarios del curso: " + e.getMessage());
        }
    }

    private void cargarAlumnosDisponibles() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT u.id_usuario, u.nombre, u.apellido, u.email, " +
                          "COUNT(a.id_curso) as cursos_actuales " +
                          "FROM USUARIO u " +
                          "LEFT JOIN ASISTENCIA a ON u.id_usuario = a.id_usuario " +
                          "WHERE u.tipo_usuario = 'alumno' " +
                          "AND u.id_usuario NOT IN (SELECT a2.id_usuario FROM ASISTENCIA a2 WHERE a2.id_curso = ?) " +
                          "GROUP BY u.id_usuario, u.nombre, u.apellido, u.email " +
                          "HAVING cursos_actuales < 2 " +
                          "ORDER BY u.nombre, u.apellido";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idCurso);
            ResultSet rs = stmt.executeQuery();

            alumnosDisponibles.clear();
            while (rs.next()) {
                alumnosDisponibles.add(new AlumnoItem(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getInt("cursos_actuales")
                ));
            }

            if (comboAlumnosDisponibles != null) comboAlumnosDisponibles.setItems(alumnosDisponibles);
            
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al cargar usuarios disponibles: " + e.getMessage());
        }
    }

    private void configurarColumnas() {
        if (colUsuarioNombre != null)
            colUsuarioNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        if (colUsuarioApellidos != null)
            colUsuarioApellidos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellidos()));
        if (colUsuarioEmail != null)
            colUsuarioEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        if (colUsuarioTipo != null)
            colUsuarioTipo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipo()));
        if (colUsuarioEdad != null)
            colUsuarioEdad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEdad()).asObject());
    }

    @FXML
    void handleVolverCursos(ActionEvent event) {
        Stage stage = (Stage) btnVolverCursos.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleEliminarUsuarioCurso(ActionEvent event) {
        if (!Configuracion.esProfesor()) {
            mostrarError("Solo los profesores pueden eliminar usuarios de cursos");
            return;
        }
        
        if (tablaUsuarios == null) return;
        
        UsuarioCurso usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarError("Selecciona un usuario para eliminar del curso");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String deleteQuery = "DELETE FROM ASISTENCIA WHERE id_usuario = ? AND id_curso = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, usuarioSeleccionado.getIdUsuario());
            deleteStmt.setInt(2, idCurso);
            deleteStmt.executeUpdate();
            deleteStmt.close();

            String updateQuery = "UPDATE CURSO SET cant_usuarios = cant_usuarios - 1 WHERE id_curso = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, idCurso);
            updateStmt.executeUpdate();
            updateStmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            usuariosCurso.remove(usuarioSeleccionado);
            tablaUsuarios.refresh();

            if (lblInfoAlumnos != null) {
                int nuevosUsuarios = Integer.parseInt(lblInfoAlumnos.getText()) - 1;
                lblInfoAlumnos.setText(String.valueOf(nuevosUsuarios));
            }

            cargarAlumnosDisponibles();

            if (cursosController != null) {
                cursosController.cargarCursos();
                cursosController.cargarAsistencias();
            }

            mostrarExito("Usuario eliminado del curso correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al eliminar usuario del curso: " + e.getMessage());
        }
    }

    @FXML
    void handleAnadirAlumnoCurso(ActionEvent event) {
        if (!Configuracion.esProfesor()) {
            mostrarError("Solo los profesores pueden anadir usuarios a cursos");
            return;
        }
        
        if (comboAlumnosDisponibles == null) return;
        
        AlumnoItem alumnoSeleccionado = comboAlumnosDisponibles.getValue();
        if (alumnoSeleccionado == null) {
            mostrarError("Selecciona un usuario para anadir al curso");
            shakeNode(comboAlumnosDisponibles);
            return;
        }

        if (alumnoSeleccionado.getCursosAsignados() >= 2) {
            mostrarError("Este usuario ya esta en 2 cursos. No puede asignarse a mas cursos.");
            shakeNode(comboAlumnosDisponibles);
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String insertQuery = "INSERT INTO ASISTENCIA (id_usuario, id_curso, apellidos, nFaltas, nota, fecha_registro) " +
                                "SELECT ?, ?, u.apellido, 0, 0.0, CURDATE() FROM USUARIO u WHERE u.id_usuario = ?";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, alumnoSeleccionado.getId());
            insertStmt.setInt(2, idCurso);
            insertStmt.setInt(3, alumnoSeleccionado.getId());
            insertStmt.executeUpdate();
            insertStmt.close();

            String updateQuery = "UPDATE CURSO SET cant_usuarios = cant_usuarios + 1 WHERE id_curso = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, idCurso);
            updateStmt.executeUpdate();
            updateStmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            cargarUsuariosCurso();

            if (lblInfoAlumnos != null) {
                int nuevosUsuarios = Integer.parseInt(lblInfoAlumnos.getText()) + 1;
                lblInfoAlumnos.setText(String.valueOf(nuevosUsuarios));
            }

            cargarAlumnosDisponibles();
            comboAlumnosDisponibles.getSelectionModel().clearSelection();

            if (cursosController != null) {
                cursosController.cargarCursos();
                cursosController.cargarAsistencias();
            }

            mostrarExito("Usuario anadido al curso correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al anadir usuario al curso: " + e.getMessage());
        }
    }

    @FXML
    void handleGuardarCambios(ActionEvent event) {
        if (!Configuracion.esProfesor()) {
            mostrarError("Solo los profesores pueden modificar cursos");
            return;
        }
        if (actualizarCurso()) {
            mostrarExito("Cambios guardados correctamente");
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

    private boolean actualizarCurso() {
        String nuevoNombre = txtInfoNombre.getText().trim();
        String nuevaDescripcion = txtInfoDescripcion.getText().trim();

        StringBuilder errores = new StringBuilder();

        if (nuevoNombre.isEmpty()) {
            errores.append("El nombre del curso no puede estar vacio\n");
            shakeNode(txtInfoNombre);
        } else if (nuevoNombre.length() > 255) {
            errores.append("El nombre del curso no puede exceder los 255 caracteres\n");
            shakeNode(txtInfoNombre);
        }
        
        if (nuevaDescripcion.isEmpty()) {
            errores.append("La descripcion del curso no puede estar vacia\n");
            shakeNode(txtInfoDescripcion);
        }

        if (errores.length() > 0) {
            mostrarError(errores.toString().trim());
            return false;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String checkQuery = "SELECT COUNT(*) FROM CURSO WHERE nombre_curso = ? AND id_curso != ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, nuevoNombre);
            checkStmt.setInt(2, idCurso);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                mostrarError("Ya existe un curso con este nombre");
                shakeNode(txtInfoNombre);
                rs.close();
                checkStmt.close();
                return false;
            }
            rs.close();
            checkStmt.close();

            String query = "UPDATE CURSO SET nombre_curso = ?, descripcion = ? WHERE id_curso = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nuevoNombre);
            stmt.setString(2, nuevaDescripcion);
            stmt.setInt(3, idCurso);

            int filasActualizadas = stmt.executeUpdate();
            stmt.close();
            
            if (filasActualizadas > 0) {
                if (lblNombreCurso != null) lblNombreCurso.setText(nuevoNombre);
                if (cursosController != null) {
                    cursosController.cargarCursos();
                    cursosController.cargarAsistencias();
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al actualizar el curso: " + e.getMessage());
        }
        return false;
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