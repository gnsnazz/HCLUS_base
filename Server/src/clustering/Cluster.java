package clustering;

import data.Data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Set;

/**
 * Modella un cluster come la collezione delle posizioni occupate
 * dagli esempi raggruppati nel Cluster nel vettore data dell’oggetto
 * che modella il dataset su cui il clustering è calcolato(istanza di Data).
 *
 * @author Nazz
 */
public class Cluster implements Iterable<Integer>, Cloneable, Serializable {
	/**
	 * Set di interi che rappresenta gli indici degli esempi raggruppati nel cluster.
	 */
	private Set<Integer> clusteredData = new TreeSet<>();

	/**
	 * Aggiunge l'indice di posizione id al cluster.
	 *
	 * @param id  indice da aggiungere al cluster
	 */
	void addData(int id) {
        clusteredData.add(id);
	}

	/**
	 * Restituisce la dimensione del cluster.
	 *
	 * @return la dimensione del cluster
	 */
	public int getSize() {
		return clusteredData.size();
	}

	/**
	 * Restituisce un iterator per scorrere gli elementi del cluster.
	 *
	 * @return clusteredData.iterator() iterator per scorrere gli elementi del cluster
	 */
	public Iterator<Integer> iterator() {
		return clusteredData.iterator();
	}

	/**
	 * Crea una copia del cluster.
	 *
	 * @return la copia del cluster
	 *
	 * @throws CloneNotSupportedException se la clonazione non è supportata
	 */
	@Override
	public Cluster clone() throws CloneNotSupportedException {
		Cluster clone;
		try {
			clone = (Cluster) super.clone();
            //noinspection unchecked
            clone.clusteredData = (Set<Integer>) ((TreeSet<Integer>) this.clusteredData).clone();
		} catch (CloneNotSupportedException e) {
			throw new CloneNotSupportedException("Errore nella clonazione!");
		}

		return clone;
	}

	/**
	 * Crea un nuovo cluster che è la fusione del cluster corrente e del cluster c.
	 *
	 * @param c  cluster da unire al cluster corrente
	 *
	 * @return newCluster cluster che è la fusione del cluster corrente e del cluster c
	 */
	Cluster mergeCluster(Cluster c) {
		Cluster newCluster = new Cluster();
		Iterator<Integer> it1 = this.iterator();
		Iterator<Integer> it2 = c.iterator();

		while (it1.hasNext()) {
			newCluster.addData(it1.next());
		}
		while (it2.hasNext()) {
			newCluster.addData(it2.next());
		}

		return newCluster;
	}


	/**
	 * Restituisce una stringa contenente gli indici degli esempi raggruppati nel cluster.
	 *
	 * @return str stringa contenente gli indici degli esempi raggruppati nel cluster
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		Iterator<Integer> it = this.iterator();

		if (it.hasNext())
			str.append(it.next());

		while (it.hasNext())
			str.append(",").append(it.next());

		return str.toString();
	}

	/**
	 * Restituisce una stringa contenente gli esempi raggruppati nel cluster.
	 *
	 * @param data  oggetto di classe Data che modella il dataset su cui il clustering è calcolato
	 *
	 * @return str la stringa contenente gli esempi raggruppati nel cluster
	 */
	public String toString(Data data) {
		StringBuilder str = new StringBuilder();

        for (Integer clusteredDatum : clusteredData)
            str.append("<[").append(data.getExample(clusteredDatum)).append("]>");

		return str.toString();
	}

}