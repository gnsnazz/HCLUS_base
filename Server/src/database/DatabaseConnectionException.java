package database;

/**
 * Eccezione lanciata quando non è possibile stabilire una connessione al database.
 */
public class DatabaseConnectionException extends Exception {
    /**
     * Costruttore parametrizzato.
     */
    DatabaseConnectionException(String msg) {
        super(msg);
    }
}