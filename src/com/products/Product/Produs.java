package com.products.Product;

/**
 * Clasa Produs reprezintă un produs din cadrul unui magazin online.
 * Aceasta include informații despre preț, descriere, cantitatea disponibilă,
 * data adăugării, data expirării, rating, numărul de ratinguri și numărul de cumpărări.
 */
public class Produs {

    /** Prețul produsului */
    private double pret;

    /** Descrierea produsului */
    private String descriere;

    /** Cantitatea disponibilă în stoc pentru acest produs */
    private int cantitateDisponibila;

    /** Numele produsului */
    private String nume;

    /** Data adăugării produsului în stoc, în format "yyyy-MM-dd" */
    private String dataAdaugarii;

    /** Data expirării produsului, în format "yyyy-MM-dd" */
    private String dataExpirare;

    /** Ratingul mediu al produsului */
    private double rating;

    /** Numărul de ratinguri primite de acest produs */
    private int nrRatinguri;

    /** Numărul de ori când produsul a fost cumpărat */
    private int numarCumparari;

    /**
     * Constructor pentru crearea unui nou produs.
     *
     * @param pret Prețul produsului
     * @param descriere Descrierea produsului
     * @param cantitateDisponibila Cantitatea disponibilă în stoc
     * @param nume Numele produsului
     * @param dataAdaugarii Data adăugării produsului în stoc
     * @param dataExpirare Data expirării produsului
     * @param rating Ratingul inițial al produsului
     * @param numarCumparari Numărul de cumpărări ale produsului
     * @param nrRatinguri Numărul de ratinguri primite
     */
    public Produs(double pret, String descriere, int cantitateDisponibila, String nume,
                  String dataAdaugarii, String dataExpirare, double rating, int numarCumparari, int nrRatinguri) {
        this.pret = pret;
        this.descriere = descriere;
        this.cantitateDisponibila = cantitateDisponibila;
        this.nume = nume;
        this.dataAdaugarii = dataAdaugarii;
        this.dataExpirare = dataExpirare;
        this.rating = rating;
        this.numarCumparari = numarCumparari;
        this.nrRatinguri = nrRatinguri;
    }

    /** @return Prețul produsului */
    public double getPret() { return pret; }

    /**
     * Setează prețul produsului.
     * @param pret Noul preț al produsului
     */
    public void setPret(double pret) { this.pret = pret; }

    /** @return Descrierea produsului */
    public String getDescriere() { return descriere; }

    /**
     * Setează descrierea produsului.
     * @param descriere Noua descriere a produsului
     */
    public void setDescriere(String descriere) { this.descriere = descriere; }

    /** @return Cantitatea disponibilă a produsului */
    public int getCantitateDisponibila() { return cantitateDisponibila; }

    /**
     * Setează cantitatea disponibilă a produsului.
     * @param cantitateDisponibila Noua cantitate disponibilă
     */
    public void setCantitateDisponibila(int cantitateDisponibila) { this.cantitateDisponibila = cantitateDisponibila; }

    /** @return Numele produsului */
    public String getNume() { return nume; }

    /**
     * Setează numele produsului.
     * @param nume Noul nume al produsului
     */
    public void setNume(String nume) { this.nume = nume; }

    /** @return Data adăugării produsului */
    public String getDataAdaugarii() { return dataAdaugarii; }

    /** @return Data expirării produsului */
    public String getDataExpirare() { return dataExpirare; }

    /**
     * Setează data expirării produsului.
     * @param dataExpirare Noua dată a expirării produsului
     */
    public void setDataExpirare(String dataExpirare) { this.dataExpirare = dataExpirare; }

    /** @return Ratingul produsului */
    public double getRating() { return rating; }

    /**
     * Setează ratingul produsului.
     * @param rating Noul rating al produsului
     */
    public void setRating(double rating) { this.rating = rating; }

    /** @return Numărul de cumpărări ale produsului */
    public int getNumarCumparari() { return numarCumparari; }

    /** @return Numărul de ratinguri primite de produs */
    public int getNrRatinguri() { return nrRatinguri; }

    /**
     * Reprezentarea textuală a obiectului Produs, pentru afișare detaliată.
     * @return Informațiile despre produs, formatate ca șir de caractere
     */
    @Override
    public String toString() {
        return "Produs{" +
                "pret=" + pret +
                ", descriere='" + descriere + '\'' +
                ", cantitateDisponibila=" + cantitateDisponibila +
                ", nume='" + nume + '\'' +
                ", dataAdaugarii='" + dataAdaugarii + '\'' +
                ", dataExpirare='" + dataExpirare + '\'' +
                ", rating=" + rating +
                ", nrRatinguri=" + nrRatinguri +
                ", numarCumparari=" + numarCumparari +
                '}';
    }

    /**
     * Incrementează numărul de cumpărări ale produsului.
     * Această metodă este folosită pentru a actualiza statisticile vânzărilor.
     */
    public void incrementNumarCumparari() {
        this.numarCumparari++;
    }
    /**
     * Incrementează numărul de ratinguri primite de produs.
     * Această metodă este folosită pentru actualizarea ratingului și a numărului de evaluări.
     */
    public void incrementNumarRatinguri() {
        this.nrRatinguri++;
    }
}
