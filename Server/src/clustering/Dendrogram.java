package clustering;

import data.Data;
import java.io.Serializable;

/**
 * Modella un dendrogramma.
 */
class Dendrogram implements Serializable {
    /** Array di ClusterSet che modella il dendrogramma. */
    private ClusterSet[] tree;

    /**
     * Crea un vettore di dimensione depth con cui inizializza tree.
     *
     * @param depth  profondità del dendrogramma
     *
     * @throws InvalidDepthException se la profondità non è valida
     */
    Dendrogram(int depth) throws InvalidDepthException {
        if (depth <= 0) {
            throw new InvalidDepthException("Profondità non valida!\n");
        }
        tree = new ClusterSet[depth];
    }

    /**
     * Memorizza un ClusterSet in una posizione specifica del dendrogramma.
     *
     * @param c  clusterSet da memorizzare
     * @param level  livello del dendrogramma in cui inserire il clusterSet
     */
    void setClusterSet(ClusterSet c, int level) {
        tree[level] = c;
    }

    /**
     * Restituisce il cluster set al livello level di tree.
     *
     * @param level  livello del dendrogramma da restituire
     *
     * @return il ClusterSet al livello level di tree
     */
    ClusterSet getClusterSet(int level) {
        return tree[level];
    }

    /**
     * Restituisce la profondità del dendrogramma.
     *
     * @return la profondità del dendrogramma
     */
    int getDepth() {
        return tree.length;
    }

    /**
     * Restituisce una rappresentazione testuale del dendrogramma.
     *
     * @return una stringa che rappresenta il dendrogramma
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < tree.length; i++)
            s.append("level").append(i).append(":\n").append(tree[i]).append("\n");

        return s.toString();
    }

    /**
     * Restituisce una rappresentazione testuale del dendrogramma.
     *
     * @param  data dataset di esempi
     *
     * @return una stringa che rappresenta il dendrogramma
     */
    public String toString(Data data) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < tree.length; i++)
            s.append("level").append(i).append(":\n").append(tree[i].toString(data)).append("\n");

        return s.toString();
    }

}