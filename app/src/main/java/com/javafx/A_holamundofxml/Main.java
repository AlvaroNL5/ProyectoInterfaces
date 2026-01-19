package com.javafx.A_holamundofxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primeraEscena) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        aplicarTema(scene);
        
        primeraEscena.setScene(scene);
        primeraEscena.setTitle("Login");
        agregarIcono(primeraEscena);
        primeraEscena.show();
    }
    
    public static void aplicarTema(Scene scene) {
        scene.getStylesheets().clear();
        String css;
        if (Configuracion.isTemaOscuro()) {
            css = Main.class.getResource("/estilos_oscuro.css").toExternalForm();
        } else {
            css = Main.class.getResource("/estilos_claro.css").toExternalForm();
        }
        scene.getStylesheets().add(css);
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