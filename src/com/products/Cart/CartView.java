package com.products.Cart;
import com.products.Alert;
import com.products.ControllersManipulation;
import com.products.Order.OrderView;
import com.products.Product.Produs;
import com.products.Product.ProdusValidationException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;

import static com.products.ControllersManipulation.*;

/**
 * Clasa CartView reprezintă interfața grafică pentru coșul de cumpărături.
 * Aceasta gestionează afișarea și manipularea produselor din coș, inclusiv
 * actualizarea cantităților și calculul totalului.
 */
public class CartView {
    /** Referință către vizualizarea comenzilor*/
    private OrderView orderView;
    /** Referință către obiectul coș de cumpărături */
    private Cart cart;
    /** Container pentru lista de produse */
    private VBox productsContainer;
    /** Etichetă pentru afișarea totalului */
    private Label totalLabel;
    /** Fereastra principală a coșului */
    private Stage cartStage;
    private final ControllersManipulation controllersManipulation=new ControllersManipulation();
    /**
     * Constructorul clasei CartView
     * @param cart Obiectul coș de cumpărături care va fi gestionat
     * @param orderView Referință către vizualizarea comenzii
     */
    public CartView(Cart cart, OrderView orderView) {
        this.cart = cart;
        this.orderView = orderView;
    }

    /**
     * Deschide fereastra coșului de cumpărături și inițializează interfața grafică.
     * Dacă coșul este gol, afișează un mesaj de informare și închide fereastra.
     */
    public void openCartView() {
        cartStage = new Stage();
        cartStage.setTitle("Coșul de Cumpărături");
        orderView.orderSent = false;

        VBox mainContainer = new VBox(15);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, " + MAIN_COLOR + ", " + DARKER_COLOR + ");");

        Label titleLabel = controllersManipulation.createStyledLabel("Coșul Meu de Cumpărături", 24);
        titleLabel.setStyle(titleLabel.getStyle() + "-fx-font-weight: bold; -fx-text-fill: white;");

        if (cart.getItems().isEmpty()) {
            Alert.showAlert("Informare", "Coșul de cumpărături este gol!", "INFORMATION");
            cartStage.close();
            return;
        }

        productsContainer = new VBox(10);
        productsContainer.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-background-radius: 10px;");
        productsContainer.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.1)));

        HBox totalBox = new HBox(10);
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setPadding(new Insets(10));
        totalBox.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");

        totalLabel = controllersManipulation.createStyledLabel("", 18);
        totalLabel.setStyle(totalLabel.getStyle() + "-fx-font-weight: bold;");
        totalBox.getChildren().add(totalLabel);

        Button sendOrderButton = controllersManipulation.createCartStyledButton("Trimite Comanda", "shopping-cart");
        sendOrderButton.setOnAction(e -> {
            orderView.sendOrder();
            cartStage.close();
        });

        mainContainer.getChildren().addAll(
                titleLabel,
                productsContainer,
                totalBox,
                sendOrderButton
        );

        updateCartDisplay();

        Scene cartScene = new Scene(mainContainer, 500, 600);
        cartStage.setScene(cartScene);
        cartStage.show();
    }


    /**
     * Creează un container pentru afișarea unui produs în coș
     *
     * @param produs Produsul care va fi afișat
     * @param quantity Cantitatea produsului
     * @return HBox Container-ul cu informațiile produsului și controalele asociate
     */
    private HBox createProductBox(Produs produs, int quantity) {
        HBox productBox = new HBox(15);
        productBox.setAlignment(Pos.CENTER_LEFT);
        productBox.setPadding(new Insets(10));
        productBox.setStyle("-fx-background-color: #fce4ec; -fx-background-radius: 5px;");

        VBox productInfo = new VBox(5);
        Label nameLabel = controllersManipulation.createStyledLabel(produs.getNume(), 16);
        Label priceLabel = controllersManipulation.createStyledLabel(String.format("%.2f RON", produs.getPret()), 14);
        productInfo.getChildren().addAll(nameLabel, priceLabel);

        TextField quantityField = new TextField(String.valueOf(quantity));
        quantityField.setPrefWidth(60);
        quantityField.setStyle("-fx-background-radius: 5px;");

        Button updateButton = controllersManipulation.createCartStyledButton("Actualizează", "refresh-cw");
        updateButton.setOnAction(e -> updateCartQuantity(produs, Integer.parseInt(quantityField.getText())));

        Button removeButton = controllersManipulation.createCartStyledButton("Șterge", "trash-2");
        removeButton.setOnAction(e -> removeFromCart(produs));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        productBox.getChildren().addAll(
                productInfo,
                spacer,
                quantityField,
                updateButton,
                removeButton
        );

        return productBox;
    }

    /**
     * Adaugă un produs în coș cu cantitatea specificată
     *
     * @param produs Produsul care va fi adăugat
     * @param quantity Cantitatea care va fi adăugată
     */
    public void addToCart(Produs produs, int quantity) {
        try {
            Alert.showAlert("Information",cart.addProduct(produs, quantity),"INFROMATION");
            if (cartStage != null && cartStage.isShowing()) {
                updateCartDisplay();
            }
        } catch (ProdusValidationException e) {
            Alert.showAlert("Eroare", e.getMessage(), "ERROR");
        }
    }

    /**
     * Actualizează cantitatea unui produs din coș
     *
     * @param produs Produsul a cărui cantitate va fi actualizată
     * @param newQuantity Noua cantitate
     * @throws IllegalArgumentException dacă noua cantitate este invalidă
     */
    private void updateCartQuantity(Produs produs, int newQuantity) {
        try {
            String message = cart.updateQuantity(produs, newQuantity);
            Alert.showAlert("Informare",message,"INFORMATION");
            updateCartDisplay();
        } catch (IllegalArgumentException e) {
            Alert.showAlert("Eroare", e.getMessage(), "ERROR");
        }
    }

    /**
     * Elimină un produs din coș
     *
     * @param produs Produsul care va fi eliminat
     */
    private void removeFromCart(Produs produs) {
        String message = cart.removeProduct(produs);
        System.out.println(message);
        updateCartDisplay();
    }

    /**
     * Actualizează afișarea produselor din coș și recalculează totalul.
     * Dacă coșul devine gol, închide fereastra.
     */
    private void updateCartDisplay() {
        productsContainer.getChildren().clear();
        double totalValue = 0.0;

        for (Produs produs : cart.getItems().keySet()) {
            int quantity = cart.getItems().get(produs);
            totalValue += produs.getPret() * quantity;

            HBox productBox = createProductBox(produs, quantity);
            productsContainer.getChildren().add(productBox);
        }

        totalLabel.setText(String.format("Total: %.2f RON", totalValue));

        if (cart.getItems().isEmpty()) {
            cartStage.close();
        }
    }
}