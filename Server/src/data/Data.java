package data;

import database.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Modella un insieme di esempi.
 *
 * @author Nazz
 */
public class Data {
    /** Lista di esempi */
    private final List<Example> data = new ArrayList<>();   //rappresenta il dataset

    /**
     * Crea un'istanza di classe Data leggendo i suoi esempi dalla tabella con nome tableName nel database.
     *
     * @param tableName  nome della tabella nel database
     *
     * @throws NoDataException se la tabella è vuota
     */
    public Data(String tableName) throws NoDataException {
        DbAccess dbAccess = new DbAccess();
        try {
            TableData tableData = new TableData(dbAccess);
            List<Example> examples = tableData.getDistinctTransazioni(tableName);

            this.data.addAll(examples);

        } catch (DatabaseConnectionException e) {
            throw new NoDataException("Errore di connessione al database: " + e.getMessage() + "\n");
        } catch (EmptySetException e) {
            throw new NoDataException("La tabella " + tableName + " è vuota: " + e.getMessage() + "\n");
        } catch (MissingNumberException e) {
            throw new NoDataException("Errore durante l'elaborazione dei dati: " + e.getMessage() + "\n");
        } catch (SQLException e) {
            throw new NoDataException("Errore SQL durante il recupero dei dati dalla tabella: " + e.getMessage() + "\n");
        }
    }

    /**
     * Restituisce il numero degli esempi memorizzati in {@link #data}.
     *
     * @return numero di esempi nel dataset
     */
    public int getNumberOfExample() {
        return data.size();
    }

    /**
     * Restituisce l'elemento dell'istanza data in posizione exampleIndex.
     *
     * @param exampleIndex  indice dell'elemento da restituire
     *
     * @return elemento in posizione exampleIndex
     */
    public Example getExample(int exampleIndex) {
        return data.get(exampleIndex);
    }

    /**
     * Restituisce un iterator per scorrere gli elementi di {@link #data}.
     *
     * @return iterator per scorrere gli elementi di {@link #data}
     */
    public Iterator<Example> iterator() {
        return data.iterator();
    }

    /**
     * Crea una stringa in cui memorizza gli esempi memorizzati in {@link #data}.
     *
     * @return stringa con gli esempi in {@link #data}
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        Iterator<Example> iterator = iterator();
        int count = 0;

        while (iterator.hasNext()) {
            s.append(count++).append(":[").append(iterator.next().toString()).append("]\n");
        }

        return s.toString();
    }

}