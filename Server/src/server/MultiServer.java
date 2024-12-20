package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gestisce la connessione al server.
 */
public class MultiServer {
    /** Porta server. */
    private final int PORT;
    /** Singleton. */
    private static MultiServer singleton = null;

    /**
     * Costruttore che inizializza la porta e invoca il metodo run.
     *
     * @param port  porta su cui il server è in ascolto.
     */
    private MultiServer(int port) {
        this.PORT = port;
        run();
    }

    /**
     * Metodo che serve per creare un istanza un nuovo Server, specificando
     * la porta in modo tale da rendere Singleton la classe Server.
     *
     * @param port  che indica la porta sulla quale avviare il server
     */
    public static void instanceMultiServer(int port){
        if(singleton == null)
            singleton = new MultiServer(port);
    }

    /**
     * Gestisce la comunicazione con il client ponendo in attesa le varie richieste.
     */
    private void run() {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            try (ss) {
                System.out.println("Server avviato: " + ss);
                while (true) {
                    Socket socket = ss.accept();
                    System.out.println("Connessione client: " + socket);
                    try {
                        new ServerOneClient(socket);
                    } catch (IOException e) {
                        System.out.println("Errore nella creazione del socket: " + socket);
                        socket.close();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Errore!");
        }
    }

}