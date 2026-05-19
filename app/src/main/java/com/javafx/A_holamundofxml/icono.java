package com.javafx.A_holamundofxml;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class icono {
    public static void setIcono(Stage stage) {
        try {
            stage.getIcons().add(new Image(icono.class.getResourceAsStream("/muudle.png")));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono");
        }
    }
}