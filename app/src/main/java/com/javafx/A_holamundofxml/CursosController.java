package com.javafx.A_holamundofxml;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CursosController {

    @FXML
    private Button btnAjustes;

    @FXML
    private Button btnAsistencias;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnCrearCurso;

    @FXML
    private Button btnInsertar;

    @FXML
    private Button btnCursos;

    @FXML
    private Button btnExpulsar;

    @FXML
    private Button btnExpulsar1;

    @FXML
    private Button btnUsuarios;

    @FXML
    private Button btnVerCursoConcreto;

    @FXML
    private Button btnVerPerfil1;

    @FXML
    private TableColumn<Usuario, String> colApellidos;

    @FXML
    private TableColumn<Usuario, String> colApellidos1;

    @FXML
    private TableColumn<Asistencia, String> colApellidos11;

    @FXML
    private TableColumn<Usuario, Integer> colEdad;

    @FXML
    private TableColumn<Usuario, Integer> colEdad1;

    @FXML
    private TableColumn<Asistencia, String> colEdad11;

    @FXML
    private TableColumn<Usuario, String> colEmail;

    @FXML
    private TableColumn<Usuario, String> colEmail1;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colNombre1;

    @FXML
    private TableColumn<Asistencia, String> colNombre11;

    @FXML
    private TableColumn<Usuario, String> colTipo;

    @FXML
    private TableColumn<Usuario, String> colTipo1;

    @FXML
    private TableColumn<Asistencia, Integer> colTipo11;

    @FXML
    private HBox hboxAcciones;

    @FXML
    private HBox hboxAcciones1;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Label lblTipoUsuario;

    @FXML
    private TableView<Curso> tablaCursos;

    @FXML
    private TableView<Usuario> tablaUsuarios1;

    @FXML
    private TableView<Asistencia> tablaUsuarios11;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TextField txtBuscar1;

    @FXML
    private VBox vboxCursos;

    @FXML
    private VBox vboxUsuarios;

    @FXML
    private VBox vboxAsistencias;

    @FXML
    private TableColumn<Curso, String> colNombreCurso;

    @FXML
    private TableColumn<Curso, String> colDescripcion;

    @FXML
    private TableColumn<Curso, Integer> colCantUsuarios;

    public static class Usuario {
        private final SimpleStringProperty nombre;
        private final SimpleStringProperty apellidos;
        private final SimpleStringProperty email;
        private final SimpleStringProperty tipo;
        private final SimpleIntegerProperty edad;

        public Usuario(String nombre, String apellidos, String email, String tipo, int edad) {
            this.nombre = new SimpleStringProperty(nombre);
            this.apellidos = new SimpleStringProperty(apellidos);
            this.email = new SimpleStringProperty(email);
            this.tipo = new SimpleStringProperty(tipo);
            this.edad = new SimpleIntegerProperty(edad);
        }

        public String getNombre() { return nombre.get(); }
        public String getApellidos() { return apellidos.get(); }
        public String getEmail() { return email.get(); }
        public String getTipo() { return tipo.get(); }
        public int getEdad() { return edad.get(); }
    }

    public static class Curso {
        private final SimpleStringProperty nombreCurso;
        private final SimpleStringProperty descripcion;
        private final SimpleIntegerProperty cantUsuarios;

        public Curso(String nombreCurso, String descripcion, int cantUsuarios) {
            this.nombreCurso = new SimpleStringProperty(nombreCurso);
            this.descripcion = new SimpleStringProperty(descripcion);
            this.cantUsuarios = new SimpleIntegerProperty(cantUsuarios);
        }

        public String getNombreCurso() { return nombreCurso.get(); }
        public String getDescripcion() { return descripcion.get(); }
        public int getCantUsuarios() { return cantUsuarios.get(); }
    }

    public static class Asistencia {
        private final SimpleStringProperty nombre;
        private final SimpleStringProperty apellidos;
        private final SimpleIntegerProperty faltas;
        private final SimpleStringProperty nota;

        public Asistencia(String nombre, String apellidos, int faltas, String nota) {
            this.nombre = new SimpleStringProperty(nombre);
            this.apellidos = new SimpleStringProperty(apellidos);
            this.faltas = new SimpleIntegerProperty(faltas);
            this.nota = new SimpleStringProperty(nota);
        }

        public String getNombre() { return nombre.get(); }
        public String getApellidos() { return apellidos.get(); }
        public int getFaltas() { return faltas.get(); }
        public String getNota() { return nota.get(); }
    }

    @FXML
    void handleBuscar(ActionEvent event) {
        String textoBusqueda = txtBuscar.getText().toLowerCase();
        if (!textoBusqueda.isEmpty()) {
            filtrarCursos(textoBusqueda);
        } else {
            cargarCursos();
        }
    }

    @FXML
    void handleExpulsar(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaUsuarios1.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            mostrarAlerta("Expulsar", "Usuario expulsado: " + usuarioSeleccionado.getNombre());
        } else {
            mostrarAlerta("Error", "Por favor selecciona un usuario");
        }
    }

    @FXML
    void handleVerPerfil(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaUsuarios1.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            mostrarAlerta("Perfil", "Ver perfil de: " + usuarioSeleccionado.getNombre());
        } else {
            mostrarAlerta("Error", "Por favor selecciona un usuario");
        }
    }

    @FXML
    public void initialize() {
        btnCursos.setOnAction(event -> {
            vboxCursos.setVisible(true);
            vboxUsuarios.setVisible(false);
            vboxAsistencias.setVisible(false);
        });
        btnUsuarios.setOnAction(event -> {
            vboxUsuarios.setVisible(true);
            vboxCursos.setVisible(false);
            vboxAsistencias.setVisible(false);
        });
        btnAsistencias.setOnAction(event -> {
            vboxAsistencias.setVisible(true);
            vboxCursos.setVisible(false);
            vboxUsuarios.setVisible(false);
        });
        btnAjustes.setOnAction(event -> abrirVentana("/Ajustes.fxml"));
        btnCerrarSesion.setOnAction(event -> cerrarSesion());
        btnCrearCurso.setOnAction(event -> lanzarAjustes());
        btnVerCursoConcreto.setOnAction(event -> abrirVentana("/CursoDetalle.fxml"));
        btnInsertar.setOnAction(event -> lanzarInsertar());

        cargarCursos();
        cargarUsuarios();
        cargarAsistencias();
        configurarColumnas();
    }
    
    public void lanzarAjustes() {
        try {
            Stage modal = new Stage();
            modal.setTitle("Settings");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnAjustes.getScene().getWindow());

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/CrearCurso.fxml"));
            Scene scene = new Scene(loader.load());
            modal.setScene(scene);

            modal.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lanzarInsertar() {
        try {
            Stage modal = new Stage();
            modal.setTitle("Settings");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnAjustes.getScene().getWindow());

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Insertar.fxml"));
            Scene scene = new Scene(loader.load());
            modal.setScene(scene);

            modal.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirVentana(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnCursos.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void cargarCursos() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT nombre_curso, descripcion, cant_usuarios FROM CURSO";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            ObservableList<Curso> cursos = FXCollections.observableArrayList();
            while (rs.next()) {
                cursos.add(new Curso(
                    rs.getString("nombre_curso"),
                    rs.getString("descripcion"),
                    rs.getInt("cant_usuarios")
                ));
            }

            tablaCursos.setItems(cursos);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los cursos: " + e.getMessage());
        }
    }

    private void cargarUsuarios() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT nombre, apellido, email, tipo_usuario, edad FROM USUARIO";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("tipo_usuario"),
                    rs.getInt("edad")
                ));
            }

            tablaUsuarios1.setItems(usuarios);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }

    private void cargarAsistencias() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT u.nombre, a.apellidos, a.nFaltas, a.nota " +
                          "FROM ASISTENCIA a " +
                          "JOIN USUARIO u ON a.id_usuario = u.id_usuario";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            ObservableList<Asistencia> asistencias = FXCollections.observableArrayList();
            while (rs.next()) {
                asistencias.add(new Asistencia(
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getInt("nFaltas"),
                    String.valueOf(rs.getDouble("nota"))
                ));
            }

            tablaUsuarios11.setItems(asistencias);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar las asistencias: " + e.getMessage());
        }
    }

    private void configurarColumnas() {
        colNombreCurso.setCellValueFactory(new PropertyValueFactory<>("nombreCurso"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCantUsuarios.setCellValueFactory(new PropertyValueFactory<>("cantUsuarios"));

        colNombre1.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos1.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEmail1.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTipo1.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colEdad1.setCellValueFactory(new PropertyValueFactory<>("edad"));

        colNombre11.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos11.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colTipo11.setCellValueFactory(new PropertyValueFactory<>("faltas"));
        colEdad11.setCellValueFactory(new PropertyValueFactory<>("nota"));
    }

    private void filtrarCursos(String filtro) {
        ObservableList<Curso> cursosFiltrados = FXCollections.observableArrayList();
        ObservableList<Curso> todosCursos = tablaCursos.getItems();
        
        for (Curso curso : todosCursos) {
            if (curso.getNombreCurso().toLowerCase().contains(filtro) ||
                curso.getDescripcion().toLowerCase().contains(filtro)) {
                cursosFiltrados.add(curso);
            }
        }
        tablaCursos.setItems(cursosFiltrados);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}