package com.products.Product;

import com.products.Alert;
import com.products.AdminView;
import com.products.File.FileManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

/**
 * Clasa ProductView gestionează interfața utilizator pentru adăugarea și actualizarea produselor.
 */
public class ProductView {
    private List<Produs> produse;
    private AdminView adminView;
    /**
     * Constructorul clasei ProductView.
     *
     * @param produse lista de produse gestionate.
     * @param adminView instanța AdminView pentru a actualiza interfața.
     */
    public ProductView(List<Produs> produse, AdminView adminView) {
        this.produse = produse;
        this.adminView = adminView;
    }
    /**
     * Salvează un produs nou sau actualizează unul existent.
     *
     * @param produs produsul care trebuie salvat sau actualizat.
     * @param numeField câmpul pentru numele produsului.
     * @param descriereField câmpul pentru descrierea produsului.
     * @param pretField câmpul pentru prețul produsului.
     * @param cantitateField câmpul pentru cantitatea disponibilă.
     * @param dateExpirarePicker selectorul pentru data expirării.
     */
    private void saveProduct(Produs produs, TextField numeField, TextField descriereField, TextField pretField, TextField cantitateField, DatePicker dateExpirarePicker) {
        try {
            double pret = Double.parseDouble(pretField.getText());
            int cantitate = Integer.parseInt(cantitateField.getText());
            String dataCurenta = LocalDate.now().toString();

            if (produs != null) {
                updateProduct(produs, numeField, descriereField, pret, cantitate, dateExpirarePicker);
                Alert.showAlert("Informare", "Produsul a fost modificat cu succes!", "INFORMATION");
            } else {
                addNewProduct(numeField, descriereField, pret, cantitate, dateExpirarePicker, dataCurenta);
                Alert.showAlert("Informare", "Produsul a fost adăugat cu succes!", "INFORMATION");
            }
            adminView.refreshProductGrid();
            ((Stage) numeField.getScene().getWindow()).close();
        } catch (NumberFormatException ex) {
            Alert.showAlert("Eroare", "Introdu valori numerice valide pentru preț și cantitate.", "ERROR");
        }
    }

    /**
     * Actualizează informațiile unui produs existent.
     *
     * @param produs produsul de actualizat.
     * @param numeField câmpul pentru numele produsului.
     * @param descriereField câmpul pentru descrierea produsului.
     * @param pret prețul actualizat al produsului.
     * @param cantitate cantitatea disponibilă actualizată.
     * @param dateExpirarePicker selectorul pentru data expirării.
     */
    public void updateProduct(Produs produs, TextField numeField, TextField descriereField, double pret, int cantitate, DatePicker dateExpirarePicker) {
        if(!numeField.getText().matches("[a-zA-Z]+")) {
            Alert.showAlert("Informare", "Numele trebuie să fie un șir de caractere!", "INFORMATION");
            numeField.clear();
        }
        produs.setNume(numeField.getText());
        produs.setDescriere(descriereField.getText());
        produs.setPret(pret);
        produs.setCantitateDisponibila(cantitate);
        produs.setDataExpirare(dateExpirarePicker.getValue() != null ? dateExpirarePicker.getValue().toString() : "");
        adminView.refreshProductGrid();
    }

    /**
     * Adaugă un produs nou în lista de produse.
     *
     * @param numeField câmpul pentru numele produsului.
     * @param descriereField câmpul pentru descrierea produsului.
     * @param pret prețul produsului.
     * @param cantitate cantitatea disponibilă.
     * @param dateExpirarePicker selectorul pentru data expirării.
     * @param dataCurenta data curentă la care se adaugă produsul.
     */
    private void addNewProduct(TextField numeField, TextField descriereField, double pret, int cantitate, DatePicker dateExpirarePicker, String dataCurenta) {
        Produs newProdus = new Produs(pret, descriereField.getText(), cantitate, numeField.getText(), dataCurenta,
                dateExpirarePicker.getValue() != null ? dateExpirarePicker.getValue().toString() : "", 0, 0, 0);
        produse.add(newProdus);
        adminView.refreshProductGrid();
    }

    /**
     * Deschide fereastra pentru adăugarea unui produs.
     *
     * @param produs produsul de adăugat sau actualizat (poate fi null).
     */
    public void openAddProductForm(Produs produs) {
        Stage addProductStage = new Stage();
        addProductStage.setTitle("Adaugă Produs");
        GridPane formLayout = createProductForm(produs);
        Scene scene = new Scene(formLayout, 400, 400);
        addProductStage.setScene(scene);
        addProductStage.show();
    }

