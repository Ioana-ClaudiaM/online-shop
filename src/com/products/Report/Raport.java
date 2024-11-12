package com.products.Report;

import com.products.Order.Comanda;
import com.products.Order.StatusComanda;
import com.products.Product.Produs;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Clasă responsabilă pentru generarea diferitelor tipuri de rapoarte
 * privind vânzările, stocurile și performanța produselor.
 *
 */
public class Raport {
    /**
     * Numele raportului curent.
     */
    private String numeRaport;
    /**
     * Lista de produse disponibile în sistem.
     */
    private List<Produs> produse;

    /**
     * Lista de comenzi efectuate.
     */
    private List<Comanda> comenzi;
    private static final String REPORTS_DIR = "Rapoarte";

    /**
     * Constructorul pentru clasa Raport.
     *
     * @param numeRaport Numele raportului
     * @param produse    Lista de produse disponibile
     * @param comenzi    Lista de comenzi efectuate
     */
    public Raport(String numeRaport, List<Produs> produse, List<Comanda> comenzi) {
        this.numeRaport = numeRaport;
        this.produse = produse;
        this.comenzi = comenzi;
        createReportsDirectory();
    }

    private void createReportsDirectory() {
        try {
            Path dirPath = Paths.get(REPORTS_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }
        } catch (IOException e) {
            System.err.println("Nu s-a putut crea directorul Rapoarte: " + e.getMessage());
        }
    }

    private String getReportPath(String filename) {
        return Paths.get(REPORTS_DIR, filename).toString();
    }

