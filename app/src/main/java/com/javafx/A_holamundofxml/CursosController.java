package com.javafx.A_holamundofxml;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
    @FXML private Button btnCrearMatricula;
    @FXML private Button btnEliminarMatricula;
    @FXML private Button btnInformes;
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
    public void initialize() {
        lblNombreUsuario.setText("Usuario: " + Configuracion.getNombreUsuarioActual());
        String tipo = Configuracion.getTipoUsuarioActual();
        lblTipoUsuario.setText("Rol: " + (tipo.substring(0, 1).toUpperCase() + tipo.substring(1)));
        
        configurarPermisosPorRol();
        
        btnCursos.setTooltip(new Tooltip("Ver listado de cursos"));
        btnUsuarios.setTooltip(new Tooltip("Ver listado de usuarios"));
        btnAsistencias.setTooltip(new Tooltip("Ver matriculaciones"));
        btnAjustes.setTooltip(new Tooltip("Configuracion y perfil"));
        btnCerrarSesion.setTooltip(new Tooltip("Cerrar sesion"));
        
        txtBuscar.setPromptText("Buscar por ID, nombre o descripcion...");
        txtBuscar1.setPromptText("Buscar por ID, nombre, apellidos o email...");
        txtBuscar11.setPromptText("Buscar por ID, nombre o curso...");
        
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
        btnAjustes.setOnAction(event -> abrirAjustes());
        btnCerrarSesion.setOnAction(event -> cerrarSesion());
        btnCrearCurso.setOnAction(event -> {
            animarBoton(btnCrearCurso);
            lanzarCrearCurso();
        });
        btnInsertar.setOnAction(event -> {
            animarBoton(btnInsertar);
            lanzarEditarMatricula();
        });
        btnCrearUsuario.setOnAction(event -> {
            animarBoton(btnCrearUsuario);
            lanzarCrearUsuario();
        });
        
        if (btnCrearMatricula != null) {
            btnCrearMatricula.setOnAction(event -> {
                animarBoton(btnCrearMatricula);
                lanzarCrearMatricula();
            });
        }
        
        if (btnEliminarMatricula != null) {
            btnEliminarMatricula.setOnAction(event -> {
                animarBoton(btnEliminarMatricula);
                eliminarMatriculaSeleccionada();
            });
        }
        
        if (btnInformes != null) {
            btnInformes.setOnAction(event -> {
                animarBoton(btnInformes);
                abrirInformes();
            });
        }

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
    
    private void configurarPermisosPorRol() {
        boolean esProfesor = Configuracion.esProfesor();
        
        btnCrearCurso.setVisible(esProfesor);
        btnCrearCurso.setManaged(esProfesor);
        btnCrearUsuario.setVisible(esProfesor);
        btnCrearUsuario.setManaged(esProfesor);
        
        btnExpulsar.setVisible(esProfesor);
        btnExpulsar.setManaged(esProfesor);
        btnExpulsar1.setVisible(esProfesor);
        btnExpulsar1.setManaged(esProfesor);
        
        btnInsertar.setVisible(esProfesor);
        btnInsertar.setManaged(esProfesor);
        
        if (btnCrearMatricula != null) {
            btnCrearMatricula.setVisible(esProfesor);
            btnCrearMatricula.setManaged(esProfesor);
        }
        
        if (btnEliminarMatricula != null) {
            btnEliminarMatricula.setVisible(esProfesor);
            btnEliminarMatricula.setManaged(esProfesor);
        }
        
        if (!esProfesor) {
            colFaltas11.setVisible(false);
            colNota11.setVisible(false);
        }
        
        btnVerPerfil1.setVisible(true);
        btnVerCursoConcreto.setVisible(true);
    }

    @FXML
    void handleExpulsar(ActionEvent event) {
        if (!Configuracion.esProfesor()) {
            mostrarAlerta("Acceso denegado", "Solo los profesores pueden realizar esta accion");
            return;
        }
        
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
        }
    }

    @FXML
    void handleVerPerfil(ActionEvent event) {
        if (vboxCursos.isVisible()) {
            Curso cursoSeleccionado = tablaCursos.getSelectionModel().getSelectedItem();
            if (cursoSeleccionado != null) {
                abrirDetalleCurso(cursoSeleccionado);
            } else {
                mostrarAlerta("Error", "Debes seleccionar un curso para ver sus detalles");
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
    
    private void eliminarMatriculaSeleccionada() {
        if (!Configuracion.esProfesor()) {
            mostrarAlerta("Acceso denegado", "Solo los profesores pueden eliminar matriculaciones");
            return;
        }
        
        Asistencia asistenciaSeleccionada = tablaUsuarios11.getSelectionModel().getSelectedItem();
        if (asistenciaSeleccionada == null) {
            mostrarAlerta("Error", "Por favor selecciona una matriculacion para eliminar");
            return;
        }
        
        borrarAsistencia(asistenciaSeleccionada);
    }
    
    private void animarCambioVista(VBox vistaAnterior, VBox vistaNueva) {
        if (vistaAnterior != null && vistaAnterior.isVisible()) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), vistaAnterior);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                vistaAnterior.setVisible(false);
                vistaNueva.setVisible(true);
                vistaNueva.setOpacity(0.0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(150), vistaNueva);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        } else {
            vistaNueva.setVisible(true);
            vistaNueva.setOpacity(0.0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(150), vistaNueva);
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
        scale.play();
    }
    
    private void abrirInformes() {
        try {
            Stage modal = new Stage();
            modal.setTitle("Informes - Muudle");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnInformes.getScene().getWindow());
            agregarIconoVentana(modal);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Informes.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            modal.setScene(scene);

            modal.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de informes: " + e.getMessage());
        }
    }
    
    public void lanzarCrearCurso() {
        if (!Configuracion.esProfesor()) {
            mostrarAlerta("Acceso denegado", "Solo los profesores pueden crear cursos");
            return;
        }
        
        try {
            Stage modal = new Stage();
            modal.setTitle("Crear Curso");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnCrearCurso.getScene().getWindow());
            agregarIconoVentana(modal);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearCurso.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            modal.setScene(scene);

            CrearCursoController controller = loader.getController();
            controller.setCursosController(this);

            modal.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lanzarCrearUsuario() {
        if (!Configuracion.esProfesor()) {
            mostrarAlerta("Acceso denegado", "Solo los profesores pueden crear usuarios");
            return;
        }
        
        try {
            Stage modal = new Stage();
            modal.setTitle("Crear Usuario");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnCrearUsuario.getScene().getWindow());
            agregarIconoVentana(modal);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearUsuario.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            modal.setScene(scene);

            CrearUsuarioController controller = loader.getController();
            controller.setCursosController(this);

            modal.showAndWait();
            cargarUsuarios();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void lanzarCrearMatricula() {
        if (!Configuracion.esProfesor()) {
            mostrarAlerta("Acceso denegado", "Solo los profesores pueden crear matriculaciones");
            return;
        }
        
        try {
            Stage modal = new Stage();
            modal.setTitle("Nueva Matriculacion");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnCrearMatricula.getScene().getWindow());
            agregarIconoVentana(modal);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearMatricula.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            modal.setScene(scene);

            CrearMatriculaController controller = loader.getController();
            controller.setCursosController(this);

            modal.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lanzarEditarMatricula() {
        if (!Configuracion.esProfesor()) {
            mostrarAlerta("Acceso denegado", "Solo los profesores pueden editar matriculaciones");
            return;
        }
        
        Asistencia asistenciaSeleccionada = tablaUsuarios11.getSelectionModel().getSelectedItem();
        
        if (asistenciaSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una matriculacion de la tabla para editar");
            return;
        }
        
        try {
            Stage modal = new Stage();
            modal.setTitle("Editar Matriculacion");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnInsertar.getScene().getWindow());
            agregarIconoVentana(modal);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Insertar.fxml"));
            Scene scene = new Scene(loader.load());
            Main.aplicarTema(scene);
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
            agregarIconoVentana(stage);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CursoDetalle.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            stage.setScene(scene);

            CursoDetalleController controller = loader.getController();
            controller.setCursosController(this);
            controller.cargarDatosCurso(curso.getIdCurso());

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
            agregarIconoVentana(modal);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PerfilDetalle.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            
            PerfilDetalleController controller = loader.getController();
            controller.setCursosController(this);
            controller.cargarDatosUsuario(usuario);
            
            modal.setScene(scene);
            modal.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el perfil del usuario: " + e.getMessage());
        }
    }

    private void abrirAjustes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ajustes.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnCursos.getScene().getWindow();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cerrarSesion() {
        try {
            // Resetear configuracion completa incluyendo tema
            Configuracion.resetear();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            
            stage.setTitle("Login - Muudle");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void agregarIconoVentana(Stage stage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/muudle.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
        }
    }

    public void cargarCursos() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query;
            PreparedStatement stmt;
            
            if (Configuracion.esProfesor()) {
                query = "SELECT id_curso, nombre_curso, descripcion, cant_usuarios FROM CURSO";
                stmt = conn.prepareStatement(query);
            } else {
                // Alumno solo ve los cursos en los que esta matriculado
                query = "SELECT DISTINCT c.id_curso, c.nombre_curso, c.descripcion, c.cant_usuarios " +
                        "FROM CURSO c " +
                        "INNER JOIN ASISTENCIA a ON c.id_curso = a.id_curso " +
                        "WHERE a.id_usuario = ?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, Configuracion.getIdUsuarioActual());
            }
            
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
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los cursos: " + e.getMessage());
        }
    }

    public void cargarUsuarios() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query;
            PreparedStatement stmt;
            
            if (Configuracion.esProfesor()) {
                // Profesor ve todos los usuarios
                query = "SELECT id_usuario, nombre, apellido, email, tipo_usuario, edad FROM USUARIO";
                stmt = conn.prepareStatement(query);
            } else {
                // Alumno ve usuarios que estan en los mismos cursos que el
                query = "SELECT DISTINCT u.id_usuario, u.nombre, u.apellido, u.email, u.tipo_usuario, u.edad " +
                        "FROM USUARIO u " +
                        "INNER JOIN ASISTENCIA a ON u.id_usuario = a.id_usuario " +
                        "WHERE a.id_curso IN (SELECT id_curso FROM ASISTENCIA WHERE id_usuario = ?)";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, Configuracion.getIdUsuarioActual());
            }
            
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
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }

    public void cargarAsistencias() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query;
            PreparedStatement stmt;
            
            if (Configuracion.esProfesor()) {
                // Profesor ve todas las matriculaciones
                query = "SELECT u.id_usuario, u.nombre, u.apellido, a.nFaltas, a.nota, a.id_curso, c.nombre_curso " +
                        "FROM ASISTENCIA a " +
                        "JOIN USUARIO u ON a.id_usuario = u.id_usuario " +
                        "JOIN CURSO c ON a.id_curso = c.id_curso";
                stmt = conn.prepareStatement(query);
            } else {
                // Alumno solo ve sus propias matriculaciones
                query = "SELECT u.id_usuario, u.nombre, u.apellido, a.nFaltas, a.nota, a.id_curso, c.nombre_curso " +
                        "FROM ASISTENCIA a " +
                        "JOIN USUARIO u ON a.id_usuario = u.id_usuario " +
                        "JOIN CURSO c ON a.id_curso = c.id_curso " +
                        "WHERE a.id_usuario = ?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, Configuracion.getIdUsuarioActual());
            }
            
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
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar las matriculaciones: " + e.getMessage());
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
                (curso.getDescripcion() != null && curso.getDescripcion().toLowerCase().contains(filtro))) {
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
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String deleteAsistencias = "DELETE FROM ASISTENCIA WHERE id_curso = ?";
            PreparedStatement stmt1 = conn.prepareStatement(deleteAsistencias);
            stmt1.setInt(1, curso.getIdCurso());
            stmt1.executeUpdate();
            stmt1.close();
            
            String query = "DELETE FROM CURSO WHERE id_curso = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, curso.getIdCurso());
            stmt.executeUpdate();
            stmt.close();

            cargarCursos();
            cargarAsistencias();
            mostrarAlerta("Exito", "Curso eliminado correctamente");
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo eliminar el curso: " + e.getMessage());
        }
    }

    private void borrarAsistencia(Asistencia asistencia) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            int idCurso = asistencia.getIdCurso();
            
            String query = "DELETE FROM ASISTENCIA WHERE id_usuario = ? AND id_curso = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, asistencia.getIdUsuario());
            stmt.setInt(2, idCurso);
            int filas = stmt.executeUpdate();
            stmt.close();
            
            if (filas > 0) {
                String updateCurso = "UPDATE CURSO SET cant_usuarios = " +
                                    "(SELECT COUNT(DISTINCT id_usuario) FROM ASISTENCIA WHERE id_curso = ?) " +
                                    "WHERE id_curso = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateCurso);
                updateStmt.setInt(1, idCurso);
                updateStmt.setInt(2, idCurso);
                updateStmt.executeUpdate();
                updateStmt.close();
                
                cargarAsistencias();
                cargarCursos();
                mostrarAlerta("Exito", "Matriculacion eliminada correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo eliminar la matriculacion: " + e.getMessage());
        }
    }

    private void borrarUsuario(Usuario usuario) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            String selectCursos = "SELECT id_curso FROM ASISTENCIA WHERE id_usuario = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectCursos);
            selectStmt.setInt(1, usuario.getIdUsuario());
            ResultSet rs = selectStmt.executeQuery();
            
            String deleteAsistencias = "DELETE FROM ASISTENCIA WHERE id_usuario = ?";
            PreparedStatement stmt1 = conn.prepareStatement(deleteAsistencias);
            stmt1.setInt(1, usuario.getIdUsuario());
            stmt1.executeUpdate();
            stmt1.close();
            
            String query = "DELETE FROM USUARIO WHERE id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, usuario.getIdUsuario());
            int filas = stmt.executeUpdate();
            stmt.close();
            
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
                    updateStmt.close();
                }
                
                conn.commit();
                cargarUsuarios();
                cargarAsistencias(); 
                cargarCursos(); 
                mostrarAlerta("Exito", "Usuario eliminado correctamente");
            }
            
            rs.close();
            selectStmt.close();
            conn.setAutoCommit(true);
            
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
        try {
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/muudle.png")));
        } catch (Exception e) {
        }
        alert.showAndWait();
    }
}