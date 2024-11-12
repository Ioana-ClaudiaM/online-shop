package com.products.Product;

import com.products.Alert;
import com.products.Cart.CartView;
import com.products.ControllersManipulation;
import com.products.UserView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
public class UserProductCard extends BaseProductCard {
    static ControllersManipulation controllersManipulation=new ControllersManipulation();
    /**
     * Creează un card de produs pentru utilizatori.
     *
     * @param produs   Obiectul produs care conține informațiile despre produs.
     * @param userView Instanța interfeței utilizatorului.
     * @param cartView Instanța vizualizării coșului de cumpărături.
     * @return Un obiect Pane care reprezintă cardul de produs pentru utilizatori.
     */
    public static Pane createUserProductCard(Produs produs, UserView userView, CartView cartView) {
        VBox productCard = (VBox) createBaseProductCard(produs);

        DropShadow defaultShadow = ControllersManipulation.createDefaultShadow();
        DropShadow hoverShadow = ControllersManipulation.createHoverShadow();
        productCard.setOnMouseEntered(e-> productCard.setEffect(hoverShadow));
        productCard.setOnMouseExited(e-> productCard.setEffect(defaultShadow));

        VBox ratingBox = new VBox(10);
        ratingBox.setAlignment(Pos.CENTER);
        StarRatingComponent ratingStars = new StarRatingComponent(produs);

        Label ratingLabel = new Label(String.format("%.1f/5.0", produs.getRating()));
        ratingLabel.setStyle("-fx-text-fill: " + ControllersManipulation.DARK_PINK + "; -fx-font-size: 14px;");
        Label purchasesLabel = new Label(String.format("(%d cumpărări)", produs.getNumarCumparari()));
        purchasesLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 12px;");
        ratingBox.getChildren().addAll(ratingStars, ratingLabel, purchasesLabel);

        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER);
        Label hintQuantity = new Label("Cantitate:");
        TextField quantityField = new TextField("1");
        quantityField.setPrefWidth(60);

        String quantityFieldStyle = """
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-border-color: %s;
            -fx-border-width: 1;
            """.formatted(ControllersManipulation.PINK_COLOR);

        quantityField.setStyle(quantityFieldStyle);
        quantityBox.getChildren().addAll(hintQuantity, quantityField);

        Button addToCartButton = ControllersManipulation.createShoppingCartButton();
        addToCartButton.setOnAction(e -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                cartView.addToCart(produs, quantity);
            } catch (NumberFormatException ex) {
                Alert.showAlert("Eroare", "Te rog introdu o cantitate validă.", "ERROR");
            }
        });

        productCard.getChildren().addAll(
                ratingBox,
                quantityBox,
                addToCartButton
        );

        return productCard;
    }
}