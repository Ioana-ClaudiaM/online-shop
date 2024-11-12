package com.products;

import com.products.Product.AdminProductCard;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Clasa ControllersManipulation este folosita pentru a gestiona actiunile controllerelor.
 */
public class ControllersManipulation {
    /** Culoarea principală folosită în interfață */
    public static final String MAIN_COLOR = "#f8bbd0";
    /** Culoarea secundară mai închisă folosită în interfață */
    public static final String DARKER_COLOR = "#f48fb1";
    /** Culoarea folosită pentru butoane */
    public static final String BUTTON_COLOR = "#f50057";
    /** Culoarea folosită pentru efectul de hover pe butoane */
    public static final String HOVER_COLOR = "#c51162";
    /** Culoarea roz utilizată pentru carduri. */
    public static final String PINK_COLOR = "#FF69B4";
    /** Culoarea roz deschis. */
    public static final String LIGHT_PINK = "#FFB6C6";
    /** Culoarea roz închis. */
    public static final String DARK_PINK = "#FF1493";

    /**
     * Constructorul implicit al clasei.
     */
    public ControllersManipulation() {
    }


    public Button createStyledButton(String text, String normalStyle, String hoverStyle, DropShadow shadow) {
        Button button = new Button(text);
        button.setStyle(normalStyle);
        button.setEffect(shadow);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));

        return button;
    }

    /**
     * Creează o umbră la hover pe un element.
     *
     * @return Efectul de umbră creat și stilizat
     */
    public static DropShadow createDefaultShadow() {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10.0);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        return shadow;
    }

    /**
     * Creează o umbră la hover pe un element.
     *
     * @return Efectul de umbră creat și stilizat
     */
    public static DropShadow createHoverShadow() {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(15.0);
        shadow.setOffsetX(5.0);
        shadow.setOffsetY(5.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.4));
        return shadow;
    }

    /**
     * Creează o etichetă stilizată cu dimensiunea fontului specificată
     *
     * @param text Textul etichetei
     * @param fontSize Dimensiunea fontului
     * @return Label Eticheta stilizată
     */
    public Label createStyledLabel(String text, int fontSize) {
        Label label = new Label(text);
        label.setStyle(String.format("-fx-font-size: %dpx;", fontSize));
        return label;
    }

    /**
     * Creează un buton stilizat cu text și icon pentru cart
     *
     * @param text Textul butonului
     * @param iconName Numele iconului care va fi afișat
     * @return Button Butonul stilizat
     */
    public Button createCartStyledButton(String text, String iconName) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + BUTTON_COLOR + ";"
                + "-fx-text-fill: white;"
                + "-fx-padding: 8 15;"
                + "-fx-background-radius: 5;"
                + "-fx-font-size: 13px;"
                + "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> button.setStyle(button.getStyle().replace(BUTTON_COLOR, HOVER_COLOR)));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace(HOVER_COLOR, BUTTON_COLOR)));

        return button;
    }

    /**
     * Creează un buton stilizat cu icon pentru coș.
     *
     * @return Butonul creat și stilizat
     */
    public static Button createShoppingCartButton() {
        Button addToCartButton = new Button("Adaugă în Coș");

        ImageView cartIcon = new ImageView(new Image(AdminProductCard.class.getResourceAsStream("/cart-icon.png")));
        cartIcon.setFitHeight(20);
        cartIcon.setFitWidth(20);
        addToCartButton.setGraphic(cartIcon);

        String buttonStyle = """
                -fx-background-color: %s;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20 10 20;
                -fx-background-radius: 20;
                """.formatted(PINK_COLOR);

        String buttonHoverStyle = """
                -fx-background-color: %s;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20 10 20;
                -fx-background-radius: 20;
                -fx-cursor: hand;
                """.formatted(DARK_PINK);

        addToCartButton.setStyle(buttonStyle);
        addToCartButton.setOnMouseEntered(e -> addToCartButton.setStyle(buttonHoverStyle));
        addToCartButton.setOnMouseExited(e -> addToCartButton.setStyle(buttonStyle));

        return addToCartButton;
    }


    /**
     * Creează un buton stilizat cu efecte de hover.
     *
     * @param text Textul care va fi afișat pe buton
     * @return Butonul creat și stilizat
     */
    public Button createReportStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("""
            -fx-background-color: #f50057;
            -fx-text-fill: white;
            -fx-padding: 8 15;
            -fx-background-radius: 5;
            """);

        button.setOnMouseEntered(e ->
                button.setStyle("""
                -fx-background-color: #c51162;
                -fx-text-fill: white;
                -fx-padding: 8 15;
                -fx-background-radius: 5;
                """)
        );

        button.setOnMouseExited(e ->
                button.setStyle("""
                -fx-background-color: #f50057;
                -fx-text-fill: white;
                -fx-padding: 8 15;
                -fx-background-radius: 5;
                """)
        );

        return button;
    }
}
