package com.javafx.A_holamundofxml;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class InformesController {

    @FXML private ComboBox<String> comboTipoInforme;
    @FXML private TextField txtParametro;
    @FXML private Label lblParametro;
    @FXML private Button btnGenerarIncrustado;
    @FXML private Button btnGenerarVentana;
    @FXML private VBox vboxVisor;
    @FXML private WebView webViewInforme;
    
    private JasperPrint jasperPrintActual;
    
    @FXML
    public void initialize() {
        comboTipoInforme.setItems(FXCollections.observableArrayList(
            "Informe de Cursos (Simple con Grafica)",
            "Informe de Matriculaciones (SQL Compuesta)",
            "Informe Condicional por Nota Minima"
        ));
        comboTipoInforme.setValue("Informe de Cursos (Simple con Grafica)");
        
        comboTipoInforme.setOnAction(event -> actualizarParametros());
        actualizarParametros();
        
        comboTipoInforme.setTooltip(new Tooltip("Seleccione el tipo de informe a generar"));
        txtParametro.setTooltip(new Tooltip("Parametro para filtrar el informe"));
        btnGenerarIncrustado.setTooltip(new Tooltip("Ver el informe en esta ventana (incrustado)"));
        btnGenerarVentana.setTooltip(new Tooltip("Abrir el informe en el navegador (no incrustado)"));
    }
    
    private void actualizarParametros() {
        String seleccion = comboTipoInforme.getValue();
        
        if (seleccion.contains("Condicional")) {
            lblParametro.setText("Nota minima:");
            txtParametro.setPromptText("Ej: 5.0");
            txtParametro.setVisible(true);
            lblParametro.setVisible(true);
        } else if (seleccion.contains("Matriculaciones")) {
            lblParametro.setText("ID Curso (vacio = todos):");
            txtParametro.setPromptText("Ej: 1");
            txtParametro.setVisible(true);
            lblParametro.setVisible(true);
        } else {
            lblParametro.setVisible(false);
            txtParametro.setVisible(false);
        }
    }
    
    @FXML
    void handleGenerarIncrustado(ActionEvent event) {
        generarInforme(true);
    }
    
    @FXML
    void handleGenerarVentana(ActionEvent event) {
        generarInforme(false);
    }
    
    @FXML
    void handleCerrar(ActionEvent event) {
        ((Stage) comboTipoInforme.getScene().getWindow()).close();
    }
    
    private InputStream cargarRecurso(String nombreArchivo) {
        InputStream is = null;
        
        String[] rutas = {
            "/informes/" + nombreArchivo,
            "informes/" + nombreArchivo,
            "/" + nombreArchivo,
            nombreArchivo
        };
        
        for (String ruta : rutas) {
            is = getClass().getResourceAsStream(ruta);
            if (is != null) {
                System.out.println("Recurso encontrado en: " + ruta);
                return is;
            }
            
            is = getClass().getClassLoader().getResourceAsStream(ruta.startsWith("/") ? ruta.substring(1) : ruta);
            if (is != null) {
                System.out.println("Recurso encontrado con ClassLoader en: " + ruta);
                return is;
            }
        }
        
        return null;
    }
    
    private void generarInforme(boolean incrustado) {
        String seleccion = comboTipoInforme.getValue();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            Map<String, Object> parametros = new HashMap<>();
            String nombreInforme;
            
            if (seleccion.contains("Cursos")) {
                nombreInforme = "InformeCursos.jrxml";
            } else if (seleccion.contains("Matriculaciones")) {
                nombreInforme = "InformeMatriculas.jrxml";
                String paramCurso = txtParametro.getText().trim();
                if (!paramCurso.isEmpty()) {
                    try {
                        parametros.put("ID_CURSO", Integer.parseInt(paramCurso));
                    } catch (NumberFormatException e) {
                        mostrarError("El ID del curso debe ser un numero entero");
                        return;
                    }
                } else {
                    parametros.put("ID_CURSO", null);
                }
            } else {
                nombreInforme = "InformeCondicional.jrxml";
                String paramNota = txtParametro.getText().trim();
                if (paramNota.isEmpty()) {
                    paramNota = "0";
                }
                try {
                    parametros.put("NOTA_MINIMA", Double.parseDouble(paramNota));
                } catch (NumberFormatException e) {
                    mostrarError("La nota debe ser un numero (use punto para decimales)");
                    return;
                }
            }
            
            InputStream inputStream = cargarRecurso(nombreInforme);
            
            if (inputStream == null) {
                mostrarError("No se encuentra el archivo: " + nombreInforme + 
                            "\n\nAsegurese de que los archivos .jrxml estan en:" +
                            "\nsrc/main/resources/informes/" +
                            "\n\nY ejecute: ./gradlew clean build");
                return;
            }
            
            // Compilar el .jrxml en tiempo de ejecucion
            System.out.println("Compilando informe: " + nombreInforme);
            JasperReport report = JasperCompileManager.compileReport(inputStream);
            System.out.println("Informe compilado correctamente");
            
            // Rellenar el informe con los datos de la BD
            jasperPrintActual = JasperFillManager.fillReport(report, parametros, conn);
            System.out.println("Informe rellenado con datos");
            
            if (incrustado) {
                mostrarInformeIncrustado();
            } else {
                mostrarInformeVentana();
            }
            
        } catch (JRException e) {
            e.printStackTrace();
            mostrarError("Error de JasperReports: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al generar informe: " + e.getMessage());
        }
    }
    
    private void mostrarInformeIncrustado() {
        try {
            String tempHtml = System.getProperty("java.io.tmpdir") + File.separator + "informe_temp.html";
            
            HtmlExporter exporter = new HtmlExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrintActual));
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(tempHtml));
            exporter.exportReport();
            
            if (webViewInforme != null) {
                webViewInforme.getEngine().load(new File(tempHtml).toURI().toString());
                vboxVisor.setVisible(true);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al mostrar informe incrustado: " + e.getMessage());
        }
    }
    
    private void mostrarInformeVentana() {
        try {
            String tempHtml = System.getProperty("java.io.tmpdir") + File.separator + "informe_muudle.html";
            
            HtmlExporter exporter = new HtmlExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrintActual));
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(tempHtml));
            exporter.exportReport();
            
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new File(tempHtml).toURI());
            } else {
                mostrarError("No se puede abrir el navegador en este sistema");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al abrir informe en navegador: " + e.getMessage());
        }
    }
    
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        agregarIconoAlerta(alert);
        alert.showAndWait();
    }
    
    private void agregarIconoAlerta(Alert alert) {
        try {
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/muudle.png")));
        } catch (Exception e) {
        }
    }
}