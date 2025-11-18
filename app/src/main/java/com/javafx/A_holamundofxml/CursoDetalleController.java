package com.javafx.A_holamundofxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CursoDetalleController{

    @FXML
    private Button btnVolverCursos;

    @FXML
    private TableColumn<?, ?> colUsuarioApellidos;

    @FXML
    private TableColumn<?, ?> colUsuarioEmail;

    @FXML
    private TableColumn<?, ?> colUsuarioNombre;

    @FXML
    private TableColumn<?, ?> colUsuarioTipo;

    @FXML
    private Label lblInfoAlumnos;

    @FXML
    private Label lblInfoNombre;

    @FXML
    private Label lblInfoProfesor;

    @FXML
    private Label lblNombreCurso;

    @FXML
    private TableView<?> tablaUsuarios;

    @FXML
    private TextArea txtInfoDescripcion;

    @FXML
    void handleVolverCursos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cursos.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) lblInfoNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    
}
