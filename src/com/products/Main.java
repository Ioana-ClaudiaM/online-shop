package com.products;

import com.products.File.FileManager;
import com.products.File.FileOperationException;
import com.products.Order.Comanda;
import com.products.Product.AdminProductCard;
import com.products.Product.Produs;
import com.products.Product.UserProductCard;
import com.products.Report.Raport;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import java.util.List;

/**
 * Clasa principală a aplicației de gestiune a produselor de patiserie.
 * Extinde Application pentru a oferi interfața grafică principală a aplicației.
 * Gestionează inițializarea aplicației și oferă punctul de intrare pentru diferite moduri de utilizare.
 */
public class Main extends Application {

    /**
     * Lista statică de produse disponibile în aplicație.
     * Este încărcată la pornirea aplicației din sistemul de fișiere.
     */
    private static List<Produs> produse;

    /**
     * Lista statică de comenzi efectuate în aplicație.
     * Este încărcată la pornirea aplicației din sistemul de fișiere.
     */
    private static List<Comanda> comenzi;

    /**
     * Obiect pentru manipularea controalelor UI.
     * Oferă metode utilitare pentru crearea și stilizarea elementelor de interfață.
     */
    ControllersManipulation controllersManipulation = new ControllersManipulation();

    /**
     * Metoda principală de inițializare a interfeței grafice.
     * Configurează fereastra principală și elementele UI ale aplicației.
     *
     * @param primaryStage Fereastra principală a aplicației
     */
    @Override
    public void start(Stage primaryStage) {
        // Încărcare date
        produse = FileManager.getInstance().getProduse();
        comenzi = FileManager.getInstance().loadOrders();

        // Configurare container principal
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8bbd0, #f48fb1);");

        // Configurare titlu
        Label titleLabel = new Label("Sistem de Gestiune al Produselor de Patiserie");
        titleLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        // Configurare subtitlu
        Label subtitleLabel = new Label("Alegeți modul de conectare");
        subtitleLabel.setFont(Font.font("Helvetica", 16));
        subtitleLabel.setTextFill(Color.ALICEBLUE);

        // Configurare container butoane
        HBox buttonContainer = new HBox(30);
        buttonContainer.setAlignment(Pos.CENTER);

        // Definire stiluri butoane
        String buttonStyle = """
            -fx-background-color: white;
            -fx-text-fill: #f50057;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-padding: 15 30;
            -fx-background-radius: 5;
            -fx-min-width: 200px;
            -fx-min-height: 50px;
            """;

        String buttonHoverStyle = """
            -fx-background-color: #f8bbd0;
            -fx-text-fill: #d5006d;
            -fx-cursor: hand;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-padding: 15 30;
            -fx-background-radius: 5;
            -fx-min-width: 200px;
            -fx-min-height: 50px;
            """;

        // Configurare efect umbră
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));

        // Creare și configurare butoane
        Button adminButton = controllersManipulation.createStyledButton("Administrator", buttonStyle, buttonHoverStyle, shadow);
        Button userButton = controllersManipulation.createStyledButton("Utilizator", buttonStyle, buttonHoverStyle, shadow);

        // Configurare containere pentru butoane și iconițe
        VBox adminContainer = new VBox(15);
        adminContainer.setAlignment(Pos.CENTER);
        VBox userContainer = new VBox(15);
        userContainer.setAlignment(Pos.CENTER);

        // Configurare iconițe
        ImageView adminIcon = new ImageView(new Image(AdminProductCard.class.getResourceAsStream("/admin.png")));
        adminIcon.setFitHeight(70);
        adminIcon.setFitWidth(70);

        ImageView userIcon = new ImageView(new Image(UserProductCard.class.getResourceAsStream("/user.png")));
        userIcon.setFitHeight(70);
        userIcon.setFitWidth(70);

        // Asamblare containere
        adminContainer.getChildren().addAll(adminIcon, adminButton);
        userContainer.getChildren().addAll(userIcon, userButton);

        // Configurare acțiuni butoane
        AdminView adminView = new AdminView(primaryStage);
        adminView.setProduse(produse);
        adminButton.setOnAction(e -> adminView.openAdminMenu());

        UserView userView = new UserView(primaryStage);
        userView.setProduse(produse);
        userButton.setOnAction(e -> userView.openUserMenu());

        // Asamblare layout final
        buttonContainer.getChildren().addAll(adminContainer, userContainer);
        mainContainer.getChildren().addAll(titleLabel, subtitleLabel, buttonContainer);

        // Configurare scenă și fereastră
        Scene scene = new Scene(mainContainer, 800, 600);
        primaryStage.setTitle("Sistem de Gestiune al Produselor de Patiserie");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        // Configurare handler de închidere
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            try {
                FileManager.getInstance().saveToFile(produse);
                FileManager.getInstance().saveAllOrders(comenzi);

                Raport raport = new Raport("Raport Vânzări", produse, comenzi);

                raport.genereazaRaportFrecventaComenzi("genereazaRaportFrecventaComenzi.txt");
                raport.genereazaRaportComenziFinalizate("genereazaRaportComenziFinalizate.txt");
                raport.genereazaRaportRatinguriProduseMatrice("genereazaRaportRatinguriProduseMatrice.txt");
                raport.genereazaRaportTendinteVanzari("genereazaRaportTendinteVanzari.txt");
                raport.genereazaRaportGeneralStoc("genereazaRaportGeneralStoc.txt");
                raport.genereazaRaportProduseAproapeExpirate("genereazaRaportProduseAproapeExpirate.txt");
                raport.genereazaRaportProduseAdaugateRecent("genereazaRaportProduseAdaugateRecent.txt");
                raport.genereazaRaportProdusePeStoc("genereazaRaportProdusePeStoc.txt");
                raport.genereazaRaportTotalVanzari("genereazaRaportTotalVanzari.txt");

                primaryStage.close();
            } catch (FileOperationException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Modificări salvate cu succes și rapoarte generate în directorul 'Rapoarte'!");
        });

        primaryStage.show();
    }

    /**
     * Returnează lista curentă de produse.
     *
     * @return Lista de produse din aplicație
     */
    public static List<Produs> getProduse() {
        return produse;
    }

    /**
     * Returnează lista curentă de comenzi.
     *
     * @return Lista de comenzi din aplicație
     */
    public static List<Comanda> getOrders() {
        return comenzi;
    }

    /**
     * Punctul de intrare principal al aplicației.
     *
     * @param args Argumentele liniei de comandă (neutilizate)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
