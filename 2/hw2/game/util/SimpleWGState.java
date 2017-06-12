package hw2.game.util;

import hw2.game.Action;
import hw2.game.WGState;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Implementazione dell'interfaccia {@link hw2.game.WGState}. */

 public class SimpleWGState implements WGState {
    /**
     * Crea un WGState con le caratteristiche specificate.
     *
     * @param a azione
     * @param s stringa di stato
     */
    public SimpleWGState(Action a, String s) {
        action=a;
        state=s;
    }
    /**
     * @return l'azione specificata nel costruttore
     */
    @Override
    public Action action() {return action;}

    /**
     * @return La stringa di stato specificata nel costruttore
     */
    @Override
    public String state() {
        return state;
    }

    private final Action action;
    private final String state;
}
