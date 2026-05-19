package com.javafx.A_holamundofxml;

public class Configuracion {
    private static boolean temaOscuro = false;
    private static int idUsuarioActual = 0;
    private static String nombreUsuarioActual = "";
    private static String tipoUsuarioActual = "alumno";
    
    public static boolean isTemaOscuro() {
        return temaOscuro;
    }
    
    public static void setTemaOscuro(boolean oscuro) {
        temaOscuro = oscuro;
    }
    
    public static int getIdUsuarioActual() {
        return idUsuarioActual;
    }
    
    public static void setIdUsuarioActual(int id) {
        idUsuarioActual = id;
    }
    
    public static String getNombreUsuarioActual() {
        return nombreUsuarioActual;
    }
    
    public static void setNombreUsuarioActual(String nombre) {
        nombreUsuarioActual = nombre != null ? nombre : "";
    }
    
    public static String getTipoUsuarioActual() {
        return tipoUsuarioActual != null ? tipoUsuarioActual : "alumno";
    }
    
    public static void setTipoUsuarioActual(String tipo) {
        if (tipo != null && !tipo.isEmpty()) {
            tipoUsuarioActual = tipo;
        } else {
            tipoUsuarioActual = "alumno";
        }
    }
    
    public static boolean esProfesor() {
        return "profesor".equalsIgnoreCase(tipoUsuarioActual);
    }
    
    public static void resetear() {
        temaOscuro = false;
        idUsuarioActual = 0;
        nombreUsuarioActual = "";
        tipoUsuarioActual = "alumno";
    }
}