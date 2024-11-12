package com.products.File;

import com.products.Main;
import com.products.Order.Comanda;
import com.products.Order.StatusComanda;
import com.products.Product.Produs;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa FileManager implementează un singleton pentru gestionarea operațiilor cu fișiere.
 * Aceasta se ocupă cu încărcarea și salvarea produselor în format JSON și a comenzilor în format text.
 */
public class FileManager {
    /** Calea către fișierul JSON cu produse */
    private static final String FILE_PATH = "data/Produse.json";

    /** Instanța singleton a clasei FileManager */
    private static FileManager instance;

    /** Lista de produse încărcată din fișier */
    private List<Produs> produse;

    /** Calea către fișierul text cu comenzi */
    private static final String ORDER_FILE = "data/Orders.txt";

    /**
     * Constructor privat pentru implementarea pattern-ului Singleton.
     * Încarcă produsele din fișier la instanțiere.
     */
    private FileManager() {
        produse = loadFromFile();
    }

    /**
     * Returnează instanța singleton a clasei FileManager.
     *
     * @return FileManager Instanța unică a managerului de fișiere
     */
    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    /**
     * Returnează lista de produse încărcată.
     *
     * @return List&lt;Produs&gt; Lista curentă de produse
     */
    public List<Produs> getProduse() {
        return produse;
    }

    /**
     * Încarcă produsele din fișierul JSON.
     *
     * @return List<Produs> Lista de produse citită din fișier
     */
    List<Produs> loadFromFile() {
        List<Produs> produse = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Produs produs = new Produs(
                        jsonObject.getDouble("pret"),
                        jsonObject.getString("descriere"),
                        jsonObject.getInt("cantitateDisponibila"),
                        jsonObject.getString("nume"),
                        jsonObject.optString("dataAdaugarii", ""),
                        jsonObject.optString("dataExpirare", ""),
                        jsonObject.optDouble("rating", 0.0),
                        jsonObject.optInt("numarCumparari", 0),
                        jsonObject.getInt("nrRatinguri")
                );
                produse.add(produs);
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fișierului: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Eroare la procesarea fișierului JSON: " + e.getMessage());
        }
        return produse;
    }

    /**
     * Salvează lista de produse în fișierul JSON.
     *
     * @param produse Lista de produse ce va fi salvată
     * @throws FileOperationException dacă apare o eroare la salvarea în fișier
     */
    public void saveToFile(List<Produs> produse) throws FileOperationException {
        JSONArray jsonArray = new JSONArray();
        for (Produs produs : produse) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pret", produs.getPret());
            jsonObject.put("descriere", produs.getDescriere());
            jsonObject.put("cantitateDisponibila", produs.getCantitateDisponibila());
            jsonObject.put("nume", produs.getNume());
            jsonObject.put("dataAdaugarii", produs.getDataAdaugarii());
            jsonObject.put("dataExpirare", produs.getDataExpirare());
            jsonObject.put("rating", produs.getRating());
            jsonObject.put("nrRatinguri", produs.getNrRatinguri());
            jsonObject.put("numarCumparari", produs.getNumarCumparari());
            jsonArray.put(jsonObject);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(jsonArray.toString(4));
        } catch (IOException e) {
            throw new FileOperationException("Eroare la salvarea fișierului: " + e.getMessage(), e);
        }
    }

    /**
     * Salvează toate comenzile în fișierul text.
     *
     * @param comenzi Lista de comenzi ce va fi salvată
     * @throws FileOperationException dacă apare o eroare la salvarea în fișier
     */
    public void saveAllOrders(List<Comanda> comenzi) throws FileOperationException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE))) {
            for (Comanda comanda : comenzi) {
                writer.write(formatOrderData(comanda));
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new FileOperationException("Eroare la salvarea fișierului: " + e.getMessage(), e);
        }
    }

    /**
     * Încarcă toate comenzile din fișierul text.
     *
     * @return ListC&lt;omanda&gt; Lista comenzilor citite din fișier
     */
    public static List<Comanda> loadOrders() {
        List<Comanda> orders = new ArrayList<>();
        try {
            FileReader reader = new FileReader(ORDER_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Comanda comanda = parseOrderData(line);
                orders.add(comanda);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Formatează datele unei comenzi pentru salvare în fișier.
     *
     * @param comanda Comanda ce va fi formatată
     * @return String Date formatate ale comenzii
     */
    private static String formatOrderData(Comanda comanda) {
        StringBuilder sb = new StringBuilder();
        sb.append("Data comenzii:" + comanda.getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));        sb.append(",");
        sb.append("Numarul produselor comandate:" + comanda.getProducts().size());
        sb.append(",");
        sb.append("Suma totala a comenzii:" + comanda.getTotalValue());
        sb.append(",");
        sb.append("Produsele comandate:" + String.join("|", comanda.getProducts().stream()
                .map(Produs::getNume)
                .toArray(String[]::new)));
        sb.append(",");
        sb.append("Statusul comenzii: " + comanda.getStatus());
        return sb.toString();
    }

    /**
     * Parsează o linie din fișierul de comenzi în obiectul Comanda.
     *
     * @param line Linia ce va fi parsată
     * @return Comanda Obiectul comandă creat din datele parsate
     */
    private static Comanda parseOrderData(String line) {
        String[] parts = line.split(",");

        String orderDateText = parts[0].replace("Data comenzii:", "").trim();
        LocalDateTime orderDateTime = LocalDateTime.parse(orderDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        int productCount = Integer.parseInt(parts[1].replace("Numarul produselor comandate:", "").trim());
        double totalValue = Double.parseDouble(parts[2].replace("Suma totala a comenzii:", "").trim());
        String[] productNames = parts[3].replace("Produsele comandate:", "").split("\\|");
        StatusComanda status = StatusComanda.valueOf(parts[4].replace("Statusul comenzii:", "").trim());

        List<Produs> products = new ArrayList<>();
        for (String productName : productNames) {
            Produs product = Main.getProduse().stream()
                    .filter(p -> p.getNume().equals(productName))
                    .findFirst()
                    .orElse(null);
            if (product != null) {
                products.add(product);
            }
        }

        return new Comanda(products, totalValue, orderDateTime, status);
    }
}