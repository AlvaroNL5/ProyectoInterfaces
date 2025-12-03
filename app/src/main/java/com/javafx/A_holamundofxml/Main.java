package com.javafx.A_holamundofxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

    /**
     *
     * @author Alvaro
     */
    public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primeraEscena) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        Scene scene = new Scene(root);
        primeraEscena.setScene(scene);
        primeraEscena.setTitle("Login");
        agregarIcono(primeraEscena);
        primeraEscena.show();
    }
    
    private void agregarIcono(Stage stage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/muudle.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }
    }
}