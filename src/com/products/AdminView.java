package com.products;
import com.products.Cart.Cart;
import com.products.File.FileManager;
import com.products.Order.OrderView;
import com.products.Product.AdminProductCard;
import com.products.Product.ProductView;
import com.products.Product.Produs;
import com.products.Report.ReportManagementView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
/**
 * Clasa AdminView gestionează interfața grafică pentru panoul de administrare al aplicației de produse.
 * Această clasă oferă funcționalități pentru managementul produselor, comenzilor și rapoartelor,
 * folosind JavaFX pentru interfața grafică.
 */

public class AdminView {

    /**
     * Manager pentru operațiile cu fișiere, implementat ca Singleton.
     * Gestionează persistența datelor aplicației.
     */
    private final FileManager fileManager = FileManager.getInstance();

    /**
     * Lista de produse gestionată în aplicație.
     * Este actualizată din FileManager și menținută sincronizată cu starea aplicației.
     */
    private List<Produs> produse = fileManager.getProduse();

    /**
     * GridPane utilizat pentru afișarea produselor într-o grilă.
     * Permite organizarea produselor într-un format vizual ușor de navigat.
     */
    private GridPane productGrid = new GridPane();

    /**
     * Fereastra principală a aplicației.
     * Reprezintă punctul de intrare pentru interfața grafică.
     */
    Stage primaryStage;

    /**
     * Obiect pentru manipularea controalelor UI.
     * Oferă metode utilitare pentru crearea și stilizarea elementelor de interfață.
     */
    ControllersManipulation controllersManipulation = new ControllersManipulation();

    /**
     * View pentru gestionarea produselor.
     * Oferă interfața pentru adăugarea și modificarea produselor.
     */
    ProductView productView;

    /**
     * View pentru gestionarea comenzilor.
     * Permite vizualizarea și gestionarea comenzilor primite.
     */
    OrderView orderView;

    /**
     * View pentru gestionarea rapoartelor.
     * Oferă funcționalități pentru generarea și vizualizarea rapoartelor.
     */
    private ReportManagementView reportManagementView;

