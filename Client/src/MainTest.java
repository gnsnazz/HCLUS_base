import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Gestisce il menu per la scelta dell'operazione da eseguire e si occupa
 * di inviare le richieste al server e di ricevere le risposte.
 *
 * @author Nazz
 */
public class MainTest {
    /**
     * Stream di output per inviare le richieste al server.
     */
    private ObjectOutputStream out;
    /**
     * Stream di input per ricevere le risposte dal server.
     */
    private ObjectInputStream in;

    /**
     * Costruttore.
     * @param ip  indirizzo ip del server
     * @param port  porta del server
     *
     * @throws IOException se ci sono errori di I/O
     */
    public MainTest(String ip, int port) throws IOException {
        InetAddress addr = InetAddress.getByName(ip);   //ip
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, port);         //port
        System.out.println(socket);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Stampa il menu e legge le scelte dell'utente.
     *
     * @return la scelta dell'utente
     */
    private int menu(){
        int answer;
        System.out.println("Scegli una opzione");
        do{
            System.out.println("(1) Carica Dendrogramma da File");
            System.out.println("(2) Apprendi Dendrogramma da Database");
            System.out.print("Risposta:");
            answer= Keyboard.readInt();
        }
        while(answer <= 0 || answer > 2);
        return answer;
    }

    /**
     * Si occupa di inviare la richiesta al server per caricare i dati dal database.
     *
     * @throws IOException se ci sono errori di I/O
     * @throws ClassNotFoundException se ci sono errori di casting
     */
    private void loadDataOnServer() throws IOException, ClassNotFoundException {
        boolean flag = false;
        do {
            System.out.println("Nome tabella:");
            String tableName = Keyboard.readString();
            out.writeObject(0);
            out.writeObject(tableName);
            String risposta = (String)(in.readObject());
            if(risposta.equals("OK"))
                flag = true;
            else System.out.println(risposta);

        }while(!flag);
    }

    /**
     * Si occupa di inviare la richiesta al server per caricare il dendrogramma da file.
     *
     * @throws IOException se ci sono errori di I/O
     * @throws ClassNotFoundException se ci sono errori di casting
     */
    private void loadDendrogramFromFileOnServer() throws IOException, ClassNotFoundException {
        System.out.println("Inserire il nome dell'archivio (comprensivo di estensione):");
        String fileName = Keyboard.readString();

        out.writeObject(2);
        out.writeObject(fileName);
        String risposta = (String) (in.readObject());
        if(risposta.equals("OK"))
            System.out.println(in.readObject()); // stampa il dendrogramma che il server mi sta inviando
        else
            System.out.println(risposta); // stampo il messaggio di errore
    }

    /**
     * Si occupa di inviare la richiesta al server per apprendere il dendrogramma.
     *
     * @throws IOException se ci sono errori di I/O
     * @throws ClassNotFoundException se ci sono errori di casting
     */
    private void mineDendrogramOnServer() throws IOException, ClassNotFoundException {
        out.writeObject(1);
        System.out.println("Inserire la profondità del dendrogramma:");
        int depth = Keyboard.readInt();
        out.writeObject(depth);
        int dType = -1;
        do {
            System.out.println("Distanza: single-link (1), average-link (2):");
            dType = Keyboard.readInt();
        }while (dType <= 0 || dType > 2);
        out.writeObject(dType);

        String risposta = (String) (in.readObject());
        if(risposta.equals("OK")) {
            System.out.println(in.readObject());    // stampa del dendrogramma
            System.out.println("Inserire il nome dell'archivio (comprensivo di estensione):");
            String fileName = Keyboard.readString();
            out.writeObject(fileName);
            risposta = (String) in.readObject();
            System.out.println(risposta);
        }
        else
            System.out.println(risposta);
    }

    /**
     * Gestisce la comunicazione con il server.
     *
     * @param args  contiene l'indirizzo ip e la porta del server
     */
    public static void main(String[] args) {
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        MainTest main = null;
        try {
            main = new MainTest(ip, port);

            main.loadDataOnServer();
            int scelta = main.menu();
            if (scelta == 1)
                main.loadDendrogramFromFileOnServer();
            else
                main.mineDendrogramOnServer();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}