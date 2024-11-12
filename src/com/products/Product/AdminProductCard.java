package com.products.Product;

import com.products.AdminView;
import com.products.ControllersManipulation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AdminProductCard extends BaseProductCard {
    /**
     * Creează un card de produs pentru administratori.
     *
     * @param produs   Obiectul produs care conține informațiile despre produs.
     * @param adminView Instanța interfeței administratorului.
     * @param productView Instanța vizualizării produselor.
     * @return Un obiect Pane care reprezintă cardul de produs pentru administrator.
     */
    public static Pane createAdminProductCard(Produs produs, AdminView adminView, ProductView productView) {
        VBox productCard = (VBox) createBaseProductCard(produs);

        DropShadow defaultShadow = ControllersManipulation.createDefaultShadow();
        DropShadow hoverShadow = ControllersManipulation.createHoverShadow();
        productCard.setEffect(defaultShadow);
        productCard.setOnMouseEntered(e-> productCard.setEffect(hoverShadow));
        productCard.setOnMouseExited(e-> productCard.setEffect(defaultShadow));

        HBox adminButtons = new HBox(10);
        adminButtons.setAlignment(Pos.CENTER);

        Button editButton = new Button("Modifică");
        ImageView editIcon = new ImageView(new Image(AdminProductCard.class.getResourceAsStream("/pencil.png")));
        editIcon.setFitHeight(20);
        editIcon.setFitWidth(20);
        editButton.setGraphic(editIcon);

        editButton.setOnAction(e -> {
            productView.openAddProductForm(produs);
            adminView.refreshProductGrid();
        });

        Button deleteButton = new Button("Șterge");
        ImageView deleteIcon = new ImageView(new Image(AdminProductCard.class.getResourceAsStream("/trash-bin.png")));
        deleteIcon.setFitHeight(20);
        deleteIcon.setFitWidth(20);
        deleteButton.setGraphic(deleteIcon);

        String buttonStyle = """
            -fx-background-color: %s;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 20;
            """.formatted(ControllersManipulation.PINK_COLOR);

        String buttonHoverStyle = """
            -fx-background-color: %s;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 20;
            -fx-cursor: hand;
            """.formatted(ControllersManipulation.DARK_PINK);

        editButton.setStyle(buttonStyle);
        deleteButton.setStyle(buttonStyle);

        editButton.setOnMouseEntered(e -> editButton.setStyle(buttonHoverStyle));
        editButton.setOnMouseExited(e -> editButton.setStyle(buttonStyle));

        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(buttonHoverStyle));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle(buttonStyle));

        deleteButton.setOnAction(e -> {
            productView.confirmDeleteProduct(produs, adminView);
            adminView.refreshProductGrid();
        });

        adminButtons.getChildren().addAll(editButton, deleteButton);
        productCard.getChildren().add(adminButtons);

        return productCard;
    }
}