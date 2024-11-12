package com.products.Order;
import com.products.Cart.Cart;
import com.products.Product.Produs;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Clasa care gestionează detaliile și procesarea unei comenzi în sistemul de vânzări.
 * Aceasta menține informații despre produsele comandate, valoarea totală, data comenzii
 * și statusul curent al comenzii.
 */
public class Comanda {
    /**
     * Lista de produse incluse în comandă.
     */
    private List<Produs> products;

    /**
     * Valoarea totală a comenzii.
     */
    private double totalValue;

    /**
     * Data și ora la care a fost plasată comanda.
     */
    private LocalDateTime orderDateTime;

    /**
     * Statusul curent al comenzii.
     */
    private StatusComanda status;
    Cart cart;

    /**
     * Construiește o nouă comandă cu lista specificată de produse.
     * Calculează automat valoarea totală și setează data curentă.
     *
     * @param products Lista de produse care vor fi incluse în comandă
     */
    public Comanda(List<Produs> products,Cart cart) {
        this.cart=cart;
        this.products = products;
        this.totalValue = calculateTotalValue(cart.getItems());
        this.orderDateTime = LocalDateTime.now();
        this.status = StatusComanda.IN_PROCESARE;
    }
    /**
     * Construiește o nouă comandă cu parametri specificați.
     *
     * @param products Lista de produse incluse în comandă
     * @param totalValue Valoarea totală a comenzii
     * @param orderDateTime Data și ora comenzii
     * @param status Statusul inițial al comenzii
     */
    public Comanda(List<Produs> products, double totalValue, LocalDateTime orderDateTime, StatusComanda status) {
        this.products = products;
        this.totalValue = totalValue;
        this.orderDateTime = orderDateTime;
        this.status = StatusComanda.IN_PROCESARE;
    }

    /**
     * Calculează valoarea totală a comenzii bazată pe prețurile produselor.
     *
     * @param products Lista de produse pentru care se calculează valoarea totală
     * @return Suma totală a prețurilor produselor
     */
    private double calculateTotalValue(Map<Produs, Integer> products) {
        double total = 0.0;
        for (Map.Entry<Produs, Integer> entry : products.entrySet()) {
            Produs product = entry.getKey();
            Integer quantity = entry.getValue();

            total += product.getPret() * quantity;
        }
        return total;
    }



    /**
     * @return Lista de produse din comandă
     */
    public List<Produs> getProducts() {
        return products;
    }

    /**
     * @return Valoarea totală a comenzii
     */
    public double getTotalValue() {
        return totalValue;
    }

    /**
     * @return Data si ora la care a fost plasată comanda
     */
    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    /**
     * @return Statusul curent al comenzii
     */
    public StatusComanda getStatus() {
        return status;
    }

    /**
     * Actualizează statusul comenzii.
     *
     * @param status Noul status al comenzii
     */
    public void setStatus(StatusComanda status) {
        this.status = status;
    }

    /**
     * Generează un string cu detaliile complete ale comenzii.
     * Include data comenzii, valoarea totală și lista produselor cu prețurile lor.
     *
     * @return String formatat cu toate detaliile comenzii
     */
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Order Date: ").append(orderDateTime.toString()).append("\n");
        details.append("Total Value: ").append(totalValue).append("\n");
        details.append("Products: ");
        for (Produs product : products) {
            details.append(product.getNume()).append(" (Price: ").append(product.getPret()).append("), ");
        }
        return details.toString();
    }
}