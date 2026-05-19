package com.javafx.A_holamundofxml;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CursosController {

    // -------- Sidebar buttons --------
    @FXML private Button btnCursos;
    @FXML private Button btnNoticias;
    @FXML private Button btnTareas;
    @FXML private Button btnForo;
    @FXML private Button btnControlAsistencia;
    @FXML private Button btnUsuarios;
    @FXML private Button btnAsistencias;
    @FXML private Button btnInformes;
    @FXML private Button btnAjustes;
    @FXML private Button btnCerrarSesion;
    @FXML private Label lblSeccionGestion;

    // -------- Header / user labels --------
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblTipoUsuario;

    // -------- Cursos view (FlowPane with cards) --------
    @FXML private VBox vboxCursos;
    @FXML private FlowPane flowCursos;
    @FXML private TextField txtBuscar;
    @FXML private Button btnCrearCurso;

    // -------- Usuarios view --------
    @FXML private VBox vboxUsuarios;
    @FXML private TextField txtBuscar1;
    @FXML private TableView<Usuario> tablaUsuarios1;
    @FXML private TableColumn<Usuario, Integer> colIdUsuario1;
    @FXML private TableColumn<Usuario, String> colNombre1;
    @FXML private TableColumn<Usuario, String> colApellidos1;
    @FXML private TableColumn<Usuario, String> colEmail1;
    @FXML private TableColumn<Usuario, String> colTipo1;
    @FXML private TableColumn<Usuario, Integer> colEdad1;
    @FXML private Button btnVerPerfil1;
    @FXML private Button btnExpulsar1;

    // -------- Matriculación view --------
    @FXML private VBox vboxAsistencias;
    @FXML private TextField txtBuscar11;
    @FXML private TableView<Asistencia> tablaUsuarios11;
    @FXML private TableColumn<Asistencia, Integer> colIdUsuarioAsistencia;
    @FXML private TableColumn<Asistencia, String> colNombre11;
    @FXML private TableColumn<Asistencia, String> colApellidos11;
    @FXML private TableColumn<Asistencia, Integer> colFaltas11;
    @FXML private TableColumn<Asistencia, String> colNota11;
    @FXML private TableColumn<Asistencia, Integer> colIdCursoAsistencia;
    @FXML private TableColumn<Asistencia, String> colNombreCursoAsistencia;
    @FXML private Button btnInsertar;
    @FXML private Button btnCrearMatricula;
    @FXML private Button btnEliminarMatricula;

    // -------- Tareas view --------
    @FXML private VBox vboxTareas;
    @FXML private TableView<Tarea> tablaTareas;
    @FXML private TableColumn<Tarea, String> colTareaTitulo;
    @FXML private TableColumn<Tarea, String> colTareaCurso;
    @FXML private TableColumn<Tarea, String> colTareaFechaLimite;
    @FXML private TableColumn<Tarea, String> colTareaPuntuacion;
    @FXML private TableColumn<Tarea, String> colTareaEstado;
    @FXML private Button btnCrearTarea;
    @FXML private Button btnVerTarea;
    @FXML private Button btnEliminarTarea;

    // -------- Foro view --------
    @FXML private VBox vboxForo;
    @FXML private TableView<HiloForo> tablaForo;
    @FXML private TableColumn<HiloForo, String> colForoTitulo;
    @FXML private TableColumn<HiloForo, String> colForoAutor;
    @FXML private TableColumn<HiloForo, String> colForoCurso;
    @FXML private TableColumn<HiloForo, String> colForoFecha;
    @FXML private Button btnCrearHilo;
    @FXML private Button btnVerHilo;
    @FXML private Button btnEliminarHilo;

    // -------- Control Asistencia view --------
    @FXML private VBox vboxControlAsistencia;
    @FXML private TableView<ControlAsistencia> tablaControlAsistencia;
    @FXML private TableColumn<ControlAsistencia, String> colAsistNombre;
    @FXML private TableColumn<ControlAsistencia, String> colAsistCurso;
    @FXML private TableColumn<ControlAsistencia, String> colAsistFecha;
    @FXML private TableColumn<ControlAsistencia, String> colAsistEstado;
    @FXML private TableColumn<ControlAsistencia, String> colAsistObservaciones;
    @FXML private Button btnRegistrarAsistencia;

    // -------- Noticias view --------
    @FXML private VBox vboxNoticias;
    @FXML private VBox vboxListaNoticias;
    @FXML private Label lblEstadoNoticias;
    @FXML private Button btnCargarNoticias;

    // -------- Data lists --------
    private final ObservableList<Curso> todosLosCursos = FXCollections.observableArrayList();
    private final ObservableList<Usuario> todosLosUsuarios = FXCollections.observableArrayList();
    private final ObservableList<Asistencia> todasLasAsistencias = FXCollections.observableArrayList();
    private final ObservableList<Tarea> todasLasTareas = FXCollections.observableArrayList();
    private final ObservableList<HiloForo> todosLosHilos = FXCollections.observableArrayList();
    private final ObservableList<ControlAsistencia> todosLosControles = FXCollections.observableArrayList();

    // Active nav button tracking
    private Button btnNavActivo = null;

    // =====================================================================
    //  DATA MODELS (inner classes)
    // =====================================================================

    public static class Usuario {
        private final SimpleIntegerProperty idUsuario;
        private final SimpleStringProperty nombre;
        private final SimpleStringProperty apellidos;
        private final SimpleStringProperty email;
        private final SimpleStringProperty tipo;
        private final SimpleIntegerProperty edad;

        public Usuario(int id, String nombre, String apellidos, String email, String tipo, int edad) {
            this.idUsuario = new SimpleIntegerProperty(id);
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
        private final SimpleIntegerProperty idCurso;
        private final SimpleStringProperty nombreCurso;
        private final SimpleStringProperty descripcion;
        private final SimpleIntegerProperty cantUsuarios;

        public Curso(int id, String nombre, String desc, int cant) {
            this.idCurso = new SimpleIntegerProperty(id);
            this.nombreCurso = new SimpleStringProperty(nombre);
            this.descripcion = new SimpleStringProperty(desc);
            this.cantUsuarios = new SimpleIntegerProperty(cant);
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

        public Asistencia(int idU, String nom, String ape, int falt, String nota, int idC, String nomC) {
            this.idUsuario = new SimpleIntegerProperty(idU);
            this.nombre = new SimpleStringProperty(nom);
            this.apellidos = new SimpleStringProperty(ape);
            this.faltas = new SimpleIntegerProperty(falt);
            this.nota = new SimpleStringProperty(nota);
            this.idCurso = new SimpleIntegerProperty(idC);
            this.nombreCurso = new SimpleStringProperty(nomC);
        }

        public int getIdUsuario() { return idUsuario.get(); }
        public String getNombre() { return nombre.get(); }
        public String getApellidos() { return apellidos.get(); }
        public int getFaltas() { return faltas.get(); }
        public String getNota() { return nota.get(); }
        public int getIdCurso() { return idCurso.get(); }
        public String getNombreCurso() { return nombreCurso.get(); }
    }

    public static class Tarea {
        private final SimpleIntegerProperty idTarea;
        private final SimpleStringProperty titulo;
        private final SimpleStringProperty nombreCurso;
        private final SimpleStringProperty fechaLimite;
        private final SimpleStringProperty puntuacionMaxima;
        private final SimpleStringProperty estado;
        private final SimpleIntegerProperty idCurso;

        public Tarea(int id, String titulo, String curso, String fecha, String puntuacion, String estado, int idC) {
            this.idTarea = new SimpleIntegerProperty(id);
            this.titulo = new SimpleStringProperty(titulo);
            this.nombreCurso = new SimpleStringProperty(curso);
            this.fechaLimite = new SimpleStringProperty(fecha);
            this.puntuacionMaxima = new SimpleStringProperty(puntuacion);
            this.estado = new SimpleStringProperty(estado);
            this.idCurso = new SimpleIntegerProperty(idC);
        }

        public int getIdTarea() { return idTarea.get(); }
        public String getTitulo() { return titulo.get(); }
        public String getNombreCurso() { return nombreCurso.get(); }
        public String getFechaLimite() { return fechaLimite.get(); }
        public String getPuntuacionMaxima() { return puntuacionMaxima.get(); }
        public String getEstado() { return estado.get(); }
        public int getIdCurso() { return idCurso.get(); }
    }

    public static class HiloForo {
        private final SimpleIntegerProperty idHilo;
        private final SimpleStringProperty titulo;
        private final SimpleStringProperty autor;
        private final SimpleStringProperty nombreCurso;
        private final SimpleStringProperty fecha;
        private final SimpleStringProperty contenido;

        public HiloForo(int id, String titulo, String autor, String curso, String fecha, String contenido) {
            this.idHilo = new SimpleIntegerProperty(id);
            this.titulo = new SimpleStringProperty(titulo);
            this.autor = new SimpleStringProperty(autor);
            this.nombreCurso = new SimpleStringProperty(curso);
            this.fecha = new SimpleStringProperty(fecha);
            this.contenido = new SimpleStringProperty(contenido);
        }

        public int getIdHilo() { return idHilo.get(); }
        public String getTitulo() { return titulo.get(); }
        public String getAutor() { return autor.get(); }
        public String getNombreCurso() { return nombreCurso.get(); }
        public String getFecha() { return fecha.get(); }
        public String getContenido() { return contenido.get(); }
    }

    public static class ControlAsistencia {
        private final SimpleIntegerProperty idControl;
        private final SimpleStringProperty nombreAlumno;
        private final SimpleStringProperty nombreCurso;
        private final SimpleStringProperty fecha;
        private final SimpleStringProperty estado;
        private final SimpleStringProperty observaciones;

        public ControlAsistencia(int id, String alumno, String curso, String fecha, String estado, String obs) {
            this.idControl = new SimpleIntegerProperty(id);
            this.nombreAlumno = new SimpleStringProperty(alumno);
            this.nombreCurso = new SimpleStringProperty(curso);
            this.fecha = new SimpleStringProperty(fecha);
            this.estado = new SimpleStringProperty(estado);
            this.observaciones = new SimpleStringProperty(obs != null ? obs : "");
        }

        public int getIdControl() { return idControl.get(); }
        public String getNombreAlumno() { return nombreAlumno.get(); }
        public String getNombreCurso() { return nombreCurso.get(); }
        public String getFecha() { return fecha.get(); }
        public String getEstado() { return estado.get(); }
        public String getObservaciones() { return observaciones.get(); }
    }

    public static class Noticia {
        private final String titulo;
        private final String url;
        private final String fecha;
        private final String fuente;
        private final String descripcion;
        private final String seccion;
        /** Cuerpo completo en texto plano (parrafos separados por '\\n'). */
        private final String cuerpo;

        public Noticia(String titulo, String url, String fecha, String fuente,
                       String descripcion, String seccion, String cuerpo) {
            this.titulo = titulo;
            this.url = url;
            this.fecha = fecha;
            this.fuente = fuente;
            this.descripcion = descripcion;
            this.seccion = (seccion == null || seccion.isEmpty()) ? "General" : seccion;
            this.cuerpo = cuerpo == null ? "" : cuerpo;
        }

        public String getTitulo()      { return titulo; }
        public String getUrl()         { return url; }
        public String getFecha()       { return fecha; }
        public String getFuente()      { return fuente; }
        public String getDescripcion() { return descripcion; }
        public String getSeccion()     { return seccion; }
        public String getCuerpo()      { return cuerpo; }
    }

    // =====================================================================
    //  VIEW MANAGEMENT
    // =====================================================================

    private List<VBox> todasLasVistas() {
        List<VBox> vistas = new ArrayList<>();
        vistas.add(vboxCursos);
        vistas.add(vboxUsuarios);
        vistas.add(vboxAsistencias);
        if (vboxTareas != null) vistas.add(vboxTareas);
        if (vboxForo != null) vistas.add(vboxForo);
        if (vboxControlAsistencia != null) vistas.add(vboxControlAsistencia);
        if (vboxNoticias != null) vistas.add(vboxNoticias);
        return vistas;
    }

    private void mostrarVista(VBox vistaAMostrar, Button btnNav) {
        VBox vistaActual = null;
        for (VBox v : todasLasVistas()) {
            if (v != null && v.isVisible()) {
                vistaActual = v;
                break;
            }
        }
        final VBox anterior = vistaActual;
        if (anterior != null && anterior != vistaAMostrar) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(180), anterior);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                for (VBox v : todasLasVistas()) { if (v != null) v.setVisible(false); }
                vistaAMostrar.setVisible(true);
                vistaAMostrar.setOpacity(0.0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(180), vistaAMostrar);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        } else {
            for (VBox v : todasLasVistas()) { if (v != null) v.setVisible(false); }
            vistaAMostrar.setVisible(true);
        }
        actualizarBtnActivo(btnNav);
    }

    private void actualizarBtnActivo(Button nuevo) {
        if (btnNavActivo != null) {
            btnNavActivo.getStyleClass().remove("nav-btn-active");
        }
        if (nuevo != null) {
            if (!nuevo.getStyleClass().contains("nav-btn-active")) {
                nuevo.getStyleClass().add("nav-btn-active");
            }
        }
        btnNavActivo = nuevo;
    }

    // =====================================================================
    //  INITIALIZE
    // =====================================================================

    @FXML
    public void initialize() {
        lblNombreUsuario.setText(Configuracion.getNombreUsuarioActual());
        lblTipoUsuario.setText(Configuracion.getTipoUsuarioActual());

        configurarPermisosPorRol();
        configurarColumnas();
        configurarContextMenus();
        configurarBotonesMenu();
        configurarBusqueda();

        cargarCursos();
        cargarUsuarios();
        cargarAsistencias();

        // Start on Inicio (courses)
        actualizarBtnActivo(btnCursos);
    }

    private void configurarBotonesMenu() {
        btnCursos.setOnAction(e -> mostrarVista(vboxCursos, btnCursos));
        btnNoticias.setOnAction(e -> mostrarVista(vboxNoticias, btnNoticias));
        btnTareas.setOnAction(e -> { mostrarVista(vboxTareas, btnTareas); cargarTareas(); });
        btnForo.setOnAction(e -> { mostrarVista(vboxForo, btnForo); cargarHilosForo(); });
        btnControlAsistencia.setOnAction(e -> { mostrarVista(vboxControlAsistencia, btnControlAsistencia); cargarControlAsistencia(); });
        btnUsuarios.setOnAction(e -> mostrarVista(vboxUsuarios, btnUsuarios));
        btnAsistencias.setOnAction(e -> mostrarVista(vboxAsistencias, btnAsistencias));

        btnAjustes.setOnAction(e -> abrirVentana("/Ajustes.fxml"));
        btnInformes.setOnAction(e -> { animarBoton(btnInformes); lanzarInformes(); });
        btnCerrarSesion.setOnAction(e -> {
            FadeTransition fade = new FadeTransition(Duration.millis(250), btnCerrarSesion.getScene().getRoot());
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(ev -> cerrarSesion());
            fade.play();
        });

        btnCrearCurso.setOnAction(e -> { animarBoton(btnCrearCurso); lanzarCrearCurso(); });
        if (btnInsertar != null) btnInsertar.setOnAction(e -> { animarBoton(btnInsertar); lanzarInsertar(); });
        if (btnCrearMatricula != null) btnCrearMatricula.setOnAction(e -> { animarBoton(btnCrearMatricula); lanzarCrearMatricula(); });
        if (btnEliminarMatricula != null) btnEliminarMatricula.setOnAction(e -> eliminarMatriculaSeleccionada());
        if (btnCrearTarea != null) btnCrearTarea.setOnAction(e -> crearTarea());
        if (btnVerTarea != null) btnVerTarea.setOnAction(e -> verDetalleTarea());
        if (btnEliminarTarea != null) btnEliminarTarea.setOnAction(e -> eliminarTarea());
        if (btnCrearHilo != null) btnCrearHilo.setOnAction(e -> crearHiloForo());
        if (btnVerHilo != null) btnVerHilo.setOnAction(e -> verHiloForo());
        if (btnEliminarHilo != null) btnEliminarHilo.setOnAction(e -> eliminarHiloForo());
        if (btnRegistrarAsistencia != null) btnRegistrarAsistencia.setOnAction(e -> registrarAsistencia());
        if (btnCargarNoticias != null) btnCargarNoticias.setOnAction(e -> { animarBoton(btnCargarNoticias); cargarNoticias(); });

    }

    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, o, n) -> filtrarCursos(n.toLowerCase()));
        if (txtBuscar1 != null) txtBuscar1.textProperty().addListener((obs, o, n) -> filtrarUsuarios(n.toLowerCase()));
        if (txtBuscar11 != null) txtBuscar11.textProperty().addListener((obs, o, n) -> filtrarAsistencias(n.toLowerCase()));
    }

    private void configurarPermisosPorRol() {
        boolean esProfesor = Configuracion.esProfesor();

        if (btnCrearCurso != null) { btnCrearCurso.setVisible(esProfesor); btnCrearCurso.setManaged(esProfesor); }
        if (btnExpulsar1 != null) { btnExpulsar1.setVisible(esProfesor); btnExpulsar1.setManaged(esProfesor); }
        if (btnCrearMatricula != null) { btnCrearMatricula.setVisible(esProfesor); btnCrearMatricula.setManaged(esProfesor); }
        if (btnEliminarMatricula != null) { btnEliminarMatricula.setVisible(esProfesor); btnEliminarMatricula.setManaged(esProfesor); }
        if (btnInsertar != null) { btnInsertar.setVisible(esProfesor); btnInsertar.setManaged(esProfesor); }
        if (btnInformes != null) { btnInformes.setVisible(esProfesor); btnInformes.setManaged(esProfesor); }
        if (btnCrearTarea != null) { btnCrearTarea.setVisible(esProfesor); btnCrearTarea.setManaged(esProfesor); }
        if (btnEliminarTarea != null) { btnEliminarTarea.setVisible(esProfesor); btnEliminarTarea.setManaged(esProfesor); }
        if (btnEliminarHilo != null) { btnEliminarHilo.setVisible(esProfesor); btnEliminarHilo.setManaged(esProfesor); }
        if (btnUsuarios != null) { btnUsuarios.setVisible(esProfesor); btnUsuarios.setManaged(esProfesor); }
        if (btnAsistencias != null) { btnAsistencias.setVisible(esProfesor); btnAsistencias.setManaged(esProfesor); }
        if (lblSeccionGestion != null) { lblSeccionGestion.setVisible(esProfesor); lblSeccionGestion.setManaged(esProfesor); }
        // Solo el profesor puede registrar asistencias; el alumno solo consulta las suyas
        if (btnRegistrarAsistencia != null) { btnRegistrarAsistencia.setVisible(esProfesor); btnRegistrarAsistencia.setManaged(esProfesor); }
    }

    // =====================================================================
    //  CONTEXT MENUS  (click derecho sobre filas de tablas)
    // =====================================================================
    private void configurarContextMenus() {
        boolean esProfesor = Configuracion.esProfesor();

        // ----- Usuarios -----
        if (tablaUsuarios1 != null) {
            ContextMenu cm = new ContextMenu();
            MenuItem miVer = new MenuItem("Ver perfil");
            miVer.setOnAction(e -> {
                Usuario sel = tablaUsuarios1.getSelectionModel().getSelectedItem();
                if (sel != null) abrirPerfilUsuario(sel);
            });
            cm.getItems().add(miVer);
            if (esProfesor) {
                MenuItem miDel = new MenuItem("Eliminar usuario");
                miDel.setOnAction(e -> {
                    Usuario sel = tablaUsuarios1.getSelectionModel().getSelectedItem();
                    if (sel != null) borrarUsuario(sel);
                });
                cm.getItems().addAll(new SeparatorMenuItem(), miDel);
            }
            tablaUsuarios1.setContextMenu(cm);
        }

        // ----- Matriculaciones -----
        if (tablaUsuarios11 != null && esProfesor) {
            ContextMenu cm = new ContextMenu();
            MenuItem miEdit = new MenuItem("Editar nota/faltas");
            miEdit.setOnAction(e -> lanzarInsertar());
            MenuItem miDel = new MenuItem("Eliminar matriculacion");
            miDel.setOnAction(e -> eliminarMatriculaSeleccionada());
            cm.getItems().addAll(miEdit, new SeparatorMenuItem(), miDel);
            tablaUsuarios11.setContextMenu(cm);
        }

        // ----- Tareas -----
        if (tablaTareas != null) {
            ContextMenu cm = new ContextMenu();
            MenuItem miVer = new MenuItem(esProfesor ? "Ver entregas" : "Entregar tarea");
            miVer.setOnAction(e -> verDetalleTarea());
            cm.getItems().add(miVer);
            if (esProfesor) {
                MenuItem miDel = new MenuItem("Eliminar tarea");
                miDel.setOnAction(e -> eliminarTarea());
                cm.getItems().addAll(new SeparatorMenuItem(), miDel);
            }
            tablaTareas.setContextMenu(cm);
        }

        // ----- Foro -----
        if (tablaForo != null) {
            ContextMenu cm = new ContextMenu();
            MenuItem miVer = new MenuItem("Ver hilo / Responder");
            miVer.setOnAction(e -> verHiloForo());
            cm.getItems().add(miVer);
            if (esProfesor) {
                MenuItem miDel = new MenuItem("Eliminar hilo");
                miDel.setOnAction(e -> eliminarHiloForo());
                cm.getItems().addAll(new SeparatorMenuItem(), miDel);
            }
            tablaForo.setContextMenu(cm);
        }

        // ----- Control de Asistencia (solo profesor puede eliminar) -----
        if (tablaControlAsistencia != null && esProfesor) {
            ContextMenu cm = new ContextMenu();
            MenuItem miDel = new MenuItem("Eliminar registro");
            miDel.setOnAction(e -> eliminarControlAsistenciaSeleccionada());
            cm.getItems().add(miDel);
            tablaControlAsistencia.setContextMenu(cm);
        }
    }

    private void eliminarControlAsistenciaSeleccionada() {
        if (!Configuracion.esProfesor()) return;
        ControlAsistencia sel = tablaControlAsistencia.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta("Error", "Selecciona un registro"); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminacion");
        confirm.setHeaderText(null);
        confirm.setContentText("Eliminar el registro de asistencia seleccionado?");
        aplicarTemaDialogo(confirm);
        agregarIconoAlerta(confirm);
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM CONTROL_ASISTENCIA WHERE id_control = ?");
            stmt.setInt(1, sel.getIdControl());
            stmt.executeUpdate(); stmt.close();
            cargarControlAsistencia();
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    // =====================================================================
    //  FXML HANDLERS (called from FXML via onAction)
    // =====================================================================

    @FXML
    void handleExpulsar(ActionEvent event) {
        if (!Configuracion.esProfesor()) { mostrarAlerta("Error", "No tienes permisos para realizar esta accion"); return; }
        if (vboxUsuarios.isVisible()) {
            Usuario sel = tablaUsuarios1.getSelectionModel().getSelectedItem();
            if (sel != null) borrarUsuario(sel);
            else mostrarAlerta("Error", "Selecciona un usuario");
        }
    }

    @FXML
    void handleVerPerfil(ActionEvent event) {
        if (vboxUsuarios.isVisible()) {
            Usuario sel = tablaUsuarios1.getSelectionModel().getSelectedItem();
            if (sel != null) abrirPerfilUsuario(sel);
            else mostrarAlerta("Error", "Selecciona un usuario");
        }
    }

    // =====================================================================
    //  COURSE CARDS
    // =====================================================================

    public void cargarCursos() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt;
            if (Configuracion.esProfesor()) {
                stmt = conn.prepareStatement("SELECT id_curso, nombre_curso, descripcion, cant_usuarios FROM CURSO");
            } else {
                stmt = conn.prepareStatement(
                    "SELECT c.id_curso, c.nombre_curso, c.descripcion, c.cant_usuarios " +
                    "FROM CURSO c INNER JOIN ASISTENCIA a ON c.id_curso = a.id_curso " +
                    "WHERE a.id_usuario = ? AND a.matriculado = 1");
                stmt.setInt(1, Configuracion.getIdUsuarioActual());
            }
            ResultSet rs = stmt.executeQuery();
            todosLosCursos.clear();
            while (rs.next()) {
                todosLosCursos.add(new Curso(
                    rs.getInt("id_curso"),
                    rs.getString("nombre_curso"),
                    rs.getString("descripcion"),
                    rs.getInt("cant_usuarios")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los cursos: " + e.getMessage());
        }
        renderizarCards(todosLosCursos);
    }

    private void renderizarCards(List<Curso> cursos) {
        if (flowCursos == null) return;
        flowCursos.getChildren().clear();
        if (cursos.isEmpty()) {
            Label vacio = new Label("No hay cursos disponibles.");
            vacio.setStyle("-fx-text-fill: #8a9ab0; -fx-font-size: 14px; -fx-padding: 20;");
            flowCursos.getChildren().add(vacio);
            return;
        }
        for (Curso c : cursos) {
            flowCursos.getChildren().add(crearCardCurso(c));
        }
    }

    private VBox crearCardCurso(Curso curso) {
        VBox card = new VBox();
        card.getStyleClass().add("card-curso");
        card.setCursor(Cursor.HAND);

        // Top color bar
        Region barra = new Region();
        barra.setMinHeight(5);
        barra.setStyle("-fx-background-color: #0066cc; -fx-background-radius: 8 8 0 0;");
        barra.setMaxWidth(Double.MAX_VALUE);

        VBox contenido = new VBox(8);
        contenido.setPadding(new Insets(12, 14, 12, 14));
        VBox.setVgrow(contenido, Priority.ALWAYS);

        Label nombre = new Label(curso.getNombreCurso());
        nombre.getStyleClass().add("card-curso-titulo");
        nombre.setWrapText(true);

        String descTexto = curso.getDescripcion() != null ? curso.getDescripcion() : "";
        if (descTexto.length() > 90) descTexto = descTexto.substring(0, 90) + "...";
        Label desc = new Label(descTexto);
        desc.getStyleClass().add("card-curso-desc");
        desc.setWrapText(true);
        VBox.setVgrow(desc, Priority.ALWAYS);

        Label meta = new Label("Alumnos: " + curso.getCantUsuarios());
        meta.getStyleClass().add("card-curso-meta");

        HBox botones = new HBox(8);
        Button btnVer = new Button("Ver curso");
        btnVer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnVer, Priority.ALWAYS);
        btnVer.setTooltip(new Tooltip("Abrir el detalle del curso"));
        btnVer.setOnAction(e -> { animarBoton(btnVer); abrirDetalleCurso(curso); });
        botones.getChildren().add(btnVer);

        if (Configuracion.esProfesor()) {
            Button btnDel = new Button("Eliminar");
            btnDel.getStyleClass().add("btn-danger");
            btnDel.setTooltip(new Tooltip("Eliminar este curso permanentemente"));
            btnDel.setOnAction(e -> borrarCurso(curso));
            botones.getChildren().add(btnDel);
        }

        contenido.getChildren().addAll(nombre, desc, meta, botones);
        card.getChildren().addAll(barra, contenido);
        card.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) abrirDetalleCurso(curso);
        });

        return card;
    }

    private void filtrarCursos(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            renderizarCards(todosLosCursos);
            return;
        }
        List<Curso> filtrados = new ArrayList<>();
        for (Curso c : todosLosCursos) {
            if (c.getNombreCurso().toLowerCase().contains(filtro)
                    || c.getDescripcion().toLowerCase().contains(filtro)
                    || String.valueOf(c.getIdCurso()).contains(filtro)) {
                filtrados.add(c);
            }
        }
        renderizarCards(filtrados);
    }

    // =====================================================================
    //  NEWS (WEB SCRAPING)
    // =====================================================================

    private void cargarNoticias() {
        if (lblEstadoNoticias != null) lblEstadoNoticias.setText("Cargando noticias...");
        if (vboxListaNoticias != null) vboxListaNoticias.getChildren().clear();
        if (btnCargarNoticias != null) btnCargarNoticias.setDisable(true);

        Thread hilo = new Thread(() -> {
            List<Noticia> noticias = ejecutarScriptNoticias();
            Platform.runLater(() -> {
                if (btnCargarNoticias != null) btnCargarNoticias.setDisable(false);
                if (noticias.isEmpty()) {
                    if (lblEstadoNoticias != null)
                        lblEstadoNoticias.setText("No se pudieron cargar las noticias. Comprueba que Python esta instalado y tienes conexion a internet.");
                } else {
                    if (lblEstadoNoticias != null)
                        lblEstadoNoticias.setText(noticias.size() + " noticias cargadas.");
                    if (vboxListaNoticias != null) {
                        LinkedHashMap<String, List<Noticia>> porSeccion = new LinkedHashMap<>();
                        for (Noticia n : noticias) {
                            porSeccion.computeIfAbsent(n.getSeccion(), k -> new ArrayList<>()).add(n);
                        }
                        for (Map.Entry<String, List<Noticia>> entry : porSeccion.entrySet()) {
                            Label secLabel = new Label(entry.getKey().toUpperCase());
                            secLabel.getStyleClass().add("section-title");
                            secLabel.setPadding(new Insets(14, 0, 4, 0));
                            vboxListaNoticias.getChildren().add(secLabel);
                            for (Noticia n : entry.getValue()) {
                                vboxListaNoticias.getChildren().add(crearCardNoticia(n));
                            }
                        }
                    }
                }
            });
        });
        hilo.setDaemon(true);
        hilo.start();
    }

    private List<Noticia> ejecutarScriptNoticias() {
        List<Noticia> resultado = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/noticias.py");
            if (is == null) return resultado;

            Path scriptTemp = Files.createTempFile("muudle_noticias", ".py");
            Files.copy(is, scriptTemp, StandardCopyOption.REPLACE_EXISTING);
            scriptTemp.toFile().deleteOnExit();

            String python = encontrarPython();
            if (python == null) return resultado;

            ProcessBuilder pb = new ProcessBuilder(python, "-X", "utf8", scriptTemp.toString());
            pb.environment().put("PYTHONIOENCODING", "utf-8");
            pb.redirectErrorStream(true);
            Process proceso = pb.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proceso.getInputStream(), StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    linea = linea.trim();
                    if (linea.isEmpty()) continue;
                    // Protocolo: 7 campos separados por |||
                    String[] partes = linea.split("\\|\\|\\|", 7);
                    if (partes.length < 2) continue;
                    String titulo  = partes[0];
                    String url     = partes[1];
                    String fecha   = partes.length > 2 ? partes[2] : "";
                    String fuente  = partes.length > 3 ? partes[3] : "Desconocida";
                    String desc    = partes.length > 4 ? partes[4] : "";
                    String seccion = partes.length > 5 ? partes[5] : "";
                    // El cuerpo viene con saltos de linea codificados como literal \n
                    String cuerpo  = partes.length > 6 ? partes[6].replace("\\n", "\n") : "";
                    if (!titulo.isEmpty()) {
                        resultado.add(new Noticia(titulo, url, fecha, fuente, desc, seccion, cuerpo));
                    }
                }
            }
            proceso.waitFor();
            Files.deleteIfExists(scriptTemp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    private String encontrarPython() {
        String[] candidatos = {"py", "python", "python3",
            "C:\\Python313\\python.exe", "C:\\Python312\\python.exe",
            "C:\\Python311\\python.exe", "C:\\Python310\\python.exe"};
        for (String cmd : candidatos) {
            try {
                ProcessBuilder pb = new ProcessBuilder(cmd, "--version");
                pb.redirectErrorStream(true);
                Process test = pb.start();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(test.getInputStream(), StandardCharsets.UTF_8))) {
                    while (br.readLine() != null) {}
                }
                test.waitFor();
                if (test.exitValue() == 0) return cmd;
            } catch (Exception ignored) {}
        }
        return null;
    }

    private Map<String, Integer> obtenerCursosParaCombo() {
        Map<String, Integer> cursos = new LinkedHashMap<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT id_curso, nombre_curso FROM CURSO ORDER BY nombre_curso");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cursos.put(rs.getString("nombre_curso"), rs.getInt("id_curso"));
            }
            rs.close(); stmt.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return cursos;
    }

    private VBox crearCardNoticia(Noticia noticia) {
        VBox card = new VBox(6);
        card.getStyleClass().add("card-noticia");

        Label titulo = new Label(noticia.getTitulo());
        titulo.getStyleClass().add("noticia-titulo");
        titulo.setWrapText(true);
        titulo.setCursor(Cursor.HAND);
        titulo.setOnMouseClicked(e -> abrirUrl(noticia.getUrl()));

        Label meta = new Label(noticia.getFuente()
                + (noticia.getFecha().isEmpty() ? "" : "  •  " + noticia.getFecha()));
        meta.getStyleClass().add("noticia-meta");

        if (!noticia.getDescripcion().isEmpty()) {
            Label desc = new Label(noticia.getDescripcion());
            desc.getStyleClass().add("noticia-desc");
            desc.setWrapText(true);
            card.getChildren().addAll(titulo, meta, desc);
        } else {
            card.getChildren().addAll(titulo, meta);
        }

        HBox botones = new HBox(8);
        botones.setPadding(new Insets(4, 0, 0, 0));

        if (!noticia.getUrl().isEmpty()) {
            Button btnAbrir = new Button("Ver noticia");
            btnAbrir.setTooltip(new Tooltip("Abrir la noticia en el navegador"));
            btnAbrir.setOnAction(e -> abrirUrl(noticia.getUrl()));
            botones.getChildren().add(btnAbrir);
        }

        Button btnDescargar = new Button("Descargar");
        btnDescargar.getStyleClass().add("btn-secondary");
        btnDescargar.setTooltip(new Tooltip("Guardar noticia como archivo de texto"));
        btnDescargar.setOnAction(e -> descargarNoticia(noticia));
        botones.getChildren().add(btnDescargar);

        card.getChildren().add(botones);
        return card;
    }

    private void abrirUrl(String urlStr) {
        if (urlStr == null || urlStr.isEmpty()) return;
        try {
            Desktop.getDesktop().browse(new URI(urlStr));
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir el navegador: " + e.getMessage());
        }
    }

    private void descargarNoticia(Noticia noticia) {
        try {
            String nombreArchivo = noticia.getTitulo()
                    .replaceAll("[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ _-]", "")
                    .trim();
            if (nombreArchivo.length() > 80) nombreArchivo = nombreArchivo.substring(0, 80);
            if (nombreArchivo.isEmpty()) nombreArchivo = "noticia";

            FileChooser fc = new FileChooser();
            fc.setTitle("Guardar noticia");
            fc.setInitialFileName(nombreArchivo + ".txt");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto (*.txt)", "*.txt"));
            File archivo = fc.showSaveDialog(vboxNoticias.getScene().getWindow());
            if (archivo == null) return;

            // Fetch del articulo completo (puede tardar 1-3s, lo hacemos en hilo)
            Thread hilo = new Thread(() -> {
                // El cuerpo viene ya incluido desde el script Python (campo 7).
                // Si por algun motivo viene vacio (script antiguo), hacemos
                // un intento de scraping al vuelo como fallback.
                String cuerpoTmp = noticia.getCuerpo();
                if (cuerpoTmp == null || cuerpoTmp.trim().isEmpty()) {
                    cuerpoTmp = obtenerCuerpoArticulo(noticia.getUrl());
                }
                // Variable final para poder usarla dentro del lambda inferior.
                final String cuerpoCompleto = cuerpoTmp == null ? "" : cuerpoTmp;
                String contenido = construirTxtNoticia(noticia, cuerpoCompleto);
                try {
                    Files.write(archivo.toPath(), contenido.getBytes(StandardCharsets.UTF_8));
                    Platform.runLater(() ->
                        mostrarAlerta("Guardado", "Noticia guardada en:\n" + archivo.getAbsolutePath()
                                + (cuerpoCompleto.isEmpty()
                                    ? "\n\n(Aviso: no se pudo extraer el cuerpo completo del articulo. "
                                      + "El archivo contiene titulo, resumen y URL.)"
                                    : "")));
                } catch (IOException e) {
                    Platform.runLater(() ->
                        mostrarAlerta("Error", "No se pudo guardar la noticia: " + e.getMessage()));
                }
            });
            hilo.setDaemon(true);
            hilo.start();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar la noticia: " + e.getMessage());
        }
    }

    /**
     * Formatea el .txt con secciones legibles.
     */
    private String construirTxtNoticia(Noticia n, String cuerpoCompleto) {
        String sep1 = "================================================================\n";
        String sep2 = "----------------------------------------------------------------\n";
        StringBuilder sb = new StringBuilder();
        sb.append(sep1).append("  TITULO\n").append(sep1).append("\n");
        sb.append(n.getTitulo()).append("\n\n\n");

        sb.append(sep2).append("  INFORMACION GENERAL\n").append(sep2).append("\n");
        sb.append(String.format("Fuente:      %s%n", n.getFuente()));
        sb.append(String.format("Fecha:       %s%n", n.getFecha().isEmpty() ? "(no disponible)" : n.getFecha()));
        sb.append(String.format("Seccion:     %s%n", n.getSeccion()));
        sb.append(String.format("URL:         %s%n", n.getUrl()));
        sb.append(String.format("Descargado:  %s%n",
            java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        sb.append("\n\n");

        sb.append(sep2).append("  RESUMEN\n").append(sep2).append("\n");
        sb.append(n.getDescripcion().isEmpty() ? "(sin resumen)" : n.getDescripcion()).append("\n\n\n");

        sb.append(sep2).append("  ARTICULO COMPLETO\n").append(sep2).append("\n");
        if (cuerpoCompleto == null || cuerpoCompleto.trim().isEmpty()) {
            sb.append("(No se pudo extraer el cuerpo del articulo automaticamente.\n");
            sb.append(" Abre la URL en tu navegador para leerlo completo.)\n\n\n");
        } else {
            sb.append(cuerpoCompleto).append("\n\n\n");
        }

        sb.append(sep1);
        sb.append("  Generado por Muudle - Noticias Educativas\n");
        sb.append(sep1);
        return sb.toString();
    }

    /**
     * Descarga la pagina del articulo y extrae el cuerpo principal en
     * texto plano (heuristica: prioriza <article>, fallback a <main>,
     * fallback final a todos los <p> de la pagina).
     */
    private String obtenerCuerpoArticulo(String urlStr) {
        if (urlStr == null || urlStr.isEmpty() || urlStr.equals("#")) return "";
        try {
            java.net.HttpURLConnection conn =
                (java.net.HttpURLConnection) new java.net.URI(urlStr).toURL().openConnection();
            // Headers que mimentan a un navegador real. Sin esto, El Pais
            // (y otros medios con anti-bot) devuelven 403 Forbidden.
            conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
            conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "es-ES,es;q=0.9,en;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "identity");   // sin gzip
            conn.setRequestProperty("Referer", inferirReferer(urlStr));
            conn.setRequestProperty("Sec-Fetch-Dest", "document");
            conn.setRequestProperty("Sec-Fetch-Mode", "navigate");
            conn.setRequestProperty("Sec-Fetch-Site", "same-origin");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(15000);
            conn.setInstanceFollowRedirects(true);

            int status = conn.getResponseCode();
            if (status < 200 || status >= 300) {
                return "";
            }

            StringBuilder html = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String linea;
                int max = 1_500_000;  // cota: 1.5 MB de HTML
                while ((linea = br.readLine()) != null && html.length() < max) {
                    html.append(linea).append('\n');
                }
            }

            String htmlStr = html.toString();

            // Estrategia 1: extractor especifico segun el dominio (mas preciso)
            String host = inferirHost(urlStr);
            if (host.contains("elpais.com")) {
                String texto = extraerCuerpoElPais(htmlStr);
                if (!texto.isEmpty()) return texto;
            }

            // Estrategia 2 (fallback): extractor generico <article> / <main>
            return extraerCuerpoHtml(htmlStr);
        } catch (Exception e) {
            return "";
        }
    }

    /** Devuelve el origen (scheme + host) de la URL para usarlo como Referer. */
    private String inferirReferer(String urlStr) {
        try {
            java.net.URI u = new java.net.URI(urlStr);
            return u.getScheme() + "://" + u.getHost() + "/";
        } catch (Exception e) {
            return "https://www.google.com/";
        }
    }

    private String inferirHost(String urlStr) {
        try {
            String h = new java.net.URI(urlStr).getHost();
            return h == null ? "" : h.toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Extractor especifico para articulos de elpais.com.
     * Estructura observada: <div class="a_c ..."> contiene el cuerpo.
     * Hay que cortar antes de <footer/aside de paywall/etc para no incluir basura.
     */
    private String extraerCuerpoElPais(String html) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(
            "(?is)<div[^>]*class=\"[^\"]*\\ba_c\\b[^\"]*\"[^>]*>(.*)").matcher(html);
        if (!m.find()) return "";

        String body = m.group(1);

        // Cortar en el primer marcador de "fin de articulo"
        int cut = -1;
        String[] cierres = { "<footer", "</article", "class=\"a_pl",
                             "class=\"a_b_pl", "id=\"comments", "<aside class=\"a_pl" };
        for (String t : cierres) {
            int p = body.indexOf(t);
            if (p > 0 && (cut == -1 || p < cut)) cut = p;
        }
        if (cut > 0) body = body.substring(0, cut);

        // Eliminar ruido estructural
        body = body.replaceAll("(?is)<script[^>]*>.*?</script>", "");
        body = body.replaceAll("(?is)<style[^>]*>.*?</style>", "");
        body = body.replaceAll("(?is)<aside[^>]*>.*?</aside>", "");
        body = body.replaceAll("(?is)<figure[^>]*>.*?</figure>", "");
        body = body.replaceAll("(?is)<noscript[^>]*>.*?</noscript>", "");
        body = body.replaceAll("(?is)<!--.*?-->", "");

        StringBuilder out = new StringBuilder();
        java.util.regex.Matcher mp = java.util.regex.Pattern.compile(
            "(?is)<(?:p|h2|h3)[^>]*>(.*?)</(?:p|h2|h3)>").matcher(body);
        while (mp.find()) {
            String parrafo = limpiarHtmlTexto(mp.group(1));
            if (parrafo.length() > 20) {
                out.append(parrafo).append("\n\n");
            }
        }
        return out.toString().trim();
    }

    private String extraerCuerpoHtml(String html) {
        // Eliminar bloques de codigo no-texto
        html = html.replaceAll("(?is)<script[^>]*>.*?</script>", "");
        html = html.replaceAll("(?is)<style[^>]*>.*?</style>", "");
        html = html.replaceAll("(?is)<noscript[^>]*>.*?</noscript>", "");
        html = html.replaceAll("(?is)<!--.*?-->", "");

        // 1) Buscar <article>...</article>; 2) fallback <main>...</main>;
        // 3) fallback toda la pagina.
        String zona = html;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(
                "(?is)<article[^>]*>(.*?)</article>").matcher(html);
        if (m.find()) {
            zona = m.group(1);
        } else {
            m = java.util.regex.Pattern.compile("(?is)<main[^>]*>(.*?)</main>").matcher(html);
            if (m.find()) zona = m.group(1);
        }

        // Extraer parrafos
        StringBuilder texto = new StringBuilder();
        java.util.regex.Matcher mp = java.util.regex.Pattern.compile(
                "(?is)<(?:p|h2|h3|li)[^>]*>(.*?)</(?:p|h2|h3|li)>").matcher(zona);
        while (mp.find()) {
            String parrafo = limpiarHtmlTexto(mp.group(1));
            if (parrafo.length() > 30) {
                texto.append(parrafo).append("\n\n");
            }
        }
        return texto.toString().trim();
    }

    /**
     * Quita tags HTML residuales, decodifica entidades comunes (espanol)
     * y normaliza espacios.
     */
    private String limpiarHtmlTexto(String s) {
        if (s == null) return "";
        s = s.replaceAll("(?is)<[^>]+>", " ");
        // Entidades HTML mas frecuentes en prensa espanola
        s = s.replace("&nbsp;", " ")
             .replace("&amp;",  "&")
             .replace("&lt;",   "<")
             .replace("&gt;",   ">")
             .replace("&quot;", "\"")
             .replace("&apos;", "'")
             .replace("&#39;",  "'")
             .replace("&hellip;", "...")
             .replace("&mdash;", "—").replace("&ndash;", "–")
             .replace("&laquo;", "«").replace("&raquo;", "»")
             .replace("&iquest;", "¿").replace("&iexcl;", "¡")
             .replace("&aacute;", "á").replace("&Aacute;", "Á")
             .replace("&eacute;", "é").replace("&Eacute;", "É")
             .replace("&iacute;", "í").replace("&Iacute;", "Í")
             .replace("&oacute;", "ó").replace("&Oacute;", "Ó")
             .replace("&uacute;", "ú").replace("&Uacute;", "Ú")
             .replace("&ntilde;", "ñ").replace("&Ntilde;", "Ñ")
             .replace("&uuml;",   "ü").replace("&Uuml;",   "Ü");
        // Entidades numericas decimales y hex (best-effort)
        s = java.util.regex.Pattern.compile("&#(\\d+);").matcher(s)
                .replaceAll(mr -> {
                    try { return new String(Character.toChars(Integer.parseInt(mr.group(1)))); }
                    catch (Exception ex) { return ""; }
                });
        s = java.util.regex.Pattern.compile("&#x([0-9a-fA-F]+);").matcher(s)
                .replaceAll(mr -> {
                    try { return new String(Character.toChars(Integer.parseInt(mr.group(1), 16))); }
                    catch (Exception ex) { return ""; }
                });
        return s.replaceAll("\\s+", " ").trim();
    }

    // =====================================================================
    //  USUARIOS
    // =====================================================================

    public void cargarUsuarios() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt;
            if (Configuracion.esProfesor()) {
                stmt = conn.prepareStatement(
                    "SELECT id_usuario, nombre, apellido, email, tipo_usuario, edad FROM USUARIO");
            } else {
                stmt = conn.prepareStatement(
                    "SELECT DISTINCT u.id_usuario, u.nombre, u.apellido, u.email, u.tipo_usuario, u.edad " +
                    "FROM USUARIO u INNER JOIN ASISTENCIA a ON u.id_usuario = a.id_usuario " +
                    "WHERE a.id_curso IN (SELECT id_curso FROM ASISTENCIA WHERE id_usuario = ?)");
                stmt.setInt(1, Configuracion.getIdUsuarioActual());
            }
            ResultSet rs = stmt.executeQuery();
            todosLosUsuarios.clear();
            while (rs.next()) {
                todosLosUsuarios.add(new Usuario(
                    rs.getInt("id_usuario"), rs.getString("nombre"), rs.getString("apellido"),
                    rs.getString("email"), rs.getString("tipo_usuario"), rs.getInt("edad")));
            }
            tablaUsuarios1.setItems(todosLosUsuarios);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }

    private void filtrarUsuarios(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) { tablaUsuarios1.setItems(todosLosUsuarios); return; }
        ObservableList<Usuario> f = FXCollections.observableArrayList();
        for (Usuario u : todosLosUsuarios) {
            if (u.getNombre().toLowerCase().contains(filtro)
                    || u.getApellidos().toLowerCase().contains(filtro)
                    || u.getEmail().toLowerCase().contains(filtro)
                    || String.valueOf(u.getIdUsuario()).contains(filtro)) f.add(u);
        }
        tablaUsuarios1.setItems(f);
    }

    // =====================================================================
    //  MATRICULACIÓN / ASISTENCIA
    // =====================================================================

    public void cargarAsistencias() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt;
            if (Configuracion.esProfesor()) {
                stmt = conn.prepareStatement(
                    "SELECT u.id_usuario, u.nombre, u.apellido, a.nFaltas, a.nota, a.id_curso, c.nombre_curso " +
                    "FROM ASISTENCIA a JOIN USUARIO u ON a.id_usuario = u.id_usuario " +
                    "JOIN CURSO c ON a.id_curso = c.id_curso WHERE a.matriculado = 1");
            } else {
                stmt = conn.prepareStatement(
                    "SELECT u.id_usuario, u.nombre, u.apellido, a.nFaltas, a.nota, a.id_curso, c.nombre_curso " +
                    "FROM ASISTENCIA a JOIN USUARIO u ON a.id_usuario = u.id_usuario " +
                    "JOIN CURSO c ON a.id_curso = c.id_curso WHERE a.id_usuario = ? AND a.matriculado = 1");
                stmt.setInt(1, Configuracion.getIdUsuarioActual());
            }
            ResultSet rs = stmt.executeQuery();
            todasLasAsistencias.clear();
            while (rs.next()) {
                todasLasAsistencias.add(new Asistencia(
                    rs.getInt("id_usuario"), rs.getString("nombre"), rs.getString("apellido"),
                    rs.getInt("nFaltas"), String.valueOf(rs.getDouble("nota")),
                    rs.getInt("id_curso"), rs.getString("nombre_curso")));
            }
            tablaUsuarios11.setItems(todasLasAsistencias);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar las matriculaciones: " + e.getMessage());
        }
    }

    private void filtrarAsistencias(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) { tablaUsuarios11.setItems(todasLasAsistencias); return; }
        ObservableList<Asistencia> f = FXCollections.observableArrayList();
        for (Asistencia a : todasLasAsistencias) {
            if (a.getNombre().toLowerCase().contains(filtro)
                    || a.getApellidos().toLowerCase().contains(filtro)
                    || a.getNombreCurso().toLowerCase().contains(filtro)
                    || String.valueOf(a.getIdUsuario()).contains(filtro)) f.add(a);
        }
        tablaUsuarios11.setItems(f);
    }

    private void eliminarMatriculaSeleccionada() {
        if (!Configuracion.esProfesor()) { mostrarAlerta("Error", "Sin permisos"); return; }
        Asistencia sel = tablaUsuarios11.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta("Error", "Selecciona una matriculacion"); return; }
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE ASISTENCIA SET matriculado = 0, nFaltas = 0, nota = 0.0 WHERE id_usuario = ? AND id_curso = ?");
            stmt.setInt(1, sel.getIdUsuario());
            stmt.setInt(2, sel.getIdCurso());
            stmt.executeUpdate();
            stmt.close();
            cargarAsistencias();
            cargarCursos();
            mostrarAlerta("Exito", "Matriculacion eliminada correctamente");
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    // =====================================================================
    //  TABLE COLUMNS SETUP
    // =====================================================================

    private void configurarColumnas() {
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

        if (colTareaTitulo != null) colTareaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        if (colTareaCurso != null) colTareaCurso.setCellValueFactory(new PropertyValueFactory<>("nombreCurso"));
        if (colTareaFechaLimite != null) colTareaFechaLimite.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));
        if (colTareaPuntuacion != null) colTareaPuntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacionMaxima"));
        if (colTareaEstado != null) colTareaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        if (colForoTitulo != null) colForoTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        if (colForoAutor != null) colForoAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        if (colForoCurso != null) colForoCurso.setCellValueFactory(new PropertyValueFactory<>("nombreCurso"));
        if (colForoFecha != null) colForoFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        if (colAsistNombre != null) colAsistNombre.setCellValueFactory(new PropertyValueFactory<>("nombreAlumno"));
        if (colAsistCurso != null) colAsistCurso.setCellValueFactory(new PropertyValueFactory<>("nombreCurso"));
        if (colAsistFecha != null) colAsistFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        if (colAsistEstado != null) colAsistEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        if (colAsistObservaciones != null) colAsistObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
    }

    // =====================================================================
    //  CRUD WINDOWS (open modals)
    // =====================================================================

    public void lanzarCrearCurso() {
        if (!Configuracion.esProfesor()) { mostrarAlerta("Error", "Sin permisos"); return; }
        try {
            Stage modal = new Stage();
            modal.setTitle("Crear Curso");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnCrearCurso.getScene().getWindow());
            agregarIconoStage(modal);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearCurso.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            modal.setScene(scene);
            root.setOpacity(0);
            FadeTransition fi = new FadeTransition(Duration.millis(250), root);
            fi.setFromValue(0); fi.setToValue(1);
            modal.show();
            fi.play();
            CrearCursoController ctrl = loader.getController();
            ctrl.setCursosController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void lanzarCrearMatricula() {
        if (!Configuracion.esProfesor()) { mostrarAlerta("Error", "Sin permisos"); return; }
        try {
            Stage modal = new Stage();
            modal.setTitle("Crear Matriculacion");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnCrearMatricula.getScene().getWindow());
            agregarIconoStage(modal);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearMatricula.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            modal.setScene(scene);
            root.setOpacity(0);
            FadeTransition fi = new FadeTransition(Duration.millis(250), root);
            fi.setFromValue(0); fi.setToValue(1);
            modal.show();
            fi.play();
            CrearMatriculaController ctrl = loader.getController();
            ctrl.setCursosController(this);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana: " + e.getMessage());
        }
    }

    public void lanzarInformes() {
        if (!Configuracion.esProfesor()) { mostrarAlerta("Error", "Sin permisos"); return; }
        try {
            Stage modal = new Stage();
            modal.setTitle("Generador de Informes");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnInformes.getScene().getWindow());
            agregarIconoStage(modal);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Informes.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            modal.setScene(scene);
            modal.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir informes: " + e.getMessage());
        }
    }

    public void lanzarInsertar() {
        if (!Configuracion.esProfesor()) { mostrarAlerta("Error", "Sin permisos"); return; }
        Asistencia sel = tablaUsuarios11.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta("Error", "Selecciona una matriculacion"); return; }
        try {
            Stage modal = new Stage();
            modal.setTitle("Actualizar Informacion");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(btnInsertar.getScene().getWindow());
            agregarIconoStage(modal);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Insertar.fxml"));
            Scene scene = new Scene(loader.load());
            Main.aplicarTema(scene);
            modal.setScene(scene);
            InsertarController ctrl = loader.getController();
            ctrl.setCursosController(this);
            ctrl.setDatosAlumno(sel.getIdUsuario(), sel.getNombre(), sel.getApellidos(),
                    sel.getIdCurso(), sel.getNombreCurso(), sel.getFaltas(),
                    Double.parseDouble(sel.getNota()));
            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =====================================================================
    //  DETAIL VIEWS
    // =====================================================================

    private void abrirDetalleCurso(Curso curso) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Detalle: " + curso.getNombreCurso());
            agregarIconoStage(stage);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CursoDetalle.fxml"));
            Scene scene = new Scene(loader.load());
            Main.aplicarTema(scene);
            stage.setScene(scene);
            CursoDetalleController ctrl = loader.getController();
            ctrl.cargarDatosCurso(curso.getIdCurso());
            ctrl.setCursosController(this);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el detalle: " + e.getMessage());
        }
    }

    private void abrirPerfilUsuario(Usuario usuario) {
        try {
            Stage modal = new Stage();
            modal.setTitle("Perfil de Usuario");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(tablaUsuarios1.getScene().getWindow());
            agregarIconoStage(modal);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PerfilDetalle.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.aplicarTema(scene);
            PerfilDetalleController ctrl = loader.getController();
            ctrl.cargarDatosUsuario(usuario);
            ctrl.setCursosController(this);
            modal.setScene(scene);
            modal.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el perfil: " + e.getMessage());
        }
    }

    // =====================================================================
    //  NAVIGATION HELPERS
    // =====================================================================

    private void abrirVentana(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
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

    // =====================================================================
    //  DB OPERATIONS
    // =====================================================================

    private void borrarCurso(Curso curso) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminacion");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar el curso \"" + curso.getNombreCurso() + "\"? Esta accion no se puede deshacer.");
        aplicarTemaDialogo(confirm);
        agregarIconoAlerta(confirm);
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement s1 = conn.prepareStatement("DELETE FROM ASISTENCIA WHERE id_curso = ?");
            s1.setInt(1, curso.getIdCurso()); s1.executeUpdate(); s1.close();
            PreparedStatement s2 = conn.prepareStatement("DELETE FROM CURSO WHERE id_curso = ?");
            s2.setInt(1, curso.getIdCurso()); s2.executeUpdate(); s2.close();
            cargarCursos();
            cargarAsistencias();
            mostrarAlerta("Exito", "Curso eliminado correctamente");
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    private void borrarUsuario(Usuario usuario) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminacion");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar al usuario \"" + usuario.getNombre() + " " + usuario.getApellidos() + "\"?");
        aplicarTemaDialogo(confirm);
        agregarIconoAlerta(confirm);
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement s1 = conn.prepareStatement("SELECT id_curso FROM ASISTENCIA WHERE id_usuario = ?");
            s1.setInt(1, usuario.getIdUsuario());
            ResultSet rs = s1.executeQuery();
            List<Integer> cursosAfectados = new ArrayList<>();
            while (rs.next()) cursosAfectados.add(rs.getInt("id_curso"));
            rs.close(); s1.close();
            PreparedStatement s2 = conn.prepareStatement("DELETE FROM ASISTENCIA WHERE id_usuario = ?");
            s2.setInt(1, usuario.getIdUsuario()); s2.executeUpdate(); s2.close();
            PreparedStatement s3 = conn.prepareStatement("DELETE FROM USUARIO WHERE id_usuario = ?");
            s3.setInt(1, usuario.getIdUsuario()); s3.executeUpdate(); s3.close();
            for (int idC : cursosAfectados) {
                PreparedStatement su = conn.prepareStatement(
                    "UPDATE CURSO SET cant_usuarios = (SELECT COUNT(*) FROM ASISTENCIA WHERE id_curso = ?) WHERE id_curso = ?");
                su.setInt(1, idC); su.setInt(2, idC); su.executeUpdate(); su.close();
            }
            conn.commit();
            cargarUsuarios(); cargarAsistencias(); cargarCursos();
            mostrarAlerta("Exito", "Usuario eliminado correctamente");
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    // =====================================================================
    //  TAREAS
    // =====================================================================

    private void cargarTareas() {
        todasLasTareas.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt;
            if (Configuracion.esProfesor()) {
                stmt = conn.prepareStatement(
                    "SELECT t.id_tarea, t.titulo, c.nombre_curso, t.fecha_limite, t.puntuacion_maxima, t.id_curso " +
                    "FROM TAREA t JOIN CURSO c ON t.id_curso = c.id_curso ORDER BY t.fecha_limite ASC");
            } else {
                stmt = conn.prepareStatement(
                    "SELECT t.id_tarea, t.titulo, c.nombre_curso, t.fecha_limite, t.puntuacion_maxima, t.id_curso " +
                    "FROM TAREA t JOIN CURSO c ON t.id_curso = c.id_curso " +
                    "WHERE t.id_curso IN (SELECT id_curso FROM ASISTENCIA WHERE id_usuario = ? AND matriculado = 1) " +
                    "ORDER BY t.fecha_limite ASC");
                stmt.setInt(1, Configuracion.getIdUsuarioActual());
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idT = rs.getInt("id_tarea");
                String estado = "Pendiente";
                if (!Configuracion.esProfesor()) {
                    PreparedStatement se = conn.prepareStatement(
                        "SELECT calificacion FROM ENTREGA WHERE id_tarea = ? AND id_alumno = ?");
                    se.setInt(1, idT); se.setInt(2, Configuracion.getIdUsuarioActual());
                    ResultSet re = se.executeQuery();
                    if (re.next()) {
                        estado = re.getObject("calificacion") != null
                            ? "Calificada: " + re.getDouble("calificacion") : "Entregada";
                    }
                    re.close(); se.close();
                } else {
                    PreparedStatement sc = conn.prepareStatement("SELECT COUNT(*) FROM ENTREGA WHERE id_tarea = ?");
                    sc.setInt(1, idT);
                    ResultSet rc = sc.executeQuery();
                    if (rc.next()) estado = rc.getInt(1) + " entregas";
                    rc.close(); sc.close();
                }
                todasLasTareas.add(new Tarea(idT, rs.getString("titulo"), rs.getString("nombre_curso"),
                    rs.getString("fecha_limite"), String.valueOf(rs.getDouble("puntuacion_maxima")),
                    estado, rs.getInt("id_curso")));
            }
            rs.close(); stmt.close();
        } catch (SQLException e) { e.printStackTrace(); }
        if (tablaTareas != null) tablaTareas.setItems(todasLasTareas);
    }

    private void crearTarea() {
        if (!Configuracion.esProfesor()) return;
        Map<String, Integer> cursosMap = obtenerCursosParaCombo();
        if (cursosMap.isEmpty()) { mostrarAlerta("Error", "No hay cursos disponibles"); return; }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Crear Tarea");
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField tituloF = new TextField(); tituloF.setPromptText("Titulo");
        TextArea descF = new TextArea(); descF.setPromptText("Descripcion"); descF.setPrefRowCount(3);
        TextField fechaF = new TextField(); fechaF.setPromptText("YYYY-MM-DD HH:MM");
        ComboBox<String> cursoCombo = new ComboBox<>();
        cursoCombo.getItems().addAll(cursosMap.keySet());
        cursoCombo.getSelectionModel().selectFirst();
        cursoCombo.setMaxWidth(Double.MAX_VALUE);
        TextField puntF = new TextField(); puntF.setPromptText("10.00");
        grid.add(new Label("Titulo:"), 0, 0); grid.add(tituloF, 1, 0);
        grid.add(new Label("Descripcion:"), 0, 1); grid.add(descF, 1, 1);
        grid.add(new Label("Curso:"), 0, 2); grid.add(cursoCombo, 1, 2);
        grid.add(new Label("Fecha limite:"), 0, 3); grid.add(fechaF, 1, 3);
        grid.add(new Label("Puntuacion max:"), 0, 4); grid.add(puntF, 1, 4);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        aplicarTemaDialogo(dialog);
        agregarIconoDialogo(dialog);
        Optional<ButtonType> res = dialog.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                int idCurso = cursosMap.get(cursoCombo.getValue());
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO TAREA (id_curso, id_profesor, titulo, descripcion, fecha_limite, puntuacion_maxima) VALUES (?, ?, ?, ?, ?, ?)");
                stmt.setInt(1, idCurso);
                stmt.setInt(2, Configuracion.getIdUsuarioActual());
                stmt.setString(3, tituloF.getText().trim());
                stmt.setString(4, descF.getText().trim());
                stmt.setString(5, fechaF.getText().trim() + ":00");
                stmt.setDouble(6, Double.parseDouble(puntF.getText().trim()));
                stmt.executeUpdate(); stmt.close();
                cargarTareas();
                mostrarAlerta("Exito", "Tarea creada correctamente");
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo crear: " + e.getMessage());
            }
        }
    }

    private void verDetalleTarea() {
        Tarea sel = tablaTareas.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta("Error", "Selecciona una tarea"); return; }
        if (!Configuracion.esProfesor()) {
            dialogEntregarTarea(sel);
        } else {
            dialogVerEntregasProfesor(sel);
        }
    }

    /**
     * Diálogo de entrega para el ALUMNO. Permite escribir texto Y/O adjuntar
     * un archivo (se guarda como base64 en la columna ENTREGA.archivo).
     * Si ya tenía entrega previa, hace UPDATE; si no, INSERT.
     */
    private void dialogEntregarTarea(Tarea tarea) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Entregar tarea");
        dialog.setHeaderText("Tarea: " + tarea.getTitulo());

        VBox box = new VBox(10);
        box.setPadding(new Insets(12, 14, 12, 14));
        box.setPrefWidth(520);

        Label lblTexto = new Label("Contenido de tu entrega (opcional si adjuntas archivo):");
        lblTexto.getStyleClass().add("section-title");
        TextArea taContenido = new TextArea();
        taContenido.setPromptText("Escribe aquí el texto de tu entrega...");
        taContenido.setPrefRowCount(6);
        taContenido.setWrapText(true);

        Label lblArchivo = new Label("Archivo adjunto (opcional):");
        lblArchivo.getStyleClass().add("section-title");
        HBox filaArch = new HBox(10);
        filaArch.setAlignment(Pos.CENTER_LEFT);
        Button btnElegir = new Button("Elegir archivo...");
        Label lblEstado = new Label("(sin archivo seleccionado)");
        lblEstado.getStyleClass().add("text-secondary");
        filaArch.getChildren().addAll(btnElegir, lblEstado);

        final File[] archivoSel = { null };
        btnElegir.setOnAction(ev -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Adjuntar archivo a la entrega");
            File f = fc.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (f != null) {
                if (f.length() > 10 * 1024 * 1024) {  // 10 MB
                    mostrarAlerta("Error", "El archivo supera el limite de 10 MB.");
                    return;
                }
                archivoSel[0] = f;
                lblEstado.setText(f.getName() + "  (" + (f.length() / 1024) + " KB)");
            }
        });

        // Comprobar si ya entregó (para que sepa que actualiza, no duplica)
        Label lblAviso = new Label();
        lblAviso.getStyleClass().add("text-secondary");
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement st = conn.prepareStatement(
                "SELECT contenido FROM ENTREGA WHERE id_tarea = ? AND id_alumno = ?");
            st.setInt(1, tarea.getIdTarea());
            st.setInt(2, Configuracion.getIdUsuarioActual());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                lblAviso.setText("Ya tienes una entrega previa. Si envías otra, se actualizará.");
                taContenido.setText(rs.getString("contenido") != null ? rs.getString("contenido") : "");
            }
            rs.close(); st.close();
        } catch (SQLException ignored) {}

        box.getChildren().addAll(lblTexto, taContenido, lblArchivo, filaArch, lblAviso);
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        aplicarTemaDialogo(dialog);
        agregarIconoDialogo(dialog);
        Optional<ButtonType> res = dialog.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK) return;

        String contenido = taContenido.getText().trim();
        String archivoB64 = null;
        if (archivoSel[0] != null) {
            try {
                byte[] bytes = Files.readAllBytes(archivoSel[0].toPath());
                archivoB64 = java.util.Base64.getEncoder().encodeToString(bytes);
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo leer el archivo: " + e.getMessage());
                return;
            }
        }
        if (contenido.isEmpty() && archivoB64 == null) {
            mostrarAlerta("Error", "Debes escribir un texto o adjuntar un archivo.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // ¿Existe ya?
            PreparedStatement check = conn.prepareStatement(
                "SELECT id_entrega FROM ENTREGA WHERE id_tarea = ? AND id_alumno = ?");
            check.setInt(1, tarea.getIdTarea());
            check.setInt(2, Configuracion.getIdUsuarioActual());
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                int idEntrega = rs.getInt("id_entrega");
                rs.close(); check.close();
                PreparedStatement up = conn.prepareStatement(
                    "UPDATE ENTREGA SET contenido = ?, archivo = COALESCE(?, archivo), " +
                    "fecha_entrega = CURRENT_TIMESTAMP WHERE id_entrega = ?");
                up.setString(1, contenido);
                if (archivoB64 != null) up.setString(2, archivoB64);
                else                    up.setNull(2, java.sql.Types.LONGVARCHAR);
                up.setInt(3, idEntrega);
                up.executeUpdate(); up.close();
            } else {
                rs.close(); check.close();
                PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO ENTREGA (id_tarea, id_alumno, contenido, archivo) VALUES (?, ?, ?, ?)");
                ins.setInt(1, tarea.getIdTarea());
                ins.setInt(2, Configuracion.getIdUsuarioActual());
                ins.setString(3, contenido);
                if (archivoB64 != null) ins.setString(4, archivoB64);
                else                    ins.setNull(4, java.sql.Types.LONGVARCHAR);
                ins.executeUpdate(); ins.close();
            }
            cargarTareas();
            mostrarAlerta("Exito", "Entrega registrada correctamente"
                + (archivoB64 != null ? "\nArchivo adjunto incluido." : ""));
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo entregar: " + e.getMessage());
        }
    }

    /**
     * Diálogo de entregas para el PROFESOR. Muestra todas las entregas de la
     * tarea, permite descargar el archivo adjunto si existe, y calificar
     * (nota + comentario para el alumno).
     */
    private void dialogVerEntregasProfesor(Tarea tarea) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Entregas de la tarea");
        dialog.setHeaderText("Tarea: " + tarea.getTitulo()
            + "  •  Puntuación máxima: " + tarea.getPuntuacionMaxima());

        VBox listaEntregas = new VBox(10);
        listaEntregas.setPadding(new Insets(8, 6, 8, 6));
        ScrollPane sp = new ScrollPane(listaEntregas);
        sp.setFitToWidth(true);
        sp.setPrefSize(640, 480);
        sp.getStyleClass().add("scroll-pane");

        dialog.getDialogPane().setContent(sp);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        aplicarTemaDialogo(dialog);
        agregarIconoDialogo(dialog);

        renderizarEntregasProfesor(listaEntregas, tarea);

        dialog.showAndWait();
    }

    private void renderizarEntregasProfesor(VBox lista, Tarea tarea) {
        lista.getChildren().clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT e.id_entrega, u.nombre, u.apellido, e.contenido, e.archivo, " +
                "e.calificacion, e.comentario_profesor, e.fecha_entrega, e.fecha_calificacion " +
                "FROM ENTREGA e JOIN USUARIO u ON e.id_alumno = u.id_usuario " +
                "WHERE e.id_tarea = ? ORDER BY e.fecha_entrega DESC");
            stmt.setInt(1, tarea.getIdTarea());
            ResultSet rs = stmt.executeQuery();
            boolean hay = false;
            while (rs.next()) {
                hay = true;
                int idEntrega = rs.getInt("id_entrega");
                String alumno = rs.getString("nombre") + " " + (rs.getString("apellido") != null ? rs.getString("apellido") : "");
                String contenido = rs.getString("contenido");
                String archivo = rs.getString("archivo");
                Object calObj = rs.getObject("calificacion");
                Double cal = calObj != null ? rs.getDouble("calificacion") : null;
                String comentario = rs.getString("comentario_profesor");
                String fechaEntrega = rs.getString("fecha_entrega");

                lista.getChildren().add(crearTarjetaEntrega(
                    tarea, idEntrega, alumno, contenido, archivo, cal,
                    comentario, fechaEntrega, lista));
            }
            rs.close(); stmt.close();
            if (!hay) {
                Label vacio = new Label("Aún no hay entregas para esta tarea.");
                vacio.getStyleClass().add("text-secondary");
                lista.getChildren().add(vacio);
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar entregas: " + e.getMessage());
        }
    }

    private VBox crearTarjetaEntrega(Tarea tarea, int idEntrega, String alumno,
                                     String contenido, String archivoB64, Double cal,
                                     String comentarioPrev, String fechaEntrega,
                                     VBox listaParent) {
        VBox card = new VBox(6);
        card.getStyleClass().add("card-noticia");
        card.setPadding(new Insets(10, 12, 10, 12));

        Label cab = new Label(alumno + "   •   " + (fechaEntrega != null ? fechaEntrega : ""));
        cab.getStyleClass().add("section-title");

        Label lblCont = new Label((contenido == null || contenido.isEmpty())
            ? "(sin texto)" : contenido);
        lblCont.setWrapText(true);
        lblCont.getStyleClass().add("noticia-desc");

        HBox accionesArch = new HBox(8);
        accionesArch.setAlignment(Pos.CENTER_LEFT);
        if (archivoB64 != null && !archivoB64.isEmpty()) {
            Label etiq = new Label("Archivo adjunto:");
            Button btnDescA = new Button("Descargar adjunto");
            btnDescA.setOnAction(ev -> descargarArchivoEntrega(archivoB64, alumno + "_entrega_" + idEntrega));
            accionesArch.getChildren().addAll(etiq, btnDescA);
        } else {
            Label etiq = new Label("Sin archivo adjunto");
            etiq.getStyleClass().add("text-secondary");
            accionesArch.getChildren().add(etiq);
        }

        // Fila calificación + comentario
        Label lblCal = new Label("Nota (0-" + tarea.getPuntuacionMaxima() + "):");
        TextField tfCal = new TextField(cal != null ? String.valueOf(cal) : "");
        tfCal.setPrefWidth(80);
        Label lblCom = new Label("Comentario:");
        TextField tfCom = new TextField(comentarioPrev != null ? comentarioPrev : "");
        HBox.setHgrow(tfCom, Priority.ALWAYS);
        Button btnGuardar = new Button("Guardar calificación");
        btnGuardar.setOnAction(ev -> {
            String n = tfCal.getText().trim();
            if (n.isEmpty()) { mostrarAlerta("Error", "Introduce una nota."); return; }
            try {
                double nota = Double.parseDouble(n.replace(',', '.'));
                guardarCalificacionEntrega(idEntrega, nota, tfCom.getText().trim());
                renderizarEntregasProfesor(listaParent, tarea);
            } catch (NumberFormatException nfe) {
                mostrarAlerta("Error", "La nota no es un número válido.");
            }
        });

        HBox filaCal = new HBox(8, lblCal, tfCal, lblCom, tfCom, btnGuardar);
        filaCal.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(cab, lblCont, accionesArch, new Separator(), filaCal);
        return card;
    }

    private void descargarArchivoEntrega(String archivoB64, String nombreSugerido) {
        try {
            byte[] bytes = java.util.Base64.getDecoder().decode(archivoB64);
            FileChooser fc = new FileChooser();
            fc.setTitle("Guardar archivo adjunto");
            fc.setInitialFileName(nombreSugerido.replaceAll("[^a-zA-Z0-9._-]", "_"));
            File f = fc.showSaveDialog(null);
            if (f != null) {
                Files.write(f.toPath(), bytes);
                mostrarAlerta("Guardado", "Archivo guardado en:\n" + f.getAbsolutePath());
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo descargar el archivo: " + e.getMessage());
        }
    }

    private void guardarCalificacionEntrega(int idEntrega, double nota, String comentario) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement up = conn.prepareStatement(
                "UPDATE ENTREGA SET calificacion = ?, comentario_profesor = ?, " +
                "fecha_calificacion = CURRENT_TIMESTAMP WHERE id_entrega = ?");
            up.setDouble(1, nota);
            up.setString(2, comentario);
            up.setInt(3, idEntrega);
            up.executeUpdate(); up.close();
            cargarTareas();
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo guardar la calificación: " + e.getMessage());
        }
    }

    private void eliminarTarea() {
        if (!Configuracion.esProfesor()) return;
        Tarea sel = tablaTareas.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta("Error", "Selecciona una tarea"); return; }
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM TAREA WHERE id_tarea = ?");
            stmt.setInt(1, sel.getIdTarea()); stmt.executeUpdate(); stmt.close();
            cargarTareas();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // =====================================================================
    //  FORO
    // =====================================================================

    private void cargarHilosForo() {
        todosLosHilos.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT f.id_hilo, f.titulo, u.nombre, c.nombre_curso, f.fecha_creacion, f.contenido " +
                "FROM FORO f JOIN USUARIO u ON f.id_usuario = u.id_usuario " +
                "JOIN CURSO c ON f.id_curso = c.id_curso ORDER BY f.fecha_creacion DESC");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                todosLosHilos.add(new HiloForo(
                    rs.getInt("id_hilo"), rs.getString("titulo"), rs.getString("nombre"),
                    rs.getString("nombre_curso"), rs.getString("fecha_creacion"), rs.getString("contenido")));
            }
            rs.close(); stmt.close();
        } catch (SQLException e) { e.printStackTrace(); }
        if (tablaForo != null) tablaForo.setItems(todosLosHilos);
    }

    private void crearHiloForo() {
        Map<String, Integer> cursosMap = obtenerCursosParaCombo();
        if (cursosMap.isEmpty()) { mostrarAlerta("Error", "No hay cursos disponibles"); return; }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Hilo");
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField tituloF = new TextField(); tituloF.setPromptText("Titulo del hilo");
        TextArea contenidoF = new TextArea(); contenidoF.setPromptText("Contenido"); contenidoF.setPrefRowCount(4);
        ComboBox<String> cursoCombo = new ComboBox<>();
        cursoCombo.getItems().addAll(cursosMap.keySet());
        cursoCombo.getSelectionModel().selectFirst();
        cursoCombo.setMaxWidth(Double.MAX_VALUE);
        grid.add(new Label("Titulo:"), 0, 0); grid.add(tituloF, 1, 0);
        grid.add(new Label("Contenido:"), 0, 1); grid.add(contenidoF, 1, 1);
        grid.add(new Label("Curso:"), 0, 2); grid.add(cursoCombo, 1, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        aplicarTemaDialogo(dialog);
        agregarIconoDialogo(dialog);
        Optional<ButtonType> res = dialog.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                int idCurso = cursosMap.get(cursoCombo.getValue());
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO FORO (id_curso, id_usuario, titulo, contenido) VALUES (?, ?, ?, ?)");
                stmt.setInt(1, idCurso);
                stmt.setInt(2, Configuracion.getIdUsuarioActual());
                stmt.setString(3, tituloF.getText().trim());
                stmt.setString(4, contenidoF.getText().trim());
                stmt.executeUpdate(); stmt.close();
                cargarHilosForo();
                mostrarAlerta("Exito", "Hilo creado correctamente");
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo crear: " + e.getMessage());
            }
        }
    }

    private void verHiloForo() {
        HiloForo sel = tablaForo.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta("Error", "Selecciona un hilo"); return; }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Hilo: " + sel.getTitulo());
        dialog.setHeaderText(sel.getTitulo());

        // ----- Contenedor principal -----
        VBox root = new VBox(10);
        root.setPadding(new Insets(14, 16, 14, 16));
        root.setPrefSize(560, 520);

        // ----- Cabecera del hilo (autor + curso + fecha + contenido) -----
        Label lblMeta = new Label("Por " + sel.getAutor()
                + "  •  " + sel.getNombreCurso()
                + "  •  " + sel.getFecha());
        lblMeta.getStyleClass().add("noticia-meta");

        TextArea contenidoArea = new TextArea(sel.getContenido());
        contenidoArea.setEditable(false);
        contenidoArea.setWrapText(true);
        contenidoArea.setPrefRowCount(3);
        contenidoArea.setStyle("-fx-control-inner-background: derive(-fx-base, 8%);");

        Label lblRespuestas = new Label("Respuestas");
        lblRespuestas.getStyleClass().add("section-title");

        // ----- Lista de respuestas (scrollable) -----
        VBox listaRespuestas = new VBox(8);
        listaRespuestas.setPadding(new Insets(6, 4, 6, 4));
        ScrollPane scrollResp = new ScrollPane(listaRespuestas);
        scrollResp.setFitToWidth(true);
        scrollResp.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scrollResp, Priority.ALWAYS);

        // ----- Caja de respuesta inline -----
        Label lblNuevaResp = new Label("Tu respuesta");
        lblNuevaResp.getStyleClass().add("section-title");

        TextArea respInput = new TextArea();
        respInput.setPromptText("Escribe tu respuesta...");
        respInput.setWrapText(true);
        respInput.setPrefRowCount(3);

        HBox barraBotones = new HBox(10);
        barraBotones.setAlignment(Pos.CENTER_RIGHT);
        Button btnPublicar = new Button("Publicar respuesta");
        btnPublicar.setDefaultButton(true);
        barraBotones.getChildren().add(btnPublicar);

        root.getChildren().addAll(
                lblMeta, contenidoArea, new Separator(),
                lblRespuestas, scrollResp,
                new Separator(),
                lblNuevaResp, respInput, barraBotones
        );

        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        aplicarTemaDialogo(dialog);
        agregarIconoDialogo(dialog);

        // Renderizado de respuestas (extracted -> reutilizable tras publicar)
        Runnable refrescarRespuestas = () -> {
            listaRespuestas.getChildren().clear();
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT u.nombre, fr.contenido, fr.fecha_creacion FROM FORO_RESPUESTA fr " +
                    "JOIN USUARIO u ON fr.id_usuario = u.id_usuario WHERE fr.id_hilo = ? " +
                    "ORDER BY fr.fecha_creacion ASC");
                stmt.setInt(1, sel.getIdHilo());
                ResultSet rs = stmt.executeQuery();
                boolean hay = false;
                while (rs.next()) {
                    hay = true;
                    listaRespuestas.getChildren().add(
                        crearTarjetaRespuestaForo(
                            rs.getString("nombre"),
                            rs.getString("fecha_creacion"),
                            rs.getString("contenido")
                        )
                    );
                }
                if (!hay) {
                    Label vacio = new Label("Aún no hay respuestas. ¡Sé el primero!");
                    vacio.getStyleClass().add("text-secondary");
                    vacio.setPadding(new Insets(8, 4, 8, 4));
                    listaRespuestas.getChildren().add(vacio);
                }
                rs.close(); stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // auto-scroll hasta el final para ver la nueva respuesta
            Platform.runLater(() -> scrollResp.setVvalue(1.0));
        };
        refrescarRespuestas.run();

        // Handler de publicar (no cierra el diálogo)
        btnPublicar.setOnAction(ev -> {
            String texto = respInput.getText().trim();
            if (texto.isEmpty()) {
                respInput.requestFocus();
                return;
            }
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO FORO_RESPUESTA (id_hilo, id_usuario, contenido) VALUES (?, ?, ?)");
                stmt.setInt(1, sel.getIdHilo());
                stmt.setInt(2, Configuracion.getIdUsuarioActual());
                stmt.setString(3, texto);
                stmt.executeUpdate();
                stmt.close();
                respInput.clear();
                refrescarRespuestas.run();
                respInput.requestFocus();
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudo publicar la respuesta: " + e.getMessage());
            }
        });

        dialog.showAndWait();
    }

    private VBox crearTarjetaRespuestaForo(String autor, String fecha, String contenido) {
        VBox card = new VBox(3);
        card.getStyleClass().add("card-noticia");
        card.setPadding(new Insets(10, 12, 10, 12));

        Label cabecera = new Label(autor + "  •  " + (fecha != null ? fecha : ""));
        cabecera.getStyleClass().add("noticia-meta");

        Label cuerpo = new Label(contenido != null ? contenido : "");
        cuerpo.setWrapText(true);
        cuerpo.getStyleClass().add("noticia-desc");

        card.getChildren().addAll(cabecera, cuerpo);
        return card;
    }

    private void eliminarHiloForo() {
        if (!Configuracion.esProfesor()) return;
        HiloForo sel = tablaForo.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta("Error", "Selecciona un hilo"); return; }
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM FORO WHERE id_hilo = ?");
            stmt.setInt(1, sel.getIdHilo()); stmt.executeUpdate(); stmt.close();
            cargarHilosForo();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // =====================================================================
    //  ASISTENCIA
    // =====================================================================

    private void cargarControlAsistencia() {
        todosLosControles.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt;
            if (Configuracion.esProfesor()) {
                stmt = conn.prepareStatement(
                    "SELECT ca.id_control, u.nombre, c.nombre_curso, ca.fecha, ca.estado, ca.observaciones " +
                    "FROM CONTROL_ASISTENCIA ca JOIN USUARIO u ON ca.id_usuario = u.id_usuario " +
                    "JOIN CURSO c ON ca.id_curso = c.id_curso ORDER BY ca.fecha DESC");
            } else {
                stmt = conn.prepareStatement(
                    "SELECT ca.id_control, u.nombre, c.nombre_curso, ca.fecha, ca.estado, ca.observaciones " +
                    "FROM CONTROL_ASISTENCIA ca JOIN USUARIO u ON ca.id_usuario = u.id_usuario " +
                    "JOIN CURSO c ON ca.id_curso = c.id_curso WHERE ca.id_usuario = ? ORDER BY ca.fecha DESC");
                stmt.setInt(1, Configuracion.getIdUsuarioActual());
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                todosLosControles.add(new ControlAsistencia(
                    rs.getInt("id_control"), rs.getString("nombre"), rs.getString("nombre_curso"),
                    rs.getString("fecha"), rs.getString("estado"), rs.getString("observaciones")));
            }
            rs.close(); stmt.close();
        } catch (SQLException e) { e.printStackTrace(); }
        if (tablaControlAsistencia != null) tablaControlAsistencia.setItems(todosLosControles);
    }

    private void registrarAsistencia() {
        // Salvaguarda: solo el profesor puede registrar asistencia.
        // El alumno solo consulta sus propios registros en la tabla (read-only).
        if (!Configuracion.esProfesor()) {
            mostrarAlerta("Error", "Solo el profesor puede registrar asistencias.");
            return;
        }

        Map<String, Integer> cursosMap = obtenerCursosParaCombo();
        if (cursosMap.isEmpty()) { mostrarAlerta("Error", "No hay cursos disponibles"); return; }

        Map<String, Integer> alumnosMap = new LinkedHashMap<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement s = conn.prepareStatement(
                "SELECT id_usuario, nombre, apellido FROM USUARIO WHERE tipo_usuario = 'alumno' ORDER BY apellido, nombre");
            ResultSet r = s.executeQuery();
            while (r.next()) {
                alumnosMap.put(r.getString("apellido") + ", " + r.getString("nombre"), r.getInt("id_usuario"));
            }
            r.close(); s.close();
        } catch (SQLException e) { e.printStackTrace(); }
        if (alumnosMap.isEmpty()) { mostrarAlerta("Error", "No hay alumnos registrados"); return; }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registrar Asistencia");
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> alumnoCombo = new ComboBox<>();
        alumnoCombo.getItems().addAll(alumnosMap.keySet());
        alumnoCombo.getSelectionModel().selectFirst();
        alumnoCombo.setMaxWidth(Double.MAX_VALUE);
        grid.add(new Label("Alumno:"), 0, 0); grid.add(alumnoCombo, 1, 0);

        ComboBox<String> cursoCombo = new ComboBox<>();
        cursoCombo.getItems().addAll(cursosMap.keySet());
        cursoCombo.getSelectionModel().selectFirst();
        cursoCombo.setMaxWidth(Double.MAX_VALUE);
        TextField fechaF = new TextField(LocalDate.now().toString());
        ComboBox<String> estadoCombo = new ComboBox<>();
        estadoCombo.getItems().addAll("presente", "ausente", "justificado", "retraso");
        estadoCombo.setValue("presente");
        TextField obsF = new TextField(); obsF.setPromptText("Observaciones (opcional)");

        grid.add(new Label("Curso:"), 0, 1); grid.add(cursoCombo, 1, 1);
        grid.add(new Label("Fecha:"), 0, 2); grid.add(fechaF, 1, 2);
        grid.add(new Label("Estado:"), 0, 3); grid.add(estadoCombo, 1, 3);
        grid.add(new Label("Observaciones:"), 0, 4); grid.add(obsF, 1, 4);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        aplicarTemaDialogo(dialog);
        agregarIconoDialogo(dialog);

        Optional<ButtonType> res = dialog.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                int idAlumno = alumnosMap.get(alumnoCombo.getValue());
                int idCurso = cursosMap.get(cursoCombo.getValue());
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO CONTROL_ASISTENCIA (id_usuario, id_curso, fecha, estado, observaciones) VALUES (?, ?, ?, ?, ?)");
                stmt.setInt(1, idAlumno);
                stmt.setInt(2, idCurso);
                stmt.setString(3, fechaF.getText().trim());
                stmt.setString(4, estadoCombo.getValue());
                stmt.setString(5, obsF.getText().trim());
                stmt.executeUpdate(); stmt.close();
                cargarControlAsistencia();
                mostrarAlerta("Exito", "Asistencia registrada correctamente");
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo registrar: " + e.getMessage());
            }
        }
    }

    // =====================================================================
    //  UTILITIES
    // =====================================================================

    private void animarBoton(Button boton) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(90), boton);
        scale.setFromX(1.0); scale.setFromY(1.0);
        scale.setToX(0.94); scale.setToY(0.94);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void aplicarTemaDialogo(Dialog<?> dialog) {
        try {
            String css = Configuracion.isTemaOscuro()
                ? getClass().getResource("/estilos_oscuro.css").toExternalForm()
                : getClass().getResource("/estilos_claro.css").toExternalForm();
            if (css != null) dialog.getDialogPane().getStylesheets().add(css);
        } catch (Exception ignored) {}
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert.AlertType tipo = titulo.equalsIgnoreCase("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        aplicarTemaDialogo(alert);
        agregarIconoAlerta(alert);
        alert.showAndWait();
    }

    private void agregarIconoAlerta(Alert alert) {
        try {
            Stage s = (Stage) alert.getDialogPane().getScene().getWindow();
            s.getIcons().add(new Image(getClass().getResourceAsStream("/muudle.png")));
        } catch (Exception ignored) {}
    }

    private void agregarIconoStage(Stage stage) {
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/muudle.png")));
        } catch (Exception ignored) {}
    }

    private void agregarIconoDialogo(Dialog<?> dialog) {
        // El window/scene del Dialog se crea cuando se muestra; usamos onShowing
        // para garantizar que cuando se intenta poner el icono ya existen.
        dialog.setOnShowing(ev -> {
            try {
                Stage s = (Stage) dialog.getDialogPane().getScene().getWindow();
                if (s != null) {
                    s.getIcons().add(new Image(getClass().getResourceAsStream("/muudle.png")));
                }
            } catch (Exception ignored) {}
        });
    }
}