    /**
     * Creează formularul pentru adăugarea sau actualizarea unui produs.
     *
     * @param produs produsul existent (poate fi null).
     * @return un layout de tip GridPane cu formularul produsului.
     */
    private GridPane createProductForm(Produs produs) {
        GridPane formLayout = new GridPane();
        formLayout.setPadding(new Insets(20));
        formLayout.setVgap(15);
        formLayout.setHgap(15);
        formLayout.setStyle("""
                -fx-background-color: linear-gradient(to bottom right, #fce4ec, #f8bbd0);
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);
                """);

        String labelStyle = """
                -fx-font-family: 'Helvetica';
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: #ad1457;
                """;

        String inputStyle = """
                -fx-background-color: white;
                -fx-background-radius: 5;
                -fx-border-color: #f48fb1;
                -fx-border-radius: 5;
                -fx-border-width: 1;
                -fx-padding: 8;
                -fx-font-size: 13px;
                """;

        String buttonStyle = """
                -fx-background-color: #ec407a;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20;
                -fx-background-radius: 5;
                -fx-cursor: hand;
                """;

        String buttonHoverStyle = """
                -fx-background-color: #d81b60;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);
                """;

        TextField numeField = new TextField(produs != null ? produs.getNume() : "");
        TextField descriereField = new TextField(produs != null ? produs.getDescriere() : "");
        TextField pretField = new TextField(produs != null ? String.valueOf(produs.getPret()) : "");
        TextField cantitateField = new TextField(produs != null ? String.valueOf(produs.getCantitateDisponibila()) : "");
        DatePicker dateExpirarePicker = new DatePicker();

        Label numeLabel = new Label("Nume:");
        Label descriereLabel = new Label("Descriere:");
        Label pretLabel = new Label("Preț:");
        Label cantitateLabel = new Label("Cantitate Disponibilă:");
        Label dataLabel = new Label("Data Expirării:");

        numeLabel.setStyle(labelStyle);
        descriereLabel.setStyle(labelStyle);
        pretLabel.setStyle(labelStyle);
        cantitateLabel.setStyle(labelStyle);
        dataLabel.setStyle(labelStyle);

        numeField.setStyle(inputStyle);
        descriereField.setStyle(inputStyle);
        pretField.setStyle(inputStyle);
        cantitateField.setStyle(inputStyle);
        dateExpirarePicker.setStyle(inputStyle);

        Button saveButton = new Button("Salvează");
        saveButton.setStyle(buttonStyle);
        saveButton.setOnMouseEntered(e -> saveButton.setStyle(buttonStyle + buttonHoverStyle));
        saveButton.setOnMouseExited(e -> saveButton.setStyle(buttonStyle));

        formLayout.add(numeLabel, 0, 0);
        formLayout.add(numeField, 1, 0);
        formLayout.add(descriereLabel, 0, 1);
        formLayout.add(descriereField, 1, 1);
        formLayout.add(pretLabel, 0, 2);
        formLayout.add(pretField, 1, 2);
        formLayout.add(cantitateLabel, 0, 3);
        formLayout.add(cantitateField, 1, 3);
        formLayout.add(dataLabel, 0, 4);
        formLayout.add(dateExpirarePicker, 1, 4);
        formLayout.add(saveButton, 1, 5);

        saveButton.setOnAction(e -> saveProduct(produs, numeField, descriereField, pretField, cantitateField, dateExpirarePicker));

        return formLayout;
    }

    /**
     * Metodă pentru ștergerea unui produs și actualizarea listei fără noul produs.
     *
     * @param produs produsul existent (poate fi null).
     */
public void deleteProduct(Produs produs) {
        produse.remove(produs);
        Alert.showAlert("Informare","Produsul a fost eliminat cu succes!","INFORMATION");
        adminView.refreshProductGrid();
    }

    /**
     * Creează dialogul de confirmare pentru ștergerea produsului.
     *
     * @param produs produsul existent.
     * @param adminView intefața administratorului care se actualizează fără produsul șters.
     */
    public void confirmDeleteProduct(Produs produs, AdminView adminView) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmare Ștergere");
        alert.setHeaderText("Sunteți sigur că doriți să ștergeți produsul " + produs.getNume() + "?");
        alert.setContentText("Această acțiune nu poate fi anulată.");

        DialogPane dialogPane = alert.getDialogPane();
        String alertStyle = """
                -fx-background-color: white;
                -fx-border-color: %s;
                -fx-border-width: 2;
                """.formatted("#FF69B4");

        dialogPane.setStyle(alertStyle);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                this.deleteProduct(produs);
                adminView.refreshProductGrid();
            }
        });
    }


}
