package com.javafx.A_holamundofxml;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class icono {
    
    public static void setStageIcon(Stage stage) {
        try {
            Image icon = new Image(icono.class.getResourceAsStream("/muudle.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }
    }
}