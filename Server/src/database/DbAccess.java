package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce l'accesso al Database.
 */
public class DbAccess {
    /** Nome del driver. */
	private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    /** Nome del DBMS da utilizzare */
    private final String DBMS = "jdbc:mysql";
    /** Indirizzo del server. */
    private final String SERVER = "localhost";
    /** Nome del database. */
    private final String DATABASE = "MapDB";
    /** Porta del server. */
    private final int PORT = 3306;
    /** Nome utente per l'accesso al database. */
    private final String USER_ID = "MapUser";
    /** Password per l'accesso al database. */
    private final String PASSWORD = "map";
    /** Connessione al database. */
    private Connection conn;

    /**
     * Inizializza la connessione al database.
     *
     * @throws DatabaseConnectionException se la connessione al database fallisce
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch(ClassNotFoundException e) {
            System.out.println("[!] Driver not found: " + e.getMessage());
            throw new DatabaseConnectionException(e.toString());
        }
        String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
                + "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";

        try {
            conn = DriverManager.getConnection(connectionString);
        } catch(SQLException e) {
            throw new DatabaseConnectionException(e.toString());
        }
    }

    /**
     * Restituisce la connessione al database.
     *
     * @return connessione al database
     *
     * @throws DatabaseConnectionException se la connessione al database fallisce
     */
    public Connection getConnection() throws DatabaseConnectionException {
        this.initConnection();
        return conn;
    }

    /**
     * Chiude la connessione al database.
     *
     * @throws SQLException se si verifica un errore durante la chiusura della connessione
     */
    public void closeConnection() throws SQLException {
        conn.close();
    }

}