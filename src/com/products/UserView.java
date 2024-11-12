package com.products;
import com.products.Cart.Cart;
import com.products.Cart.CartView;
import com.products.Order.Comanda;
import com.products.Order.OrderView;
import com.products.Product.Produs;
import com.products.Product.UserProductCard;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.util.List;

/**
 * Reprezintă interfața grafică pentru utilizatorii obișnuiți ai sistemului de gestiune a produselor.
 * Această clasă gestionează toate operațiunile disponibile utilizatorilor, inclusiv vizualizarea
 * produselor și gestionarea coșului de cumpărături.
 */
public class UserView {
    /**
     * Lista produselor disponibile pentru vizualizare și cumpărare.
     */
    private List<Produs> produse;

    /**
     * Coșul de cumpărături asociat sesiunii curente a utilizatorului.
     */
    private Cart cart = new Cart();

    /**
     * Stage-ul principal al aplicației JavaFX.
     */
    Stage primaryStage;

    /**
     * Gestionar pentru vizualizarea comenzilor.
     */
    OrderView orderView;

    /**
     * Clasă utilitară pentru manipularea controlerelor.
     */
    ControllersManipulation controllersManipulation = new ControllersManipulation();

    /**
     * Gestionar pentru vizualizarea coșului de cumpărături.
     */
    CartView cartView;

    /**
     * Construiește o nouă instanță UserView cu un stage principal specificat.
     * Inițializează componentele necesare pentru gestionarea comenzilor și coșului.
     *
     * @param primaryStage Stage-ul principal al aplicației JavaFX
     */
    public UserView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.orderView = new OrderView(cart);
        this.cartView = new CartView(cart, orderView);
    }

    /**
     * Constructor implicit care inițializează un nou coș și încarcă comenzile existente.
     */
    public UserView() {
        this.cart = new Cart();
    }

    /**
     * Setează lista de produse disponibile utilizatorului.
     *
     * @param produse Lista de produse ce va fi afișată
     */
    public void setProduse(List<Produs> produse) {
        this.produse = produse;
    }

    /**
     * Deschide interfața meniului principal pentru utilizator.
     * Afișează opțiuni pentru vizualizarea produselor, coșului și ieșirea din aplicație.
     * Creează un layout cu butoane stilizate și efecte vizuale.
     */
    public void openUserMenu() {
        Stage userStage = new Stage();
        userStage.setTitle("Meniu Utilizator");
        userStage.setMinWidth(800);
        userStage.setMinHeight(600);

        Label titleLabel = new Label("Alegeți o opțiune din meniul de mai jos:");
        titleLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        VBox userMenuLayout = new VBox(20);
        userMenuLayout.setAlignment(Pos.CENTER);
        userMenuLayout.setPadding(new Insets(40));
        userMenuLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8bbd0, #f48fb1);");

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

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));

        Button viewProductsButton = controllersManipulation.createStyledButton("Vizualizează produse", buttonStyle, buttonHoverStyle, shadow);
        Button viewCartButton = controllersManipulation.createStyledButton("Vizualizează coșul", buttonStyle, buttonHoverStyle, shadow);
        Button exitApplication = controllersManipulation.createStyledButton("Închide aplicația", buttonStyle, buttonHoverStyle, shadow);

        viewProductsButton.setOnAction(e -> {
            userStage.close();
            openUserProductView();
        });
        viewCartButton.setOnAction(e -> {
            userStage.close();
            cartView.openCartView();
        });

        exitApplication.setOnAction(e -> closeApplication());

        userMenuLayout.getChildren().addAll(titleLabel, viewProductsButton, viewCartButton, exitApplication);
        userMenuLayout.setPadding(new Insets(20));

        Scene userMenuScene = new Scene(userMenuLayout, 300, 200);
        userStage.setScene(userMenuScene);
        userStage.show();
    }

    /**
     * Deschide interfața de vizualizare a produselor pentru utilizatori.
     * Afișează produsele într-un layout grid cu opțiuni de adăugare în coș.
     * Include funcționalități de navigare și vizualizare a coșului.
     */
    private void openUserProductView() {
        Stage productStage = new Stage();
        productStage.setTitle("Produse Utilizator");
        productStage.setMinHeight(600);
        productStage.setMinWidth(800);

        GridPane productGrid = new GridPane();
        productGrid.setHgap(10);
        productGrid.setVgap(10);
        productGrid.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8bbd0, #f48fb1);");

        for (int i = 0; i < 3; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(33.33);
            productGrid.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < produse.size(); i++) {
            Produs produs = produse.get(i);
            VBox productBox = new VBox();
            productBox.setStyle("-fx-border-color: #000; -fx-border-radius: 5; -fx-background-color: white; -fx-padding: 10;");
            productBox.setPrefWidth(150);
            productBox.getChildren().add(UserProductCard.createUserProductCard(produs, this, cartView));
            productGrid.add(productBox, i % 3, i / 3);
        }

        ScrollPane scrollPane = new ScrollPane(productGrid);
        scrollPane.setFitToWidth(true);

        VBox mainLayout = new VBox(10, scrollPane);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setAlignment(Pos.CENTER);

        Button backButton = new Button("Înapoi către meniu");
        backButton.setStyle("-fx-background-color:pink;-fx-padding: 10; -fx-font-weight: bold;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color:white;-fx-padding: 10; -fx-font-weight: bold;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color:pink;-fx-padding: 10; -fx-font-weight: bold;"));
        backButton.setOnAction(e -> {
            productStage.close();
            openUserMenu();
        });

        Button viewCartButton = new Button("Vizualizează coșul");
        viewCartButton.setStyle("-fx-background-color:pink;-fx-padding: 10; -fx-font-weight: bold;");
        viewCartButton.setOnMouseEntered(e -> viewCartButton.setStyle("-fx-background-color:white;-fx-padding: 10; -fx-font-weight: bold;"));
        viewCartButton.setOnMouseExited(e -> viewCartButton.setStyle("-fx-background-color:pink;-fx-padding: 10; -fx-font-weight: bold;"));
        viewCartButton.setOnAction(e -> {
            productStage.close();
            cartView.openCartView();
        });

        VBox buttonsLayout = new VBox(10, backButton, viewCartButton);
        buttonsLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().add(buttonsLayout);

        Scene productScene = new Scene(mainLayout, 800, 600);
        productStage.setScene(productScene);
        productStage.show();
    }

    /**
     * Închide aplicația și efectuează operațiunile de cleanup necesare.
     * Închide stage-ul principal și termină execuția aplicației.
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