    /**
     * Constructor pentru clasa AdminView.
     * Inițializează toate componentele necesare pentru interfața de administrare.
     *
     * @param primaryStage Fereastra principală a aplicației
     */
    public AdminView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.productView = new ProductView(produse, this);
        this.orderView = new OrderView(new Cart());
        this.reportManagementView = new ReportManagementView(produse, FileManager.loadOrders());
    }

    /**
     * Actualizează lista de produse.
     * Folosită când se modifică datele din exterior pentru a menține sincronizarea.
     *
     * @param produse Noua listă de produse care va înlocui lista existentă
     */
    public void setProduse(List<Produs> produse) {
        this.produse = produse;
    }

    /**
     * Deschide meniul principal de administrator.
     * Creează și afișează o fereastră cu butoane pentru diferite funcționalități administrative.
     * Include stilizare avansată și efecte vizuale pentru o experiență de utilizare plăcută.
     */
    public void openAdminMenu() {
        Stage adminStage = new Stage();
        adminStage.setTitle("Meniu Administrator");
        adminStage.setMinWidth(800);
        adminStage.setMinHeight(600);

        // Configurare titlu
        Label titleLabel = new Label("Alegeți o opțiune din meniul de mai jos:");
        titleLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        // Configurare layout principal
        VBox adminMenuLayout = new VBox(20);
        adminMenuLayout.setAlignment(Pos.CENTER);
        adminMenuLayout.setPadding(new Insets(40));
        adminMenuLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8bbd0, #f48fb1);");

        // Definire stiluri pentru butoane
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

        // Creare efect umbră
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));

        // Creare butoane cu stil
        Button addProductButton = controllersManipulation.createStyledButton(
                "Adaugă Produs", buttonStyle, buttonHoverStyle, shadow);
        Button viewProductsButton = controllersManipulation.createStyledButton(
                "Vizualizează Produse", buttonStyle, buttonHoverStyle, shadow);
        Button viewOrdersButton = controllersManipulation.createStyledButton(
                "Vezi Comenzi", buttonStyle, buttonHoverStyle, shadow);
        Button exitApplication = controllersManipulation.createStyledButton(
                "Închide aplicația", buttonStyle, buttonHoverStyle, shadow);
        Button reportButton = controllersManipulation.createStyledButton(
                "Gestionare Rapoarte", buttonStyle, buttonHoverStyle, shadow);

        // Configurare acțiuni butoane
        reportButton.setOnAction(e -> reportManagementView.openReportManagementView());
        addProductButton.setOnAction(e -> productView.openAddProductForm(null));
        viewProductsButton.setOnAction(e -> openAdminProductView());
        viewOrdersButton.setOnAction(e -> orderView.openOrdersView());
        exitApplication.setOnAction(e -> closeApplication());

        // Asamblare layout
        adminMenuLayout.getChildren().addAll(
                titleLabel,
                addProductButton,
                viewProductsButton,
                viewOrdersButton,
                reportButton,
                exitApplication
        );
        adminMenuLayout.setPadding(new Insets(20));

        // Configurare și afișare scenă
        Scene adminMenuScene = new Scene(adminMenuLayout, 300, 400);
        adminStage.setScene(adminMenuScene);
        adminStage.show();
    }

    /**
     * Deschide vizualizarea produselor pentru administrator.
     * Creează o fereastră nouă care afișează toate produsele într-un grid scrollabil.
     * Include funcționalități pentru navigare și administrare produse.
     */
    private void openAdminProductView() {
        // Actualizare listă produse
        produse = fileManager.getProduse();

        // Configurare fereastră
        Stage productStage = new Stage();
        productStage.setTitle("Produse Administrator");
        productStage.setMinHeight(600);
        productStage.setMinWidth(800);

        // Configurare GridPane
        productGrid = new GridPane();
        productGrid.setHgap(10);
        productGrid.setVgap(10);
        productGrid.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8bbd0, #f48fb1);");

        // Configurare coloane responsive
        for (int i = 0; i < 3; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(33.33);
            productGrid.getColumnConstraints().add(columnConstraints);
        }

        // Populare grid cu produse
        refreshProductGrid();

        // Configurare ScrollPane
        ScrollPane scrollPane = new ScrollPane(productGrid);
        scrollPane.setFitToWidth(true);

        // Configurare buton înapoi
        Button backButton = new Button("Înapoi către meniu");
        backButton.setStyle("-fx-background-color:pink;-fx-padding: 10; -fx-font-weight: bold;");
        backButton.setOnMouseEntered(e ->
                backButton.setStyle("-fx-background-color:white;-fx-padding: 10; -fx-font-weight: bold;"));
        backButton.setOnMouseExited(e ->
                backButton.setStyle("-fx-background-color:pink;-fx-padding: 10; -fx-font-weight: bold;"));
        backButton.setOnAction(e -> {
            productStage.close();
            openAdminMenu();
        });

        // Configurare layout principal
        VBox mainLayout = new VBox(10, scrollPane, backButton);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setAlignment(Pos.CENTER);

        // Configurare și afișare scenă
        Scene productScene = new Scene(mainLayout, 800, 600);
        productStage.setScene(productScene);
        productStage.show();
    }

    /**
     * Reîmprospătează grila de produse.
     * Actualizează afișarea produselor în interfață după modificări.
     * Reorganizează cardurile produselor într-un grid de 3 coloane făcând interfața responsive.
     */
    public void refreshProductGrid() {
        System.out.println("Refresh!");
        productGrid.getChildren().clear();

        for (int i = 0; i < produse.size(); i++) {
            Produs produs = produse.get(i);
            Pane productCard = AdminProductCard.createAdminProductCard(produs, this, productView);
            productGrid.add(productCard, i % 3, i / 3);
        }
    }

    /**
     * Închide aplicația.
     * Oprește toate procesele active și eliberează resursele.
     */
    private void closeApplication() {
        System.out.println("Buton apasat!");
        if (primaryStage != null) {
            primaryStage.close();
        }
        Platform.exit();
        System.exit(0);
    }
}