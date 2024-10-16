package clustering;

import data.Data;
import data.InvalidSizeException;
import distance.ClusterDistance;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;

/**
 * Modella il processo di clustering.
 *
 * @author Nazz
 */
public class HierachicalClusterMiner implements Serializable {
	/** Dendrogramma */
	private final Dendrogram dendrogram;
	/** Percorso della directory di salvataggio e caricamento degli oggetti serializzati */
	private static final String DIRECTORY_PATH = "./saved/";


	/**
	 * Crea un'istanza di classe HierachicalClusterMiner con profondità depth.
	 *
	 * @param depth  profondità del dendrogramma
	 *
	 * @throws InvalidDepthException se la profondità è minore di 1
	 */
	public HierachicalClusterMiner(int depth) throws InvalidDepthException {
		dendrogram = new Dendrogram(depth);
	}

	/**
	 * Restituisce la profondità del dendrogramma.
	 *
	 * @return la profondità del dendrogramma
	 */
	public int getDepth() {
		return dendrogram.getDepth();
	}

	/**
	 * Calcola il clustering del dataset data.
	 *
	 * @param data  dataset su cui calcolare il clustering
	 * @param distance  interfaccia per il calcolo della distanza tra cluster
	 *
	 * @throws InvalidDepthException se la profondità del dendrogramma è minore del numero di esempi
	 * @throws InvalidSizeException se la dimensione del cluster è minore di 2
	 * @throws InvalidClustersNumberException se il numero di cluster è minore di 2
	 */
	public void mine(Data data, ClusterDistance distance) throws InvalidDepthException, InvalidSizeException, InvalidClustersNumberException {
		if (getDepth() > data.getNumberOfExample()) {
			throw new InvalidDepthException("Numero di esempi maggiore della profondità del dendrogramma! \n");
		}

		ClusterSet level0 = new ClusterSet(data.getNumberOfExample());
		for (int i = 0; i < data.getNumberOfExample(); i++) {
			Cluster c = new Cluster();
			c.addData(i);
			level0.add(c);
		}
		dendrogram.setClusterSet(level0, 0);
		for (int i = 1; i < getDepth(); i++) {
            ClusterSet nextLevel;
            try {
                nextLevel = dendrogram.getClusterSet(i-1).mergeClosestClusters(distance, data);
				dendrogram.setClusterSet(nextLevel, i);
			} catch (InvalidSizeException | InvalidClustersNumberException e) {
				getDepth();
                throw e;
            }
        }

	}

	/**
	 * Restituisce una rappresentazione testuale del dendrogramma.
	 *
	 * @return una stringa che rappresenta il dendrogramma
	 */
	public String toString() {
		return dendrogram.toString();
	}

	/**
	 * Restituisce una rappresentazione testuale del dendrogramma.
	 *
	 * @param  data dataset di esempi
	 *
	 * @throws InvalidDepthException se la profondità del dendrogramma è minore del numero di esempi
	 *
	 * @return una stringa che rappresenta il dendrogramma
	 */
	public String toString(Data data) throws InvalidDepthException {
		return dendrogram.toString(data);
	}

	/**
	 * Carica un'istanza di HierachicalClusterMiner da un file.
	 *
	 * @param  fileName nome del file da cui caricare l'istanza
	 *
	 * @return l'istanza caricata di HierachicalClusterMiner
	 *
	 * @throws IOException se si verifica un errore di input/output
	 * @throws ClassNotFoundException se la classe dell'oggetto serializzato non viene trovata
	 * @throws IllegalArgumentException se il nome del file è nullo o vuoto
	 * @throws FileNotFoundException se il file non viene trovato
	 */
	public static HierachicalClusterMiner loadHierachicalClusterMiner(String fileName) throws IOException, ClassNotFoundException, IllegalArgumentException {
		if (fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException("Il nome del file non può essere vuoto o nullo");
		}
		String filePath = DIRECTORY_PATH + fileName;
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("File non trovato: " + fileName);
		}
		try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(filePath))) {
			HierachicalClusterMiner loadedFile = (HierachicalClusterMiner) inStream.readObject();
			inStream.close();
			return loadedFile;
        } catch (FileNotFoundException e) {
			throw new FileNotFoundException("File non trovato: " + fileName);
		}

	}

	/**
	 * Salva l'istanza corrente di HierachicalClusterMiner su un file.
	 *
	 * @param  fileName nome del file su cui salvare l'istanza
	 *
	 * @throws FileNotFoundException se il file non viene trovato
	 * @throws IOException se si verifica un errore di input/output
	 * @throws IllegalArgumentException se il nome del file è nullo o vuoto
	 */
	public void salva(String fileName) throws FileNotFoundException, IOException, IllegalArgumentException {
		final String invalidRegex = "[<>:\"|?*]";
		final String validRegex = "^[\\w,\\s-]+\\.(txt|csv|json|xml|dat|bin|ser)$";

		if (fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException("Il nome del file non può essere nullo o vuoto.");
		}

		if (fileName.matches(invalidRegex)) {
			throw new IOException("Il nome del file contiene caratteri non validi.");
		}

		if (!fileName.matches(validRegex)) {
			throw new IOException("Estensione non valida. Il nome del file deve terminare con una estensione: .bin, .dat, .txt, .csv, .xml, .json, .ser");
		}

		fileName = fileName.replace("\\", File.separator).replace("/", File.separator);

		File directory = new File(DIRECTORY_PATH);
		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Impossibile creare la directory: " + DIRECTORY_PATH);
		}

		String filePath = DIRECTORY_PATH + fileName;
		File file = new File(filePath);

		if (file.exists()) {
			throw new FileAlreadyExistsException("File già esistente: " + fileName);
		}

		File parentDirectory = file.getParentFile();
		if (parentDirectory != null && !parentDirectory.exists() && !parentDirectory.mkdirs()) {
			throw new IOException("Impossibile creare la directory: " + parentDirectory.getAbsolutePath());
		}

		try (ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(file))) {
			outStream.writeObject(this);
        }
	}

}