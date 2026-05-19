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

public class CrearMatriculaController {

    @FXML private ComboBox<UsuarioItem> comboUsuario;
    @FXML private ComboBox<CursoItem> comboCurso;
    @FXML private Spinner<Integer> spinnerFaltas;
    @FXML private Spinner<Double> spinnerNota;
    @FXML private Button btnCrear;
    
    private ObservableList<UsuarioItem> usuariosDisponibles = FXCollections.observableArrayList();
    private ObservableList<CursoItem> cursosDisponibles = FXCollections.observableArrayList();
    
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
        public String getNombre() { return nombre; }
        public String getApellidos() { return apellidos; }
        public String getEmail() { return email; }
        public int getCursosAsignados() { return cursosAsignados; }
        
        @Override
        public String toString() {
            return nombre + " " + apellidos + " (" + email + ") - " + cursosAsignados + "/2 cursos";
        }
    }
    
    public static class CursoItem {
        private final int id;
        private final String nombre;
        private final int cantUsuarios;
        
        public CursoItem(int id, String nombre, int cantUsuarios) {
            this.id = id;
            this.nombre = nombre;
            this.cantUsuarios = cantUsuarios;
        }
        
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public int getCantUsuarios() { return cantUsuarios; }
        
        @Override
        public String toString() {
            return nombre + " (ID: " + id + ") - " + cantUsuarios + " alumnos";
        }
    }
    
    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> faltasFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        spinnerFaltas.setValueFactory(faltasFactory);
        
        SpinnerValueFactory<Double> notaFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10.0, 0.0, 0.5);
        spinnerNota.setValueFactory(notaFactory);
        
        comboUsuario.setItems(usuariosDisponibles);
        comboCurso.setItems(cursosDisponibles);
        
        cargarUsuariosEnCursos();
        cargarTodosLosCursos();
        
        comboUsuario.setOnAction(event -> actualizarCursosParaUsuario());
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), comboUsuario.getParent());
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
    
    private void cargarUsuariosEnCursos() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT DISTINCT u.id_usuario, u.nombre, u.apellido, u.email, " +
                          "COUNT(a.id_curso) as cursos_actuales " +
                          "FROM USUARIO u " +
                          "INNER JOIN ASISTENCIA a ON u.id_usuario = a.id_usuario " +
                          "WHERE a.matriculado = 0 " +
                          "GROUP BY u.id_usuario, u.nombre, u.apellido, u.email " +
                          "ORDER BY u.nombre, u.apellido";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            usuariosDisponibles.clear();
            while (rs.next()) {
                usuariosDisponibles.add(new UsuarioItem(
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
            mostrarError("Error al cargar usuarios: " + e.getMessage());
        }
    }
    
    private void cargarTodosLosCursos() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id_curso, nombre_curso, cant_usuarios FROM CURSO ORDER BY nombre_curso";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            cursosDisponibles.clear();
            while (rs.next()) {
                cursosDisponibles.add(new CursoItem(
                    rs.getInt("id_curso"),
                    rs.getString("nombre_curso"),
                    rs.getInt("cant_usuarios")
                ));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al cargar cursos: " + e.getMessage());
        }
    }
    
    private void actualizarCursosParaUsuario() {
        UsuarioItem usuarioSeleccionado = comboUsuario.getValue();
        if (usuarioSeleccionado == null) {
            cargarTodosLosCursos();
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT c.id_curso, c.nombre_curso, c.cant_usuarios " +
                          "FROM CURSO c " +
                          "INNER JOIN ASISTENCIA a ON c.id_curso = a.id_curso " +
                          "WHERE a.id_usuario = ? AND a.matriculado = 0 " +
                          "ORDER BY c.nombre_curso";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, usuarioSeleccionado.getId());
            ResultSet rs = stmt.executeQuery();
            
            cursosDisponibles.clear();
            while (rs.next()) {
                cursosDisponibles.add(new CursoItem(
                    rs.getInt("id_curso"),
                    rs.getString("nombre_curso"),
                    rs.getInt("cant_usuarios")
                ));
            }
            
            rs.close();
            stmt.close();
            
            comboCurso.setValue(null);
            
            if (cursosDisponibles.isEmpty()) {
                mostrarInfo("Este usuario no tiene cursos pendientes de matricular.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al actualizar cursos: " + e.getMessage());
        }
    }
    
    @FXML
    void handleCancelar(ActionEvent event) {
        Stage stage = (Stage) comboUsuario.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void handleCrear(ActionEvent event) {
        if (validarCampos()) {
            crearMatricula();
        }
    }
    
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();
        
        if (comboUsuario.getValue() == null) {
            errores.append("Debes seleccionar un usuario\n");
            shakeNode(comboUsuario);
        }
        
        if (comboCurso.getValue() == null) {
            errores.append("Debes seleccionar un curso\n");
            shakeNode(comboCurso);
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
        int faltas = spinnerFaltas.getValue();
        double nota = spinnerNota.getValue();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            String checkQuery = "SELECT COUNT(*) FROM ASISTENCIA WHERE id_usuario = ? AND id_curso = ? AND matriculado = 1";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, usuario.getId());
            checkStmt.setInt(2, curso.getId());
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next() && checkRs.getInt(1) > 0) {
                mostrarError("Este usuario ya esta matriculado en este curso");
                checkRs.close();
                checkStmt.close();
                conn.rollback();
                return;
            }
            checkRs.close();
            checkStmt.close();
            
            String existeQuery = "SELECT id_asistencia FROM ASISTENCIA WHERE id_usuario = ? AND id_curso = ? AND matriculado = 0";
            PreparedStatement existeStmt = conn.prepareStatement(existeQuery);
            existeStmt.setInt(1, usuario.getId());
            existeStmt.setInt(2, curso.getId());
            ResultSet existeRs = existeStmt.executeQuery();
            
            if (existeRs.next()) {
                String updateQuery = "UPDATE ASISTENCIA SET nFaltas = ?, nota = ?, matriculado = 1 WHERE id_usuario = ? AND id_curso = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, faltas);
                updateStmt.setDouble(2, nota);
                updateStmt.setInt(3, usuario.getId());
                updateStmt.setInt(4, curso.getId());
                updateStmt.executeUpdate();
                updateStmt.close();
            } else {
                String insertQuery = "INSERT INTO ASISTENCIA (id_usuario, id_curso, apellidos, nFaltas, nota, fecha_registro, matriculado) " +
                                    "VALUES (?, ?, ?, ?, ?, CURDATE(), 1)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, usuario.getId());
                insertStmt.setInt(2, curso.getId());
                insertStmt.setString(3, usuario.getApellidos());
                insertStmt.setInt(4, faltas);
                insertStmt.setDouble(5, nota);
                insertStmt.executeUpdate();
                insertStmt.close();
                
                String updateCursoQuery = "UPDATE CURSO SET cant_usuarios = cant_usuarios + 1 WHERE id_curso = ?";
                PreparedStatement updateCursoStmt = conn.prepareStatement(updateCursoQuery);
                updateCursoStmt.setInt(1, curso.getId());
                updateCursoStmt.executeUpdate();
                updateCursoStmt.close();
            }
            
            existeRs.close();
            existeStmt.close();
            
            conn.commit();
            
            if (cursosController != null) {
                cursosController.cargarAsistencias();
                cursosController.cargarCursos();
            }
            
            mostrarExito("Usuario matriculado correctamente en el curso '" + curso.getNombre() + "'");
            
            Stage stage = (Stage) comboUsuario.getScene().getWindow();
            stage.close();
            
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
    
    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
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