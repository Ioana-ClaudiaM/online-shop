package com.products.Order;

import com.products.Alert;
import com.products.Cart.Cart;
import com.products.Main;
import com.products.Product.Produs;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa care gestionează interfața de vizualizare și manipulare a comenzilor.
 * Permite trimiterea comenzilor noi și vizualizarea/actualizarea statusului comenzilor existente.
 */
public class OrderView {
    /**
     * Lista tuturor comenzilor din sistem.
     */
    private List<Comanda> comenzi;
    /**
     * Indicator pentru a verifica dacă comanda curentă a fost trimisă.
     */
    public boolean orderSent = false;
    /**
     * Referință către coșul de cumpărături asociat.
     */
    private Cart cart;

    /**
     * Construiește o nouă vedere pentru comenzi cu un coș specificat.
     *
     * @param cart Coșul de cumpărături asociat cu această vedere
     */
    public OrderView(Cart cart) {
        this.comenzi = Main.getOrders();
        this.cart = cart;
    }

    /**
     * Deschide interfața grafică pentru vizualizarea și gestionarea comenzilor.
     * Permite vizualizarea detaliilor comenzilor și actualizarea statusului acestora.
     */
    public void openOrdersView() {
        Stage ordersStage = new Stage();
        ordersStage.setTitle("Lista Comenzilor");

        TableView<Comanda> ordersTableView = createOrdersTableView();

        String buttonStyle = """
                -fx-background-color: #ec407a;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20;
                -fx-background-radius: 5;
                -fx-cursor: hand;
                """;

        String buttonHoverStyle = """
                -fx-background-color: #d81b60;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);
                """;

        Button updateButton = new Button("Actualizează Statusul");
        Button closeButton = new Button("Închide");

        updateButton.setStyle(buttonStyle);
        closeButton.setStyle(buttonStyle);

        updateButton.setOnMouseEntered(e -> updateButton.setStyle(buttonStyle + buttonHoverStyle));
        updateButton.setOnMouseExited(e -> updateButton.setStyle(buttonStyle));
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(buttonStyle + buttonHoverStyle));
        closeButton.setOnMouseExited(e -> closeButton.setStyle(buttonStyle));

        updateButton.setOnAction(e -> updateOrderStatuses(ordersTableView));
        closeButton.setOnAction(e -> ordersStage.close());

        ordersTableView.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 5;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);
                """);

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #fce4ec, #f8bbd0);");

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(updateButton, closeButton);

        layout.getChildren().addAll(ordersTableView, buttonContainer);

        Scene scene = new Scene(layout, 600, 500);
        ordersStage.setScene(scene);
        ordersStage.show();
    }

    /**
     * Procesează și trimite comanda curentă din coș.
     * Verifică dacă comanda poate fi trimisă și actualizează starea sistemului corespunzător.
     */
    public void sendOrder() {
        if (orderSent) {
            Alert.showAlert("Eroare", "Comanda a fost deja trimisă.", "ERROR");
            return;
        }

        if (cart.getItems().isEmpty()) {
            Alert.showAlert("Eroare", "Coșul este gol. Adaugă produse înainte de a trimite comanda.", "EROARE");
            return;
        }

        Comanda comanda = new Comanda(new ArrayList<>(cart.getItems().keySet()),cart);
        comenzi.add(comanda);
        for (Produs produs : cart.getItems().keySet()) {
            produs.incrementNumarCumparari();
        }
        cart.clearCart();
        orderSent = true;

        Alert.showAlert("Informare","Comanda a fost trimisă cu succes!","CONFIRMATION");
    }
    /**
     * Creează și configurează tabelul pentru afișarea comenzilor.
     *
     * @return TableView configurat pentru afișarea comenzilor
     */
    private TableView<Comanda> createOrdersTableView() {
        TableView<Comanda> ordersTableView = new TableView<>();

        TableColumn<Comanda, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().toString()));
        statusColumn.setCellFactory(column -> new TableCell<>() {
            private final ComboBox<String> statusComboBox = new ComboBox<>();
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    statusComboBox.setItems(FXCollections.observableArrayList("În procesare", "Finalizată", "Expediată"));
                    statusComboBox.setValue(status);

                    statusComboBox.setOnAction(e -> {
                        Comanda comanda = getTableView().getItems().get(getIndex());
                        String selectedStatus = statusComboBox.getValue();

                        if (selectedStatus.equals("În procesare")) {
                            comanda.setStatus(StatusComanda.IN_PROCESARE);
                        } else if (selectedStatus.equals("Expediată")) {
                            comanda.setStatus(StatusComanda.EXPEDIATA);
                        } else {
                            comanda.setStatus(StatusComanda.FINALIZATA);
                        }
                    });

                    setGraphic(statusComboBox);
                }
            }
        });

        TableColumn<Comanda, String> detailsColumn = new TableColumn<>("Detalii Comandă");
        detailsColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDetails()));

        ordersTableView.getColumns().addAll(statusColumn, detailsColumn);
        ordersTableView.setItems(FXCollections.observableArrayList(Main.getOrders()));

        return ordersTableView;
    }

    /**
     * Actualizează statusul tuturor comenzilor din tabel conform selecțiilor utilizatorului.
     *
     * @param ordersTableView Tabelul care conține comenzile de actualizat
     */
    private void updateOrderStatuses(TableView<Comanda> ordersTableView) {
        for (Comanda comanda : ordersTableView.getItems()) {
            int rowIndex = ordersTableView.getItems().indexOf(comanda);
            String selectedStatus = (String) ordersTableView.getColumns().get(0).getCellObservableValue(rowIndex).getValue();

            if ("În procesare".equals(selectedStatus)) {
                comanda.setStatus(StatusComanda.IN_PROCESARE);
            } else if ("Expediată".equals(selectedStatus)) {
                comanda.setStatus(StatusComanda.EXPEDIATA);
            } else if ("Finalizată".equals(selectedStatus)) {
                comanda.setStatus(StatusComanda.FINALIZATA);
            }
        }
        Alert.showAlert("Notificare", "Statutul comenzilor a fost actualizat.", "INFORMATION");
    }
}