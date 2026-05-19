package com.javafx.A_holamundofxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        Scene scene = new Scene(root);
        
        aplicarTema(scene);
        
        primaryStage.setTitle("Login - Muudle");
        primaryStage.setScene(scene);
        
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/muudle.png")));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono");
        }
        
        primaryStage.show();
    }
    
    public static void aplicarTema(Scene scene) {
        if (scene == null) return;
        
        scene.getStylesheets().clear();
        
        try {
            String rutaCss;
            if (Configuracion.isTemaOscuro()) {
                rutaCss = Main.class.getResource("/estilos_oscuro.css").toExternalForm();
            } else {
                rutaCss = Main.class.getResource("/estilos_claro.css").toExternalForm();
            }
            
            if (rutaCss != null) {
                scene.getStylesheets().add(rutaCss);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar CSS: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}