package com.javafx.A_holamundofxml;

public class Configuracion {
    private static boolean temaOscuro = false;

    public static boolean isTemaOscuro() {
        return temaOscuro;
    }

    public static void setTemaOscuro(boolean temaOscuro) {
        Configuracion.temaOscuro = temaOscuro;
    }
}