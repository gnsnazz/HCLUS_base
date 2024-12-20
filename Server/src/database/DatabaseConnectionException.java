package database;

/**
 * Eccezione lanciata quando non è possibile stabilire una connessione al database.
 */
public class DatabaseConnectionException extends Exception {
    /**
     * Costruttore parametrizzato.
     *
     * @param msg  messaggio da visualizzare
     */
    DatabaseConnectionException(String msg) {
        super(msg);
    }

}