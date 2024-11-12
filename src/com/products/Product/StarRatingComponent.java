package com.products.Product;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * Componenta de evaluare cu stele, care permite utilizatorului să selecteze un rating pentru un produs.
 */
public class StarRatingComponent extends HBox {
    private static final int MAX_STARS = 5;
    private final SVGPath[] stars;
    private Produs produs;
    private double currentSelectedRating;

    /**
     * Constructorul clasei StarRatingComponent.
     *
     * @param produs Produsul pentru care se face evaluarea.
     */
    public StarRatingComponent(Produs produs) {
        this.setSpacing(2);
        this.produs = produs;
        this.stars = new SVGPath[MAX_STARS];
        this.currentSelectedRating = 0;

        String starPath = "M 12,2 L 9,9 L 2,9 L 7.5,13 L 5.5,20 L 12,16 L 18.5,20 L 16.5,13 L 22,9 L 15,9 Z";

        for (int i = 0; i < MAX_STARS; i++) {
            stars[i] = new SVGPath();
            stars[i].setContent(starPath);
            stars[i].setScaleX(0.7);
            stars[i].setScaleY(0.7);
            int starIndex = i;

            stars[i].setOnMouseClicked(event -> {
                currentSelectedRating = starIndex + 1;
                updateStars();
            });

            this.getChildren().add(stars[i]);
        }

        Button confirmButton = new Button("Confirmă Rating");
        confirmButton.setOnAction(event -> {
            if (currentSelectedRating > 0) {
                double totalRating = produs.getRating() * produs.getNrRatinguri();
                produs.incrementNumarRatinguri();
                totalRating += currentSelectedRating;
                produs.setRating(totalRating / produs.getNrRatinguri());
                currentSelectedRating = 0;
                updateStars();
            }
        });

        confirmButton.setStyle("-fx-background-color: #ff69b4; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 10px; " +
                "-fx-padding: 10 20; " +
                "-fx-border-radius: 15; " +
                "-fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0.0, 0, 1);");

        confirmButton.setOnMouseEntered(event ->
                confirmButton.setStyle("-fx-background-color: #ff1493; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 10px; " +
                        "-fx-padding: 10 20; " +
                        "-fx-border-radius: 15; " +
                        "-fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0.0, 0, 1);"));

        confirmButton.setOnMouseExited(event ->
                confirmButton.setStyle("-fx-background-color: #ff69b4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 10px; " +
                        "-fx-padding: 10 20; " +
                        "-fx-border-radius: 15; " +
                        "-fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0.0, 0, 1);"));

        this.getChildren().add(confirmButton);
        updateStars();
    }

    /**
     * Actualizează stelele afișate în funcție de ratingul selectat și ratingul produsului.
     */
    private void updateStars() {
        int fullStars = (int) Math.round(currentSelectedRating > 0 ? currentSelectedRating : produs.getRating());

        for (int i = 0; i < MAX_STARS; i++) {
            if (i < fullStars) {
                stars[i].setFill(Color.GOLD);
                stars[i].setStroke(Color.ORANGE);
            } else {
                stars[i].setFill(Color.LIGHTGRAY);
                stars[i].setStroke(Color.GRAY);
            }
        }
    }
}
