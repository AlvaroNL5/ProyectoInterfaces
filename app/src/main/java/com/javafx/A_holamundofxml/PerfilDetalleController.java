package com.javafx.A_holamundofxml;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PerfilDetalleController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField txtEdad;
    @FXML private Button btnGuardar;
    
    private CursosController cursosController;
    private CursosController.Usuario usuario;
    
    public void setCursosController(CursosController controller) {
        this.cursosController = controller;
    }
    
    @FXML
    public void initialize() {
        ObservableList<String> tipos = FXCollections.observableArrayList("profesor", "alumno");
        comboTipo.setItems(tipos);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), txtNombre.getParent());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    
    public void cargarDatosUsuario(CursosController.Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            txtNombre.setText(usuario.getNombre());
            txtApellidos.setText(usuario.getApellidos());
            txtEmail.setText(usuario.getEmail());
            comboTipo.setValue(usuario.getTipo());
            txtEdad.setText(String.valueOf(usuario.getEdad()));
        } else {
            txtNombre.setText("No disponible");
            txtApellidos.setText("No disponible");
            txtEmail.setText("No disponible");
            comboTipo.setValue("alumno");
            txtEdad.setText("0");
        }
        
        txtNombre.setEditable(false);
        txtApellidos.setEditable(false);
        txtEmail.setEditable(false);
        comboTipo.setDisable(true);
        txtEdad.setEditable(false);
        
        if (btnGuardar != null) {
            btnGuardar.setVisible(false);
            btnGuardar.setManaged(false);
        }
    }
    
    @FXML
    void handleCerrar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), txtNombre.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> stage.close());
        fadeOut.play();
    }
    
    @FXML
    void handleGuardarCambios(ActionEvent event) {
        mostrarError("No está permitido editar perfiles de otros usuarios");
    }
    
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
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