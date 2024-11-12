package com.products;

/**
 * Clasa utilitară pentru afișarea alertelor în aplicație.
 * Oferă metode statice pentru afișarea diferitelor tipuri de alerte către utilizator.
 */
public class Alert {

    /**
     * Afișează o alertă către utilizator cu titlu, mesaj și tip specificate.
     * Suportă trei tipuri de alerte: INFORMATION, ERROR și CONFIRMATION.
     *
     * @param title Titlul alertei
     * @param message Mesajul alertei
     * @param type Tipul alertei ("INFORMATION", "ERROR" sau "CONFIRMATION")
     */
    public static void showAlert(String title, String message, String type) {
        if(type.equals("INFORMATION")) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } else if (type.equals("ERROR")) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
        else {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
}