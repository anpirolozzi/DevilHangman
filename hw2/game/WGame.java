package hw2.game;

import java.util.List;
import hw2.game.Param;
import hw2.game.WGState;

/** Un WGame è un gioco basato sulle parole (un word game o word puzzle), come ad
 * esempio Hangman. Un WGame permette di giocare più partite (ma solamente una alla
 * volta). */
//a qst ce da implementare il gioco.
 public interface WGame {
    /** @return il nome del gioco */
    String name();

    /** @return informazione sul gioco o su come si gioca */
    String info();

    /** Ritorna la lista degli eventuali parametri del gioco. Se il gioco non ha
     * parametri, ritorna la lista vuota. La lista ritornata non deve essere
     * modificabile e deve sempre essere la stessa.
     * @return la lista degli eventuali parametri del gioco */
    List<Param<?>> params();

    /** Inizia un nuovo gioco (partita) e ritorna lo stato iniziale.
     * @return lo stato iniziale dela nuova partita
     * @throws java.lang.IllegalStateException se c'è già una partita attiva */
    WGState newGame();

    /** Prende in input la mossa s del giocatore e risponde con il prosimo stato
     * della partita.
     * @param s  la mossa del giocatore
     * @return  lo stato della partita dopo la mossa del giocatore
     * @throws java.lang.IllegalStateException se non c'è una partita attiva */
    WGState player(String s);

    /** Termina prematuramente l'eventuale partita attualmente attiva */
    void abort();
}
