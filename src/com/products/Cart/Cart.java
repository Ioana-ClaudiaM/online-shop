package com.products.Cart;

import com.products.Product.Produs;
import com.products.Product.ProdusValidationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Clasa Cart reprezintă coșul de cumpărături al aplicației.
 * Gestionează produsele și cantitățile acestora folosind un HashMap.
 */

public class Cart {
    /** Mapare care stochează perechile produs-cantitate din coș */
    private final Map<Produs, Integer> items;
    /**
     * Constructorul implicit al clasei Cart.
     * Inițializează o nouă mapare goală pentru produse.
     */
    public Cart() {

        this.items = new HashMap<>();
    }

    /**
     * Returnează perechea curentă de produse și cantități din coș.
     *
     * @return Map&lt;Produs, Integer&gt; Perechile produs-cantitate din coș
     */
    public Map<Produs, Integer> getItems() {
        return items;
    }

    /**
     * Adaugă un produs în coș cu cantitatea specificată.
     *
     * @param produs Produsul care va fi adăugat în coș
     * @param quantity Cantitatea produsului care va fi adăugată
     * @return String Mesaj de confirmare a adăugării produsului
     * @throws ProdusValidationException dacă cantitatea cerută depășește stocul disponibil
     */
    public String addProduct(Produs produs, int quantity) throws ProdusValidationException {
        if (quantity > produs.getCantitateDisponibila()) {
            throw new ProdusValidationException("Stoc insuficient pentru produsul: " + produs.getNume() + ". Cantitate disponibilă: " + produs.getCantitateDisponibila());
        }
        items.put(produs, items.getOrDefault(produs, 0) + quantity);
        produs.setCantitateDisponibila(produs.getCantitateDisponibila()-quantity);
        return "Produs adăugat: " + produs.getNume() + ", Cantitate: " + quantity;
    }

    /**
     * Actualizează cantitatea unui produs din coș.
     * Dacă noua cantitate este 0 sau negativă, produsul este eliminat din coș.
     *
     * @param produs Produsul a cărui cantitate va fi actualizată
     * @param newQuantity Noua cantitate pentru produs
     * @return String Mesaj de confirmare a actualizării sau ștergerii
     */
    public String updateQuantity(Produs produs, int newQuantity) {
        if (newQuantity <= 0) {
            return removeProduct(produs);
        } else if (newQuantity > produs.getCantitateDisponibila()) {
            return "Cantitatea pentru " + produs.getNume().toUpperCase(Locale.ROOT) + " nu este disponibila. Sunt disponibile doar "+produs.getCantitateDisponibila()+" bucati.";
        } else {
            items.put(produs, newQuantity);
            return "Cantitatea pentru " + produs.getNume() + " a fost actualizată la " + newQuantity;
        }
    }

    /**
     * Elimină un produs din coș.
     *
     * @param produs Produsul care va fi eliminat din coș
     * @return String Mesaj de confirmare a eliminării produsului
     */
    public String removeProduct(Produs produs) {
        items.remove(produs);
        return "Produsul " + produs.getNume() + " a fost șters din coș.";
    }

    /**
     * Golește coșul de cumpărături, eliminând toate produsele.
     */
    public void clearCart(){
        items.clear();
    }
}