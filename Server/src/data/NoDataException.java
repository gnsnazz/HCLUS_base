package data;

/**
 * Eccezione lanciata quando non sono presenti dati.
 */
public class NoDataException extends Exception {
    /** Costruttore parametrizzato.
     *
     * @param message  messaggio da visualizzare
     */
    public NoDataException(String message) {
        super(message);
    }
}
