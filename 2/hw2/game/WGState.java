package hw2.game;
import hw2.game.Action;


/** Un WGState rappresenta le informazioni relative allo stato durante una partita
 * di un gioco {@link hw2.game.WGame} e che sono comunicate al gestore della UI
 * (Interfaccia Utente) */
public interface WGState {
    /** @return l'azione che il gestore del gioco chiede di compiere al gestore
     * della UI. */
    Action action();

    /** @return una stringa che rappresenta l'attuale stato della partita e che il
     * gestore della UI dovrebbe mostrare al giocatore. */
    String state();
}