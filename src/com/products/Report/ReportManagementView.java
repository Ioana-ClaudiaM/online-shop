package com.products.Report;

import com.products.Alert;
import com.products.ControllersManipulation;
import com.products.Order.Comanda;
import com.products.Product.Produs;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.awt.Desktop;
import java.io.IOException;
import java.util.List;

/**
 * Clasă responsabilă pentru interfața grafică de gestionare a rapoartelor.
 * Oferă funcționalități pentru generarea și vizualizarea diferitelor tipuri de rapoarte
 * într-o interfață grafică JavaFX.
 *
 */
public class ReportManagementView {
    /**
     * Obiectul pentru generarea rapoartelor.
     */
    private final Raport raport;

    /**
     * Layout-ul principal al interfeței grafice.
     */
    private final VBox mainLayout;

    ControllersManipulation controllersManipulation=new ControllersManipulation();
    private static final String REPORTS_DIR = "Rapoarte";

    /**
     * Constructorul pentru interfața de gestionare a rapoartelor.
     * Inițializează interfața grafică și setează stilizarea de bază.
     *
     * @param produse Lista de produse disponibile în sistem
     * @param comenzi Lista de comenzi efectuate
     */
    public ReportManagementView(List<Produs> produse, List<Comanda> comenzi) {
        this.raport = new Raport("Raport Vânzări", produse, comenzi);
        this.mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8bbd0, #f48fb1);");
    }

    /**
     * Deschide fereastra de gestionare a rapoartelor.
     * Creează și afișează interfața grafică cu toate opțiunile disponibile pentru rapoarte.
     */
    public void openReportManagementView() {
        Stage reportStage = new Stage();
        reportStage.setTitle("Gestionare Rapoarte");

        Label titleLabel = new Label("Gestionare Rapoarte");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox reportsContainer = new VBox(10);
        reportsContainer.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-background-radius: 5px;");

        String[] reportFiles = {
                "genereazaRaportFrecventaComenzi.txt",
                "genereazaRaportComenziFinalizate.txt",
                "genereazaRaportRatinguriProduseMatrice.txt",
                "genereazaRaportTendinteVanzari.txt",
                "genereazaRaportGeneralStoc.txt",
                "genereazaRaportProduseAproapeExpirate.txt",
                "genereazaRaportProduseAdaugateRecent.txt",
                "genereazaRaportProdusePeStoc.txt",
                "genereazaRaportTotalVanzari.txt"
        };

        String[] reportNames = {
                "Raport Frecvență Comenzi",
                "Raport Comenzi Finalizate",
                "Raport Rating Produse",
                "Raport Tendințe Vânzări",
                "Raport General Stoc",
                "Raport Produse Aproape Expirate",
                "Raport Produse Adăugate Recent",
                "Raport Produse Pe stoc",
                "Raport Total Vânzări"
        };

        for (int i = 0; i < reportFiles.length; i++) {
            final String fileName = reportFiles[i];
            final int index = i;

            HBox reportRow = new HBox(10);
            reportRow.setAlignment(Pos.CENTER_LEFT);

            Label reportName = new Label(reportNames[i]);
            reportName.setStyle("-fx-font-size: 14px;");
            reportName.setPrefWidth(250);

            Button generateButton = controllersManipulation.createReportStyledButton("Generează Raport");
            Button viewButton = controllersManipulation.createReportStyledButton("Vezi Raport");

            generateButton.setOnAction(e -> generateReport(fileName, index));
            viewButton.setOnAction(e -> viewReport(fileName));

            reportRow.getChildren().addAll(reportName, generateButton, viewButton);
            reportsContainer.getChildren().add(reportRow);
        }

        Button backButton = controllersManipulation.createReportStyledButton("Înapoi");
        backButton.setOnAction(e -> reportStage.close());

        mainLayout.getChildren().addAll(titleLabel, reportsContainer, backButton);

        Scene scene = new Scene(mainLayout, 600, 700);
        reportStage.setScene(scene);
        reportStage.show();
    }

    /**
     * Generează un raport specific în funcție de indexul și numele fișierului specificat.
     *
     * @param fileName Numele fișierului în care va fi generat raportul
     * @param reportIndex Indexul care determină tipul de raport ce va fi generat
     */
    private void generateReport(String fileName, int reportIndex) {
        try {
            switch (reportIndex) {
                case 0 -> raport.genereazaRaportFrecventaComenzi(fileName);
                case 1 -> raport.genereazaRaportComenziFinalizate(fileName);
                case 2 -> raport.genereazaRaportRatinguriProduseMatrice(fileName);
                case 3 -> raport.genereazaRaportTendinteVanzari(fileName);
                case 4 -> raport.genereazaRaportGeneralStoc(fileName);
                case 5 -> raport.genereazaRaportProduseAproapeExpirate(fileName);
                case 6 -> raport.genereazaRaportProduseAdaugateRecent(fileName);
                case 7 -> raport.genereazaRaportProdusePeStoc(fileName);
                case 8 -> raport.genereazaRaportTotalVanzari(fileName);

            }
            Alert.showAlert("Succes", "Raportul a fost generat cu succes!", "INFORMATION");
        } catch (Exception e) {
            Alert.showAlert("Eroare", "A apărut o eroare la generarea raportului: " + e.getMessage(), "ERROR");
        }
    }

    /**
     * Deschide un raport existent pentru vizualizare.
     *
     * @param fileName Numele fișierului ce trebuie deschis
     */
    private void viewReport(String fileName) {
        try {
            File file = new File(REPORTS_DIR, fileName);

            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                Alert.showAlert("Avertisment",
                        "Raportul nu există în directorul " + REPORTS_DIR + ". Generați-l mai întâi!",
                        "ERROR");
            }
        } catch (IOException e) {
            Alert.showAlert("Eroare",
                    "Nu s-a putut deschide raportul: " + e.getMessage(),
                    "ERROR");
        }
    }
}