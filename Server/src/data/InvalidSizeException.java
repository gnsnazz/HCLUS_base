package data;

/**
 * Eccezione lanciata quando la dimensione di un insieme di cluster è minore di 1.
 */
public class InvalidSizeException extends Exception {
    /** Costruttore parametrizzato.
     *
     * @param message  messaggio da visualizzare
     */
    public InvalidSizeException(String message) {
        super(message);
    }
}
