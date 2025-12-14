package com.javafx.A_holamundofxml;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
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
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CursosController {

    @FXML private Button btnAjustes;
    @FXML private Button btnAsistencias;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnCrearCurso;
    @FXML private Button btnInsertar;
    @FXML private Button btnCursos;
    @FXML private Button btnExpulsar;
    @FXML private Button btnExpulsar1;
    @FXML private Button btnUsuarios;
    @FXML private Button btnVerCursoConcreto;
    @FXML private Button btnVerPerfil1;
    @FXML private TableColumn<Usuario, String> colApellidos1;
    @FXML private TableColumn<Asistencia, String> colApellidos11;
    @FXML private TableColumn<Usuario, Integer> colEdad1;
    @FXML private TableColumn<Asistencia, String> colNota11;
    @FXML private TableColumn<Usuario, String> colEmail1;
    @FXML private TableColumn<Usuario, String> colNombre1;
    @FXML private TableColumn<Asistencia, String> colNombre11;
    @FXML private TableColumn<Usuario, String> colTipo1;
    @FXML private TableColumn<Asistencia, Integer> colFaltas11;
    @FXML private TableColumn<Asistencia, Integer> colIdUsuarioAsistencia;
    @FXML private TableColumn<Asistencia, Integer> colIdCursoAsistencia;
    @FXML private TableColumn<Asistencia, String> colNombreCursoAsistencia;
    @FXML private TableColumn<Usuario, Integer> colIdUsuario1;
    @FXML private TableColumn<Curso, Integer> colIdCurso;
    @FXML private HBox hboxAcciones;
    @FXML private HBox hboxAcciones1;
    @FXML private Label lblFecha;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblTipoUsuario;
    @FXML private TableView<Curso> tablaCursos;
    @FXML private TableView<Usuario> tablaUsuarios1;
    @FXML private TableView<Asistencia> tablaUsuarios11;
    @FXML private TextField txtBuscar;
    @FXML private TextField txtBuscar1;
    @FXML private TextField txtBuscar11;
    @FXML private VBox vboxCursos;
    @FXML private VBox vboxUsuarios;
    @FXML private VBox vboxAsistencias;
    @FXML private TableColumn<Curso, String> colNombreCurso;
    @FXML private TableColumn<Curso, String> colDescripcion;
    @FXML private TableColumn<Curso, Integer> colCantUsuarios;
    @FXML private Button btnCrearUsuario;

    private ObservableList<Curso> todosLosCursos = FXCollections.observableArrayList();
    private ObservableList<Usuario> todosLosUsuarios = FXCollections.observableArrayList();
    private ObservableList<Asistencia> todasLasAsistencias = FXCollections.observableArrayList();

    public static class Usuario {
        private final SimpleIntegerProperty idUsuario;
        private final SimpleStringProperty nombre;
        private final SimpleStringProperty apellidos;
        private final SimpleStringProperty email;
        private final SimpleStringProperty tipo;
        private final SimpleIntegerProperty edad;

        public Usuario(int idUsuario, String nombre, String apellidos, String email, String tipo, int edad) {
            this.idUsuario = new SimpleIntegerProperty(idUsuario);
            this.nombre = new SimpleStringProperty(nombre);
            this.apellidos = new SimpleStringProperty(apellidos);
            this.email = new SimpleStringProperty(email);
            this.tipo = new SimpleStringProperty(tipo);
            this.edad = new SimpleIntegerProperty(edad);
        }

        public int getIdUsuario() { return idUsuario.get(); }
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
        private final SimpleIntegerProperty idCurso;

        public Curso(int idCurso, String nombreCurso, String descripcion, int cantUsuarios) {
            this.idCurso = new SimpleIntegerProperty(idCurso);
            this.nombreCurso = new SimpleStringProperty(nombreCurso);
            this.descripcion = new SimpleStringProperty(descripcion);
            this.cantUsuarios = new SimpleIntegerProperty(cantUsuarios);
        }

        public int getIdCurso() { return idCurso.get(); }
        public String getNombreCurso() { return nombreCurso.get(); }
        public String getDescripcion() { return descripcion.get(); }
        public int getCantUsuarios() { return cantUsuarios.get(); }
    }

    public static class Asistencia {
        private final SimpleIntegerProperty idUsuario;
        private final SimpleStringProperty nombre;
        private final SimpleStringProperty apellidos;
        private final SimpleIntegerProperty faltas;
        private final SimpleStringProperty nota;
        private final SimpleIntegerProperty idCurso;
        private final SimpleStringProperty nombreCurso;

        public Asistencia(int idUsuario, String nombre, String apellidos, int faltas, String nota, int idCurso, String nombreCurso) {
            this.idUsuario = new SimpleIntegerProperty(idUsuario);
            this.nombre = new SimpleStringProperty(nombre);
            this.apellidos = new SimpleStringProperty(apellidos);
            this.faltas = new SimpleIntegerProperty(faltas);
            this.nota = new SimpleStringProperty(nota);
            this.idCurso = new SimpleIntegerProperty(idCurso);
            this.nombreCurso = new SimpleStringProperty(nombreCurso);
        }

        public int getIdUsuario() { return idUsuario.get(); }
        public String getNombre() { return nombre.get(); }
        public String getApellidos() { return apellidos.get(); }
        public int getFaltas() { return faltas.get(); }
        public String getNota() { return nota.get(); }
        public int getIdCurso() { return idCurso.get(); }
        public String getNombreCurso() { return nombreCurso.get(); }
    }

    @FXML
    void handleBuscar(ActionEvent event) {
        if (vboxCursos.isVisible()) {
            filtrarCursos(txtBuscar.getText().toLowerCase());
        } else if (vboxUsuarios.isVisible()) {
            filtrarUsuarios(txtBuscar1.getText().toLowerCase());
        } else if (vboxAsistencias.isVisible()) {
            filtrarAsistencias(txtBuscar11.getText().toLowerCase());
        }
    }

    @FXML
    void handleExpulsar(ActionEvent event) {
        if (vboxCursos.isVisible()) {
            Curso cursoSeleccionado = tablaCursos.getSelectionModel().getSelectedItem();
            if (cursoSeleccionado != null) {
                borrarCurso(cursoSeleccionado);
            } else {
                mostrarAlerta("Error", "Debes seleccionar un curso para borrar");
            }
        } else if (vboxUsuarios.isVisible()) {
            Usuario usuarioSeleccionado = tablaUsuarios1.getSelectionModel().getSelectedItem();
            if (usuarioSeleccionado != null) {
                borrarUsuario(usuarioSeleccionado);
            } else {
                mostrarAlerta("Error", "Por favor selecciona un usuario");
            }
        } else if (vboxAsistencias.isVisible()) {
            Asistencia asistenciaSeleccionada = tablaUsuarios11.getSelectionModel().getSelectedItem();
            if (asistenciaSeleccionada != null) {
                borrarAsistencia(asistenciaSeleccionada);
            } else {
                mostrarAlerta("Error", "Por favor selecciona una asistencia");
            }
        }
    }

    @FXML
    void handleVerPerfil(ActionEvent event) {
        if (vboxCursos.isVisible()) {
            Curso cursoSeleccionado = tablaCursos.getSelectionModel().getSelectedItem();
            if (cursoSeleccionado != null) {
                abrirDetalleCurso(cursoSeleccionado);
            } else {
                mostrarAlerta("Error", "Debes seleccionar un curso para poder ver sus detalles");
            }
        } else if (vboxUsuarios.isVisible()) {
            Usuario usuarioSeleccionado = tablaUsuarios1.getSelectionModel().getSelectedItem();
            if (usuarioSeleccionado != null) {
                abrirPerfilUsuario(usuarioSeleccionado);
            } else {
                mostrarAlerta("Error", "Por favor selecciona un usuario");
            }
        }
    }

    @FXML
    public void initialize() {
        btnCursos.setOnAction(event -> {
            if (!vboxCursos.isVisible()) {
                animarCambioVista(vboxUsuarios.isVisible() ? vboxUsuarios : vboxAsistencias, vboxCursos);
                vboxUsuarios.setVisible(false);
                vboxAsistencias.setVisible(false);
            }
        });
        btnUsuarios.setOnAction(event -> {
            if (!vboxUsuarios.isVisible()) {
                animarCambioVista(vboxCursos.isVisible() ? vboxCursos : vboxAsistencias, vboxUsuarios);
                vboxCursos.setVisible(false);
                vboxAsistencias.setVisible(false);
            }
        });
        btnAsistencias.setOnAction(event -> {
            if (!vboxAsistencias.isVisible()) {
                animarCambioVista(vboxCursos.isVisible() ? vboxCursos : vboxUsuarios, vboxAsistencias);
                vboxCursos.setVisible(false);
                vboxUsuarios.setVisible(false);
            }
        });
        btnAjustes.setOnAction(event -> abrirVentana("/Ajustes.fxml"));
        btnCerrarSesion.setOnAction(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), btnCerrarSesion.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> cerrarSesion());
            fadeOut.play();
        });
        btnCrearCurso.setOnAction(event -> {
            animarBoton(btnCrearCurso);
            lanzarCrearCurso();
        });
        btnInsertar.setOnAction(event -> {
            animarBoton(btnInsertar);
            lanzarInsertar();
        });
        btnCrearUsuario.setOnAction(event -> {
            animarBoton(btnCrearUsuario);
            lanzarCrearUsuario();
        });

        cargarCursos();
        cargarUsuarios();
        cargarAsistencias();
        configurarColumnas();
        
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarCursos(newValue.toLowerCase());
        });
        
        txtBuscar1.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarUsuarios(newValue.toLowerCase());
        });
        
        txtBuscar11.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarAsistencias(newValue.toLowerCase());
        });
    }
    
    private void animarCambioVista(VBox vistaAnterior, VBox vistaNueva) {
        if (vistaAnterior != null && vistaAnterior.isVisible()) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), vistaAnterior);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                vistaAnterior.setVisible(false);
                
                vistaNueva.setVisible(true);
                vistaNueva.setOpacity(0.0);
                
                FadeTransition fadeIn = new FadeTransition(Duration.millis(200), vistaNueva);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        } else {
            vistaNueva.setVisible(true);
            vistaNueva.setOpacity(0.0);
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), vistaNueva);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
    }
    
    private void animarBoton(Button boton) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), boton);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.95);
        scale.setToY(0.95);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        
        ScaleTransition scaleBack = new ScaleTransition(Duration.millis(100), boton);
        scaleBack.setFromX(0.95);
        scaleBack.setFromY(0.95);
        scaleBack.setToX(1.0);
        scaleBack.setToY(1.0);
        
        SequentialTransition sequence = new SequentialTransition(scale, scaleBack);
        sequence.play();
    }
    
    public void lanzarCrearCurso() {
        try {
            Stage modal = new Stage();
            modal.setTitle("Crear Curso");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnCrearCurso.getScene().getWindow());
            
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/muudle.png"));
            modal.getIcons().add(icon);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearCurso.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(btnCrearCurso.getScene().getStylesheets());
            modal.setScene(scene);

            root.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            
            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), root);
            scaleIn.setFromX(0.9);
            scaleIn.setFromY(0.9);
            scaleIn.setToX(1.0);
            scaleIn.setToY(1.0);
            
            ParallelTransition parallel = new ParallelTransition(fadeIn, scaleIn);
            
            modal.show();
            parallel.play();

            CrearCursoController controller = loader.getController();
            controller.setCursosController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lanzarCrearUsuario() {
        try {
            Stage modal = new Stage();
            modal.setTitle("Crear Usuario");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnCrearUsuario.getScene().getWindow());
            
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/muudle.png"));
            modal.getIcons().add(icon);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearUsuario.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(btnCrearUsuario.getScene().getStylesheets());
            modal.setScene(scene);

            CrearUsuarioController controller = loader.getController();
            controller.setCursosController(this);

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lanzarInsertar() {
        Asistencia asistenciaSeleccionada = tablaUsuarios11.getSelectionModel().getSelectedItem();
        
        if (asistenciaSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar un alumno de la tabla para actualizar");
            return;
        }
        
        try {
            Stage modal = new Stage();
            modal.setTitle("Actualizar Información");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnInsertar.getScene().getWindow());
            
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/muudle.png"));
            modal.getIcons().add(icon);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Insertar.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().addAll(btnInsertar.getScene().getStylesheets());
            modal.setScene(scene);

            InsertarController controller = loader.getController();
            controller.setCursosController(this);
            
            String nombre = asistenciaSeleccionada.getNombre();
            String apellidos = asistenciaSeleccionada.getApellidos();
            int faltas = asistenciaSeleccionada.getFaltas();
            double nota = Double.parseDouble(asistenciaSeleccionada.getNota());
            int idCurso = asistenciaSeleccionada.getIdCurso();
            String nombreCurso = asistenciaSeleccionada.getNombreCurso();
            int idUsuario = asistenciaSeleccionada.getIdUsuario();
            
            controller.setDatosAlumno(idUsuario, nombre, apellidos, idCurso, nombreCurso, faltas, nota);

            modal.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirDetalleCurso(Curso curso) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Detalle del Curso: " + curso.getNombreCurso());
            
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/muudle.png"));
            stage.getIcons().add(icon);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CursoDetalle.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().addAll(tablaCursos.getScene().getStylesheets());
            stage.setScene(scene);

            CursoDetalleController controller = loader.getController();
            controller.cargarDatosCurso(curso.getIdCurso());
            controller.setCursosController(this);

            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el detalle del curso: " + e.getMessage());
        }
    }

    private void abrirPerfilUsuario(Usuario usuario) {
        try {
            Stage modal = new Stage();
            modal.setTitle("Perfil de Usuario");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(tablaUsuarios1.getScene().getWindow());
            
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/muudle.png"));
            modal.getIcons().add(icon);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PerfilDetalle.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(tablaUsuarios1.getScene().getStylesheets());
            
            PerfilDetalleController controller = loader.getController();
            controller.cargarDatosUsuario(usuario);
            controller.setCursosController(this);
            
            modal.setScene(scene);
            modal.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el perfil del usuario: " + e.getMessage());
        }
    }

    private void abrirVentana(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnCursos.getScene().getWindow();
            Scene scene = new Scene(root);
            
            if (Configuracion.isTemaOscuro()) {
                scene.getStylesheets().add(getClass().getResource("/estilos_oscuro.css").toExternalForm());
            } else {
                scene.getStylesheets().add(getClass().getResource("/estilos_claro.css").toExternalForm());
            }
            
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            Scene scene = new Scene(root);
            
            if (Configuracion.isTemaOscuro()) {
                scene.getStylesheets().add(getClass().getResource("/estilos_oscuro.css").toExternalForm());
            } else {
                scene.getStylesheets().add(getClass().getResource("/estilos_claro.css").toExternalForm());
            }
            
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarCursos() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id_curso, nombre_curso, descripcion, cant_usuarios FROM CURSO";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            todosLosCursos.clear();
            while (rs.next()) {
                todosLosCursos.add(new Curso(
                    rs.getInt("id_curso"),
                    rs.getString("nombre_curso"),
                    rs.getString("descripcion"),
                    rs.getInt("cant_usuarios")
                ));
            }

            tablaCursos.setItems(todosLosCursos);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los cursos: " + e.getMessage());
        }
    }

    public void cargarUsuarios() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id_usuario, nombre, apellido, email, tipo_usuario, edad FROM USUARIO";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            todosLosUsuarios.clear();
            while (rs.next()) {
                todosLosUsuarios.add(new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("tipo_usuario"),
                    rs.getInt("edad")
                ));
            }

            tablaUsuarios1.setItems(todosLosUsuarios);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }

    public void cargarAsistencias() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT u.id_usuario, u.nombre, u.apellido, a.nFaltas, a.nota, a.id_curso, c.nombre_curso " +
                        "FROM ASISTENCIA a " +
                        "JOIN USUARIO u ON a.id_usuario = u.id_usuario " +
                        "JOIN CURSO c ON a.id_curso = c.id_curso";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            todasLasAsistencias.clear();
            while (rs.next()) {
                todasLasAsistencias.add(new Asistencia(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getInt("nFaltas"),
                    String.valueOf(rs.getDouble("nota")),
                    rs.getInt("id_curso"),
                    rs.getString("nombre_curso")
                ));
            }

            tablaUsuarios11.setItems(todasLasAsistencias);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar las asistencias: " + e.getMessage());
        }
    }

    private void configurarColumnas() {
        colIdCurso.setCellValueFactory(new PropertyValueFactory<>("idCurso"));
        colNombreCurso.setCellValueFactory(new PropertyValueFactory<>("nombreCurso"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCantUsuarios.setCellValueFactory(new PropertyValueFactory<>("cantUsuarios"));

        colIdUsuario1.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre1.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos1.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEmail1.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTipo1.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colEdad1.setCellValueFactory(new PropertyValueFactory<>("edad"));

        colIdUsuarioAsistencia.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre11.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos11.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colFaltas11.setCellValueFactory(new PropertyValueFactory<>("faltas"));
        colNota11.setCellValueFactory(new PropertyValueFactory<>("nota"));
        colIdCursoAsistencia.setCellValueFactory(new PropertyValueFactory<>("idCurso"));
        colNombreCursoAsistencia.setCellValueFactory(new PropertyValueFactory<>("nombreCurso"));
    }

    private void filtrarCursos(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            tablaCursos.setItems(todosLosCursos);
            return;
        }
        
        ObservableList<Curso> cursosFiltrados = FXCollections.observableArrayList();
        
        for (Curso curso : todosLosCursos) {
            if (curso.getNombreCurso().toLowerCase().contains(filtro) ||
                String.valueOf(curso.getIdCurso()).contains(filtro) ||
                curso.getDescripcion().toLowerCase().contains(filtro)) {
                cursosFiltrados.add(curso);
            }
        }
        tablaCursos.setItems(cursosFiltrados);
    }

    private void filtrarUsuarios(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            tablaUsuarios1.setItems(todosLosUsuarios);
            return;
        }
        
        ObservableList<Usuario> usuariosFiltrados = FXCollections.observableArrayList();
        
        for (Usuario usuario : todosLosUsuarios) {
            if (usuario.getNombre().toLowerCase().contains(filtro) ||
                usuario.getApellidos().toLowerCase().contains(filtro) ||
                usuario.getEmail().toLowerCase().contains(filtro) ||
                String.valueOf(usuario.getIdUsuario()).contains(filtro)) {
                usuariosFiltrados.add(usuario);
            }
        }
        tablaUsuarios1.setItems(usuariosFiltrados);
    }

    private void filtrarAsistencias(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            tablaUsuarios11.setItems(todasLasAsistencias);
            return;
        }
        
        ObservableList<Asistencia> asistenciasFiltradas = FXCollections.observableArrayList();
        
        for (Asistencia asistencia : todasLasAsistencias) {
            if (asistencia.getNombre().toLowerCase().contains(filtro) || 
                asistencia.getApellidos().toLowerCase().contains(filtro) ||
                asistencia.getNombreCurso().toLowerCase().contains(filtro) ||
                String.valueOf(asistencia.getIdUsuario()).contains(filtro) ||
                String.valueOf(asistencia.getIdCurso()).contains(filtro)) {
                asistenciasFiltradas.add(asistencia);
            }
        }
        tablaUsuarios11.setItems(asistenciasFiltradas);
    }

    private void borrarCurso(Curso curso) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String deleteAsistencias = "DELETE FROM ASISTENCIA WHERE id_curso = ?";
            PreparedStatement stmt1 = conn.prepareStatement(deleteAsistencias);
            stmt1.setInt(1, curso.getIdCurso());
            stmt1.executeUpdate();
            
            String query = "DELETE FROM CURSO WHERE id_curso = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, curso.getIdCurso());
            stmt.executeUpdate();

            cargarCursos();
            cargarAsistencias();
            mostrarAlerta("Éxito", "Curso eliminado correctamente");
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo eliminar el curso: " + e.getMessage());
        }
    }

    private void borrarAsistencia(Asistencia asistencia) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            int idCurso = asistencia.getIdCurso();
            
            String query = "DELETE FROM ASISTENCIA WHERE id_usuario = ? AND id_curso = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, asistencia.getIdUsuario());
            stmt.setInt(2, idCurso);
            int filas = stmt.executeUpdate();
            
            if (filas > 0) {
                String updateCurso = "UPDATE CURSO SET cant_usuarios = " +
                                    "(SELECT COUNT(DISTINCT id_usuario) FROM ASISTENCIA WHERE id_curso = ?) " +
                                    "WHERE id_curso = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateCurso);
                updateStmt.setInt(1, idCurso);
                updateStmt.setInt(2, idCurso);
                updateStmt.executeUpdate();
                
                cargarAsistencias();
                cargarCursos();
                mostrarAlerta("Éxito", "Alumno eliminado del curso. Ahora puede ser asignado a otro curso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo eliminar la asistencia: " + e.getMessage());
        }
    }

    private void borrarUsuario(Usuario usuario) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            String selectCursos = "SELECT id_curso FROM ASISTENCIA WHERE id_usuario = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectCursos);
            selectStmt.setInt(1, usuario.getIdUsuario());
            ResultSet rs = selectStmt.executeQuery();
            
            String deleteAsistencias = "DELETE FROM ASISTENCIA WHERE id_usuario = ?";
            PreparedStatement stmt1 = conn.prepareStatement(deleteAsistencias);
            stmt1.setInt(1, usuario.getIdUsuario());
            stmt1.executeUpdate();
            
            String query = "DELETE FROM USUARIO WHERE id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, usuario.getIdUsuario());
            int filas = stmt.executeUpdate();
            
            if (filas > 0) {
                while (rs.next()) {
                    int idCurso = rs.getInt("id_curso");
                    String updateCurso = "UPDATE CURSO SET cant_usuarios = " +
                                        "(SELECT COUNT(DISTINCT id_usuario) FROM ASISTENCIA WHERE id_curso = ?) " +
                                        "WHERE id_curso = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateCurso);
                    updateStmt.setInt(1, idCurso);
                    updateStmt.setInt(2, idCurso);
                    updateStmt.executeUpdate();
                }
                
                conn.commit();
                cargarUsuarios();
                cargarAsistencias(); 
                cargarCursos(); 
                mostrarAlerta("Éxito", "Usuario eliminado correctamente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo eliminar al usuario: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}