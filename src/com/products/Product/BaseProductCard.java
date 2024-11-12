package com.products.Product;

import com.products.ControllersManipulation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BaseProductCard {
    /**
     * Creează un card de produs de bază utilizat atât în interfața utilizatorului, cât și în interfața de administrator.
     *
     * @param produs Obiectul produs care conține informațiile despre produs.
     * @return Un obiect Pane care reprezintă cardul de produs de bază.
     */
    protected static Pane createBaseProductCard(Produs produs) {
        VBox productCard = new VBox(15);
        productCard.setAlignment(Pos.CENTER);
        productCard.setPadding(new Insets(15));

        String baseStyle = """
            -fx-background-color: white;
            -fx-border-color: %s;
            -fx-border-width: 2;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            """.formatted(ControllersManipulation.PINK_COLOR);

        productCard.setStyle(baseStyle);

        Label productName = new Label(produs.getNume());
        productName.setFont(Font.font("System", FontWeight.BOLD, 16));
        productName.setStyle("-fx-text-fill: " + ControllersManipulation.DARK_PINK + ";");

        VBox descriptionBox = new VBox(5);
        Label descriptionTitle = new Label("Descriere:");
        descriptionTitle.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        Label productDescription = new Label(produs.getDescriere());
        productDescription.setWrapText(true);
        descriptionBox.getChildren().addAll(descriptionTitle, productDescription);

        String descriptionStyle = """
            -fx-padding: 10;
            -fx-background-color: #FFF0F5;
            -fx-background-radius: 5;
            """;
        descriptionBox.setStyle(descriptionStyle);

        Label productPrice = new Label(String.format("%.2f RON", produs.getPret()));
        productPrice.setFont(Font.font("System", FontWeight.BOLD, 18));
        productPrice.setStyle("-fx-text-fill: " + ControllersManipulation.DARK_PINK + ";");

        productCard.getChildren().addAll(
                productName,
                descriptionBox,
                productPrice
        );

        return productCard;
    }
}