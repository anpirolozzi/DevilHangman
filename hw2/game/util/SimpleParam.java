package hw2.game.util;

import hw2.game.Param;

import java.util.*;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Implementazione dell'interfaccia {@link hw2.game.Param}.
 * @param <T>  il tipo dei valori del parametro */
public class SimpleParam<T> implements Param<T> {
    /** Crea un Param con le caratteristiche specificate.
     * @param p  stringa di prompt
     * @param i  indice del valore iniziale relativo all'array vals
     * @param vals  array dei possibili valori del parametro */
    public SimpleParam(String p, int i, T...vals) {
        prompt=p;
        index=i;
        values=Arrays.asList(vals);
    }

    /** @return la stringa di prompt specificata nel costruttore */
    @Override
    public String prompt() {return prompt;}

    /** @return la lista dei possibili valori specificati nel costruttore */
    @Override
    public List<T> values() {return values;}

    /** Imposta il valore del parametro tramite l'indice nella lista dei valori
     * ritornata dal metodo {@link hw2.game.util.SimpleParam#values()}.
     * @param i  indice del valore nella lista dei valori
     * @throws IllegalStateException se l'indice è fuori range */
    @Override
    public void set(int i) {
        index=i;
        if((index<0)||(index>values.size())){throw new IllegalStateException();}
    }

    /** @return il valore del parametro */
    @Override
    public T get() {return values.get(index);}

    private int index;
    final private String prompt;
    final private List<T> values;
}

