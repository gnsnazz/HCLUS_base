package clustering;

/**
 * Eccezione lanciata quando la profondità del dendrogramma è minore o uguale a 0.
 */
public class InvalidDepthException extends Exception {
    /**
     * Costruttore parametrizzato.
     */
    InvalidDepthException(String msg) {
        super(msg);
    }
}