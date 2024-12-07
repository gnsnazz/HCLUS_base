package server;

import data.Data;
import data.InvalidSizeException;
import data.NoDataException;
import clustering.HierarchicalClusterMiner;
import clustering.InvalidClustersNumberException;
import clustering.InvalidDepthException;

import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;

/**
 * Gestisce le connessioni con i client.
 *
 * @author Nazz
 */
public class ServerOneClient extends Thread {
    /**
     * Socket per la comunicazione con il client.
     */
    private Socket clientSocket;
    /**
     * Stream di input per la comunicazione con il client.
     */
    private ObjectInputStream in;
    /**
     * Stream di output per la comunicazione con il client.
     */
    private ObjectOutputStream out;
    /**
     * Dataset per memorizzare i dati caricati.
     */
    private Data data;

    /**
     * Costruttore per il gestore client.
     *
     * @param socket  socket per la comunicazione con il client
     *
     * @throws IOException se si verifica un errore di I/O
     */
    public ServerOneClient(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.start();
    }

    /**
     * Gestisce le richieste del client.
     */
    @Override
    public void run() {
        try {
            while (true) {
                int request = (int) in.readObject();

                switch (request) {
                    case 0:
                        // Carica dati dal database
                        handleLoadData();
                        break;
                    case 1:
                        // Esegue clustering
                        handleClustering();
                        break;
                    case 2:
                        // Carica il dendrogram da file
                        handleLoadDendrogramFromFile();
                        break;
                    default:
                        out.writeObject("Tipo di richiesta non valido.");
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Disconnessione client: " + clientSocket);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                clientSocket.close();
                out.close();
                in.close();
            } catch (IOException e) {
                System.err.println("Errore nella chiusura del socket");
            }
        }
    }

    /**
     * Gestisce il caricamento dei dati dal database.
     *
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe non è trovata
     */
    private void handleLoadData() throws IOException, ClassNotFoundException {
        String tableName = (String) in.readObject();
        try {
            this.data = new Data(tableName);
            out.writeObject("OK");
        } catch (NoDataException e) {
            out.writeObject(e.getMessage());
        }
    }

    /**
     * Gestisce l'operazione di clustering.
     *
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe non è trovata
     */
    private void handleClustering() throws IOException, ClassNotFoundException {
        if (data == null) {
            out.writeObject("Dati non caricati");
            return;
        }

        int depth = (int) in.readObject();
        int distanceType = (int) in.readObject();

        try {
            HierarchicalClusterMiner clustering = new HierarchicalClusterMiner(depth);
            ClusterDistance distance = distanceType == 1 ? new SingleLinkDistance() : new AverageLinkDistance();

            clustering.mine(data, distance);

            out.writeObject("OK");
            out.writeObject(clustering.toString(data));

            String fileName = (String) in.readObject();

            try {
                clustering.salva(fileName);
                out.writeObject("Dendrogramma salvato correttamente.");
            } catch (FileAlreadyExistsException e) {
                out.writeObject("Errore. Il file " + fileName + " esiste già.");
            } catch (IOException e) {
                out.writeObject("Errore durante il salvataggio del dendrogramma: " + e.getMessage());
            }
        } catch (InvalidSizeException | InvalidClustersNumberException | InvalidDepthException | IllegalArgumentException e) {
            out.writeObject(e.getMessage());
        }
    }


    /**
     * Gestisce il caricamento del dendrogram da un file.
     *
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe non è trovata
     */
    private void handleLoadDendrogramFromFile() throws IOException, ClassNotFoundException {
        String fileName = (String) in.readObject();
        try {
            HierarchicalClusterMiner clustering = HierarchicalClusterMiner.loadHierarchicalClusterMiner(fileName);

            if (data == null) {
                out.writeObject("Dati non caricati.");
                return;
            }

            if (clustering.getDepth() > data.getNumberOfExample()) {
                out.writeObject("Profondità del dendrogramma maggiore del numero degli esempi!");
            } else {
                out.writeObject("OK");
                out.writeObject(clustering.toString(data));
            }
        } catch (IOException | ClassNotFoundException | InvalidDepthException e) {
            out.writeObject(e.getMessage());
        }
    }

}