package data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Modella le entità esempio inteso come vettore di valori reali.
 *
 * @author Nazz
 */
public class Example implements Iterable<Double> {
    /** Vettore di valori reali */
    private final List<Double> example;    //vettore di valori reali

    /**
     * Crea un'istanza di classe Example.
     */
    public Example(){
        example = new LinkedList<>();
    }

    /**
     * Restituisce un iterator per scorrere gli elementi di {@link #example}.
     *
     * @return example.iterator() iterator per scorrere gli elementi di {@link #example}
     */
    public Iterator<Double> iterator(){
        return example.iterator();
    }

    /**
     * Modifica {@link #example} inserendo v in coda.
     *
     * @param v  valore da inserire
     */
    public void add(Double v){
        example.add(v);
    }

    /**
     * Calcola la distanza euclidea tra l'istanza this.example e l'istanza newE.example.
     *
     * @param newE  istanza di Example con cui calcolare la distanza
     *
     * @return sum somma delle distanze tra i valori delle due istanze Example
     *
     * @throws InvalidSizeException se le due istanze hanno dimensioni diverse
     */
     public double distance(Example newE) throws InvalidSizeException{
         if(example.size() != newE.example.size())
             throw new InvalidSizeException("Gli esempi hanno dimensioni diverse!");

         double sum = 0.0;
         Iterator<Double> iterator1 = example.iterator();
         Iterator<Double> iterator2 = newE.iterator();

         while (iterator1.hasNext() && iterator2.hasNext()) {
             double diff = iterator1.next() - iterator2.next();
             sum += Math.pow(diff, 2);
         }

         return sum;
    }

    /**
     * Restituisce una stringa che rappresenta il contenuto di example.
     *
     * @return s stringa contenente i valori di example
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        Iterator<Double> iterator = iterator();

        if (iterator.hasNext())
            s.append(iterator.next());

        while (iterator.hasNext())
            s.append(",").append(iterator.next());

        return s.toString();
    }

}