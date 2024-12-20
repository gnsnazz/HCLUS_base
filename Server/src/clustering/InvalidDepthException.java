package clustering;

/**
 * Eccezione lanciata quando la profondità del dendrogramma è minore o uguale a 0.
 */
public class InvalidDepthException extends Exception {
    /**
     * Costruttore parametrizzato.
     *
     * @param msg  messaggio da visualizzare
     */
    InvalidDepthException(String msg) {
        super(msg);
    }

}