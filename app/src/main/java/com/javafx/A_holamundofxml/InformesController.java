package com.javafx.A_holamundofxml;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class InformesController {

    @FXML private ComboBox<String> comboTipoInforme;
    @FXML private TextField txtParametro;
    @FXML private Label lblParametro;
    @FXML private Button btnGenerar;
    @FXML private WebView webViewInforme;
    @FXML private VBox vboxVisor;
    @FXML private VBox vboxParametro;

    private static final String CARPETA_INFORMES = "INFORMES";

    @FXML
    public void initialize() {
        comboTipoInforme.setItems(FXCollections.observableArrayList(
            "Informe de Cursos (Simple con Gráfica)",
            "Informe de Matriculaciones (SQL Compuesta)",
            "Informe Condicional por Nota Mínima"
        ));
        
        comboTipoInforme.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            actualizarParametros(newVal);
        });
        
        comboTipoInforme.getSelectionModel().selectFirst();
        vboxVisor.setVisible(false);
        vboxVisor.setManaged(false);
        
        crearCarpetaInformes();
    }
    
    private void crearCarpetaInformes() {
        File carpeta = new File(CARPETA_INFORMES);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }
    
    private void actualizarParametros(String tipoInforme) {
        if (tipoInforme == null) return;
        
        switch (tipoInforme) {
            case "Informe de Cursos (Simple con Gráfica)":
                vboxParametro.setVisible(false);
                vboxParametro.setManaged(false);
                break;
            case "Informe de Matriculaciones (SQL Compuesta)":
                vboxParametro.setVisible(true);
                vboxParametro.setManaged(true);
                lblParametro.setText("ID Curso (vacío = todos):");
                txtParametro.setPromptText("Ej: 1");
                txtParametro.clear();
                break;
            case "Informe Condicional por Nota Mínima":
                vboxParametro.setVisible(true);
                vboxParametro.setManaged(true);
                lblParametro.setText("Nota mínima:");
                txtParametro.setPromptText("Ej: 5.0");
                txtParametro.clear();
                break;
        }
    }

    @FXML
    private void handleGenerarInforme() {
        String tipoInforme = comboTipoInforme.getValue();
        
        if (tipoInforme == null) {
            mostrarError("Debe seleccionar un tipo de informe");
            return;
        }
        
        try {
            switch (tipoInforme) {
                case "Informe de Cursos (Simple con Gráfica)":
                    generarInformeCursos();
                    break;
                case "Informe de Matriculaciones (SQL Compuesta)":
                    generarInformeMatriculas();
                    break;
                case "Informe Condicional por Nota Mínima":
                    generarInformeCondicional();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al generar el informe: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleCerrar() {
        Stage stage = (Stage) btnGenerar.getScene().getWindow();
        stage.close();
    }
    
    private void generarInformeCursos() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            InputStream inputStream = getClass().getResourceAsStream("/informes/InformeCursos.jrxml");
            if (inputStream == null) {
                throw new Exception("No se encontró el archivo InformeCursos.jrxml");
            }
            
            JasperReport report = JasperCompileManager.compileReport(inputStream);
            Map<String, Object> parametros = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(report, parametros, conn);
            
            String nombreBase = CARPETA_INFORMES + "/InformeCursos";
            guardarInformeEnArchivos(print, nombreBase);
            
            mostrarInformeIncrustado(nombreBase + ".html");
        }
    }
    
    private void generarInformeMatriculas() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            InputStream inputStream = getClass().getResourceAsStream("/informes/InformeMatriculas.jrxml");
            if (inputStream == null) {
                throw new Exception("No se encontró el archivo InformeMatriculas.jrxml");
            }
            
            JasperReport report = JasperCompileManager.compileReport(inputStream);
            Map<String, Object> parametros = new HashMap<>();
            
            String paramTexto = txtParametro.getText().trim();
            if (!paramTexto.isEmpty()) {
                try {
                    parametros.put("ID_CURSO", Integer.parseInt(paramTexto));
                } catch (NumberFormatException e) {
                    mostrarError("El ID del curso debe ser un número entero");
                    return;
                }
            } else {
                parametros.put("ID_CURSO", null);
            }
            
            JasperPrint print = JasperFillManager.fillReport(report, parametros, conn);
            
            String nombreBase = CARPETA_INFORMES + "/InformeMatriculas";
            guardarInformeEnArchivos(print, nombreBase);
            
            mostrarInformeModal("Informe de Matriculaciones", nombreBase + ".html");
        }
    }
    
    private void generarInformeCondicional() throws Exception {
        String paramTexto = txtParametro.getText().trim();
        if (paramTexto.isEmpty()) {
            mostrarError("Debe introducir una nota mínima");
            return;
        }
        
        double notaMinima;
        try {
            notaMinima = Double.parseDouble(paramTexto.replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarError("La nota mínima debe ser un número válido (ej: 5.0)");
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            InputStream inputStream = getClass().getResourceAsStream("/informes/InformeCondicional.jrxml");
            if (inputStream == null) {
                throw new Exception("No se encontró el archivo InformeCondicional.jrxml");
            }
            
            JasperReport report = JasperCompileManager.compileReport(inputStream);
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("NOTA_MINIMA", notaMinima);
            
            JasperPrint print = JasperFillManager.fillReport(report, parametros, conn);
            
            String nombreBase = CARPETA_INFORMES + "/InformeCondicional";
            guardarInformeEnArchivos(print, nombreBase);
            
            mostrarInformeModal("Informe Condicional (Nota >= " + notaMinima + ")", nombreBase + ".html");
        }
    }
    
    private void guardarInformeEnArchivos(JasperPrint print, String nombreBase) throws Exception {
        JasperExportManager.exportReportToPdfFile(print, nombreBase + ".pdf");
        
        HtmlExporter htmlExporter = new HtmlExporter();
        htmlExporter.setExporterInput(new SimpleExporterInput(print));
        htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(nombreBase + ".html"));
        htmlExporter.exportReport();
        
        System.out.println("Informe guardado en: " + nombreBase + ".pdf");
        System.out.println("Informe guardado en: " + nombreBase + ".html");
    }
    
    private void mostrarInformeIncrustado(String rutaHtml) {
        try {
            File htmlFile = new File(rutaHtml);
            if (htmlFile.exists()) {
                vboxVisor.setVisible(true);
                vboxVisor.setManaged(true);
                webViewInforme.getEngine().load(htmlFile.toURI().toString());
            } else {
                mostrarError("No se pudo generar el archivo HTML del informe");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al mostrar el informe: " + e.getMessage());
        }
    }
    
    private void mostrarInformeModal(String titulo, String rutaHtml) {
        try {
            File htmlFile = new File(rutaHtml);
            if (!htmlFile.exists()) {
                mostrarError("No se pudo generar el archivo HTML del informe");
                return;
            }
            
            Stage modalStage = new Stage();
            modalStage.setTitle(titulo);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(btnGenerar.getScene().getWindow());
            
            try {
                modalStage.getIcons().add(new Image(getClass().getResourceAsStream("/muudle.png")));
            } catch (Exception e) {
            }
            
            VBox vboxPrincipal = new VBox(15);
            vboxPrincipal.setPadding(new Insets(20));
            
            Label lblTitulo = new Label(titulo);
            lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            
            Separator sep1 = new Separator();
            
            WebView webView = new WebView();
            webView.getEngine().load(htmlFile.toURI().toString());
            VBox.setVgrow(webView, Priority.ALWAYS);
            
            Separator sep2 = new Separator();
            
            Button btnCerrarModal = new Button("Cerrar");
            btnCerrarModal.setMnemonicParsing(true);
            btnCerrarModal.setText("C_errar");
            btnCerrarModal.setTooltip(new Tooltip("Cerrar ventana del informe"));
            btnCerrarModal.setOnAction(e -> modalStage.close());
            
            HBox hboxBotones = new HBox();
            hboxBotones.setAlignment(Pos.BOTTOM_RIGHT);
            hboxBotones.getChildren().add(btnCerrarModal);
            
            vboxPrincipal.getChildren().addAll(lblTitulo, sep1, webView, sep2, hboxBotones);
            
            Scene scene = new Scene(vboxPrincipal);
            modalStage.setScene(scene);
            modalStage.setWidth(850);
            modalStage.setHeight(650);
            
            modalStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al mostrar el informe: " + e.getMessage());
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