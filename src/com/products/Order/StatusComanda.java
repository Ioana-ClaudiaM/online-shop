package com.products.Order;

/**
 * Enumerația pentru stările posibile ale unei comenzi în sistemul de procesare.
 * Aceasta definește toate statusurile posibile prin care poate trece o comandă
 * de la creare până la finalizare.
 */
public enum StatusComanda {
    /**
     * Indică faptul că comanda a fost procesată și finalizată cu succes.
     */
    FINALIZATA,

    /**
     * Indică faptul că comanda este în curs de procesare și nu a fost încă finalizată.
     */
    IN_PROCESARE,
    /**
     * Indică faptul că comanda a fost expediată către client.
     */
    EXPEDIATA
}