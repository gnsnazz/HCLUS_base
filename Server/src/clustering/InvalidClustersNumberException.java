package clustering;

/**
 * Eccezione lanciata quando il numero di cluster è minore di 1.
 */
public class InvalidClustersNumberException extends Exception {
    /**
     * Costruttore parametrizzato.
     *
     * @param msg  messaggio da visualizzare
     */
    InvalidClustersNumberException(String msg) {
        super(msg);
    }

}