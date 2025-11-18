package com.javafx.A_holamundofxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InsertarController {

    @FXML
    private Button btnCrear;

    @FXML
    private TextField txtBuscarAlumno;

    @FXML
    private TextField txtNombreCurso;

    @FXML
    private TextField txtNombreCurso1;

    @FXML
    private TextField txtRutaImagen;

    @FXML
    void handleCancelar(ActionEvent event) {
        ((Stage)txtNombreCurso.getScene().getWindow()).close();
    }

    @FXML
    void handleCrear(ActionEvent event) {

    }

}
