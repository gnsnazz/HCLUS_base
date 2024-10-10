import server.MultiServer;

/**
 * Classe main del Server.
 */
public class Main {

    /**
     * Punto di partenza dell'applicazione lato server.
     *
     * @param args  argomenti passati da terminale
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Nessun numero di porta inserito.");
            System.exit(1);
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
            if (port < 0 || port > 65535) {
                System.err.println("Numero di porta non valido: " + args[0]);
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.err.println("Numero di porta non valido: " + args[0]);
            System.exit(1);
            return;
        }

        System.out.println("Server avviato sulla porta " + port);
        MultiServer.instanceMultiServer(port);
    }

}