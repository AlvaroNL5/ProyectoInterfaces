package com.javafx.A_holamundofxml;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CrearCursoController {
    
    @FXML
    private TextField txtNombreCurso;
    
    @FXML
    private TextArea txtDescripcion;
    
    @FXML
    private TextField txtRutaImagen;
    
    @FXML
    private TextField txtBuscarAlumno;
    
    @FXML
    private ListView<?> listaAlumnos;
    
    @FXML
    private Button btnCrear;
    
    @FXML
    public void handleSeleccionarImagen() {
    }
    
    @FXML
    public void handleBuscarAlumno() {
    }
    
    @FXML
    public void handleCrear() {
    }
    
    @FXML
    public void handleCancelar() {
        ((Stage)txtNombreCurso.getScene().getWindow()).close();
    }
}