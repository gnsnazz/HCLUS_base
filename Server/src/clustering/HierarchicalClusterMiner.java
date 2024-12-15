package clustering;

import data.Data;
import data.InvalidSizeException;
import distance.ClusterDistance;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Modella il processo di clustering.
 *
 * @author Nazz
 */
public class HierarchicalClusterMiner implements Serializable {
	/** Dendrogramma */
	private final Dendrogram dendrogram;
	/** Percorso della directory di salvataggio e caricamento degli oggetti serializzati */
	private static final String DIRECTORY_PATH = "./saved/";


	/**
	 * Crea un'istanza di classe HierarchicalClusterMiner con profondità depth.
	 *
	 * @param depth  profondità del dendrogramma
	 *
	 * @throws InvalidDepthException se la profondità è minore di 1
	 */
	public HierarchicalClusterMiner(int depth) throws InvalidDepthException {
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
			throw new InvalidDepthException("Profondità del dendrogramma maggiore del numero degli esempi!\n");
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
	 * Carica un'istanza di HierarchicalClusterMiner da un file.
	 *
	 * @param  fileName nome del file da cui caricare l'istanza
	 *
	 * @return l'istanza caricata di HierarchicalClusterMiner
	 *
	 * @throws IOException se si verifica un errore di input/output
	 * @throws ClassNotFoundException se la classe dell'oggetto serializzato non viene trovata
	 * @throws IllegalArgumentException se il nome del file è nullo o vuoto
	 * @throws FileNotFoundException se il file non viene trovato
	 */
	public static HierarchicalClusterMiner loadHierarchicalClusterMiner(String fileName) throws IOException, ClassNotFoundException, IllegalArgumentException {
		if (fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException("Il nome del file non può essere vuoto o nullo");
		}
		String filePath = DIRECTORY_PATH + fileName;
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("File non trovato: " + fileName);
		}
		try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(filePath))) {
			HierarchicalClusterMiner loadedFile = (HierarchicalClusterMiner) inStream.readObject();
			inStream.close();
			return loadedFile;
        } catch (FileNotFoundException e) {
			throw new FileNotFoundException("File non trovato: " + fileName);
		}

	}

	/**
	 * Salva l'istanza corrente di HierarchicalClusterMiner su un file.
	 *
	 * @param  fileName nome del file su cui salvare l'istanza
	 *
	 * @throws FileNotFoundException se il file non viene trovato
	 * @throws IOException se si verifica un errore di input/output
	 * @throws IllegalArgumentException se il nome del file è nullo o vuoto
	 */
	public void salva(String fileName) throws FileNotFoundException, IOException, IllegalArgumentException {
		final String invalidRegex = "[<>:\"|?*\\\\/]";
		final String validRegex = "^[\\w,\\s-]+\\.(txt|csv|json|xml|dat|bin|ser)$";

		if (fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException("Il nome del file non può essere nullo o vuoto.");
		}

		Pattern pattern = Pattern.compile(invalidRegex);
		Matcher matcher = pattern.matcher(fileName);

		if (matcher.find()) {
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