    /**
     * Generează un raport cu totalul vânzărilor.
     * Include suma totală a vânzărilor și numărul total de comenzi.
     *
     * @param filename Numele fișierului în care va fi salvat raportul
     */
    public void genereazaRaportTotalVanzari(String filename) {
        double totalVanzari = 0;
        for (Comanda comanda : comenzi) {
            totalVanzari += comanda.getTotalValue();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getReportPath(filename)))) {
            writer.write("Raport Total Vanzari\n");
            writer.write("========================\n");
            writer.write("Total Vanzari: " + totalVanzari + " lei\n");
            writer.write("Numar Total Comenzi: " + comenzi.size() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generează un raport cu starea curentă a stocului pentru toate produsele.
     *
     * @param filename Numele fișierului în care va fi salvat raportul
     */
    public void genereazaRaportProdusePeStoc(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getReportPath(filename)))) {
            writer.write("Raport Produse pe Stoc\n");
            writer.write("========================\n");
            for (Produs produs : produse) {
                if(produs.getCantitateDisponibila()>0)
                {
                    writer.write("Produs: " + produs.getNume() +
                            ", Cantitate Disponibila: " + produs.getCantitateDisponibila() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generează un raport cu produsele adăugate în ultimele 30 de zile.
     *
     * @param filename Numele fișierului în care va fi salvat raportul
     */
    public void genereazaRaportProduseAdaugateRecent(String filename) {
        LocalDate today = LocalDate.now();
        LocalDate limita = today.minusDays(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getReportPath(filename)))) {
            writer.write("Raport Produse Adăugate Recent\n");
            writer.write("================================\n");

            for (Produs produs : produse) {
                try {
                    LocalDate dataAdaugare = LocalDate.parse(produs.getDataAdaugarii(), formatter);
                    if (dataAdaugare.isAfter(limita)) {
                        writer.write("Produs: " + produs.getNume() +
                                ", Data Adăugare: " + produs.getDataAdaugarii() + "\n");
                    }
                } catch (DateTimeParseException e) {
                    writer.write("Produs: " + produs.getNume() +
                            ", Data Adăugare invalidă: " + produs.getDataAdaugarii() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generează un raport cu produsele care vor expira în următoarele 30 de zile.
     *
     * @param filename Numele fișierului în care va fi salvat raportul
     */
    public void genereazaRaportProduseAproapeExpirate(String filename) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getReportPath(filename)))) {
            writer.write("Raport Produse Aproape Expirate\n");
            writer.write("================================\n");

            for (Produs produs : produse) {
                try {
                    if (produs.getDataExpirare() != null && !produs.getDataExpirare().isEmpty()) {
                        LocalDate dataExpirare = LocalDate.parse(produs.getDataExpirare(), formatter);
                        if (dataExpirare.isBefore(today.plusDays(30)) && dataExpirare.isAfter(today)) {
                            writer.write("Produs: " + produs.getNume() + ", Data Expirare: " + produs.getDataExpirare() + "\n");
                        }
                    } else {
                        writer.write("Produs: " + produs.getNume() + ", Data Expirare invalidă: " + produs.getDataExpirare() + "\n");
                    }
                } catch (DateTimeParseException e) {
                    writer.write("Produs: " + produs.getNume() + ", Data Expirare invalidă: " + produs.getDataExpirare() + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generează un raport cu referire la stocurile de produse.
     * Include produsele sub limita de cantitiate disponibilă (5)
     *
     * @param filename Numele fișierului în care va fi salvat raportul
     */
    public void genereazaRaportGeneralStoc(String filename) {
        int numarProduse = produse.size();
        double valoareTotalaStoc = produse.stream().mapToDouble(Produs::getPret).sum();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getReportPath(filename)))) {
            writer.write("Raport General al Stocului\n");
            writer.write("=============================\n");
            writer.write("Număr Total de Produse: " + numarProduse + "\n");
            writer.write("Valoare Totală a Stocului: " + valoareTotalaStoc + "\n");
            writer.write("Produse Sub Limita Minimă de Stoc:\n");

            for (Produs produs : produse) {
                if (produs.getCantitateDisponibila() < 5) {
                    writer.write("Produs: " + produs.getNume() + ", Cantitate Disponibilă: " + produs.getCantitateDisponibila() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generează un raport cu vanzarile pe zile.
     *
     * @param filename Numele fișierului în care va fi salvat raportul
     */
    public void genereazaRaportTendinteVanzari(String filename) {
        Map<LocalDate, Double> vanzariZilnice = new HashMap<>();

        for (Comanda comanda : comenzi) {
            LocalDate dataComenzii = LocalDate.from(comanda.getOrderDateTime());
            double totalComanda = comanda.getProducts().stream().mapToDouble(Produs::getPret).sum();

            vanzariZilnice.put(dataComenzii, vanzariZilnice.getOrDefault(dataComenzii, 0.0) + totalComanda);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getReportPath(filename)))) {
            writer.write("Raport Tendințe în Vânzări\n");
            writer.write("===========================\n");

            for (Map.Entry<LocalDate, Double> entry : vanzariZilnice.entrySet()) {
                writer.write("Data: " + entry.getKey() + ", Vânzări: " + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Generează un raport de ratinguri pentru toate produsele utilizând o matrice.
     *
     * @param filename Numele fișierului în care va fi salvat raportul
     */
    public void genereazaRaportRatinguriProduseMatrice(String filename) {
        double[][] ratinguriMatrice = new double[produse.size()][2];

        for (int i = 0; i < produse.size(); i++) {
            Produs produs = produse.get(i);
            ratinguriMatrice[i][0] = produs.getRating();
            ratinguriMatrice[i][1] = produs.getNrRatinguri();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getReportPath(filename)))) {
            writer.write("Raport Ratinguri Produse (Matrice)\n");
            writer.write("====================================\n");

            for (int i = 0; i < ratinguriMatrice.length; i++) {
                writer.write("Produs: " + produse.get(i).getNume() +
                        ", Rating: " + ratinguriMatrice[i][0] +
                        ", Ratinguri: " + ratinguriMatrice[i][1] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Generează un raport cu comenzile finalizate.
     * Include lista cu detaliile comenzilor, dar și numărul total al lor și valoarea totală.
     *
     * @param filename Numele fișierului în care va fi salvat raportul
     */
    public List<Comanda> genereazaRaportComenziFinalizate(String filename) {
        List<Comanda> comenziFinalizate = comenzi.stream()
                .filter(c -> c.getStatus() == StatusComanda.FINALIZATA)
                .collect(Collectors.toList());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getReportPath(filename)))) {
            writer.write("Raport Comenzi Finalizate\n");
            writer.write("========================\n\n");

            double totalValoare = 0;
            for (Comanda comanda : comenziFinalizate) {
                totalValoare += comanda.getTotalValue();
                writer.write(String.format("Comandă: %s\n", comanda.getProducts()));
                writer.write(String.format("Data: %s\n", comanda.getOrderDateTime()));
                writer.write(String.format("Valoare: %.2f lei\n", comanda.getTotalValue()));
                writer.write("---------------------\n");
            }

            writer.write(String.format("\nTotal Comenzi Finalizate: %d\n", comenziFinalizate.size()));
            writer.write(String.format("Valoare Totală: %.2f lei\n", totalValoare));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comenziFinalizate;
    }
    /**
     * Generează un raport cu frecvența comenzilor pe zile calendaristice și ore.
     *
     * @param filename Numele fișierului în care va fi salvat raportul
     */
    public void genereazaRaportFrecventaComenzi(String filename) {
        int[][] matrice = new int[31][24];

        for (Comanda comanda : comenzi) {
            int dayOfMonth = comanda.getOrderDateTime().getDayOfMonth() - 1;
            int hourOfDay = comanda.getOrderDateTime().getHour();
            matrice[dayOfMonth][hourOfDay]++;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getReportPath(filename)))) {
            writer.write("Raport Frecvență Comenzi pe Zilele Calendaristice și Ore\n");
            writer.write("========================================================\n\n");

            for (int zi = 0; zi < 31; zi++) {
                writer.write("Ziua " + (zi + 1) + ":\n");
                for (int ora = 0; ora < 24; ora++) {
                    if (matrice[zi][ora] > 0) {
                        writer.write(String.format("Ora %02d:00 - %d comenzi\n", ora, matrice[zi][ora]));
                    }
                }
                writer.write("---------------------\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}