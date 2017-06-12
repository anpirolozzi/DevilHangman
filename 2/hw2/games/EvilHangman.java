package hw2.games;

import hw2.game.Param;
import hw2.game.Action;
import hw2.game.WGState;
import hw2.game.WGame;
import hw2.game.util.Utils;
import hw2.game.util.SimpleWGState;
import hw2.game.util.SimpleParam;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

/**  <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Un oggetto EvilHangman realizza un gioco che è una variante dell'Impiccato che
 * chiamiamo L'Impiccato Diabolico. A differenza dell'Impiccato tradizionale la
 * variante Diabolica non sceglie una singola parola segreta ma all'inizio fissa
 * solamente la lunghezza della parola. Il gioco per il giocatore procede
 * (apparentemente) come se giocasse l'Impiccato tradizionale. Ma dietro le quinte
 * il computer fa il possibile per rendere molto più difficile indovinare la parola
 * mantenendo però la coerenza con le risposte date. Per fare ciò, all'inizio il
 * computer invece di scegliere una singola parola parte con tutte le parole della
 * lunghezza fissata e questo lo chiamiamo l'insieme PW delle parole possibili.
 * Ogni volta che il giocatore chiede se un certo carattere x è presente, il computer
 * partiziona PW nei sottoinsiemi rispetto alle posizioni del carattere x. Ad esempio
 * se PW = ["pizza","torre","porta","pazza","perle","torte"] e x = 'a', allora la
 * partizione e' nei tre sottoinsiemi:
 * [["perle","torte","torre"], ["pizza","porta"], ["pazza"]]. Ogni sottoinsieme
 * consiste di esattamente quelle parole in PW che hanno il carattere x nelle stesse
 * posizioni o non è presente in nessuna di esse. A questo punto il computer
 * continua il gioco impostando PW uguale al sottoinsieme di cardinalità maggiore e a
 * parità di cardinalità quello con il minor numero di occorrenze del carattere x.
 * Nell'esempio continuerebbe quindi con PW = ["perle","torte","torre"]. In questo
 * modo il computer cerca di rimanere con il sottoinsieme più grande che è
 * compatibile con le informazioni che ha dato finora sulla parola (o le parole).
 * Il giocatore riesce a vincere solamente se quando tenta di indovinare la parola
 * l'insieme PW contiene una sola parola. Infatti anche se la parola tentata dal
 * giocatore è contenuta in PW ma PW ha almeno due parole, il computer potrà
 * asserire che la parola segreta era un'altra in PW. Per facilitare il testing
 * il computer deve dichiarare la parola che viene prima nell'ordimanete naturale
 * tra quelle in PW eccettuata la parola tentata dal giocatore. Ecco un esempio di
 * sessione di gioco come potrebbe essere prodotta dal gestore della UI testuale
 * {@link hw2.game.ui.TextUIWGames} fornendogli un oggetto EvilHangman:
 <pre>
 1. L'IMPICCATO DIABOLICO
 2. Quit
 Digita un intero da 1 a 2: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Lunghezza parola (5)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 2
 Lunghezza parola: 7
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Lunghezza parola (7)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 3
 Per interrompere il gioco digita $stop
 L'IMPICCATO DIABOLICO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: _ _ _ _ _ _ _
 Mancanti: a
 Indovina:
 e
 Parola: _ _ _ _ _ _ _
 Mancanti: a, e
 Indovina:
 i
 Parola: _ _ _ _ i _ _
 Mancanti: a, e
 Indovina:
 o
 Parola: _ o _ _ i _ o
 Mancanti: a, e
 Indovina:
 t
 Parola: _ o _ _ i _ o
 Mancanti: a, e, t
 Indovina:
 r
 Parola: _ o _ _ i _ o
 Mancanti: a, e, t, r
 Indovina:
 s
 Parola: _ o _ _ i _ o
 Mancanti: a, e, t, r, s
 Indovina:
 b
 Parola: _ o _ _ i _ o
 Mancanti: a, e, t, r, s, b (Hai esaurito il numero di errori)
 Indovina:
 n
 Parola: _ o _ _ i n o
 Mancanti: a, e, t, r, s, b (Hai esaurito il numero di errori)
 Indovina:
 c
 Non hai indovinato, la parola era: mozzino

 1. L'IMPICCATO DIABOLICO
 2. Quit
 Digita un intero da 1 a 2: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Lunghezza parola (7)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 1
 Massimo numero di errori: 5
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (5)
 2. Lunghezza parola (7)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 9
 Errore, input ammessi: [1, 2, 3]
 Digita un intero da 1 a 3: 2
 Lunghezza parola: 9
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (5)
 2. Lunghezza parola (9)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 3
 Per interrompere il gioco digita $stop
 L'IMPICCATO DIABOLICO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: _ _ _ _ _ _ _ _ _
 Mancanti: a
 Indovina:
 e
 Parola: _ _ _ _ _ _ _ _ _
 Mancanti: a, e
 Indovina:
 i
 Parola: _ _ _ _ _ _ i _ _
 Mancanti: a, e
 Indovina:
 o
 Parola: _ _ _ _ _ _ i _ o
 Mancanti: a, e
 Indovina:
 u
 Parola: _ _ u _ _ _ i _ o
 Mancanti: a, e
 Indovina:
 s
 Parola: _ _ u _ _ _ i _ o
 Mancanti: a, e, s
 Indovina:
 r
 Parola: _ r u _ _ _ i _ o
 Mancanti: a, e, s
 Indovina:
 t
 Parola: t r u _ _ _ i _ o
 Mancanti: a, e, s
 Indovina:
 n
 Parola: t r u _ _ _ i n o
 Mancanti: a, e, s
 Indovina:
 c
 Parola: t r u c c _ i n o
 Mancanti: a, e, s
 Indovina:
 h
 HAI INDOVINATO: trucchino

 1. L'IMPICCATO DIABOLICO
 2. Quit
 Digita un intero da 1 a 2: 2
 Fine
 </pre>
 */
public class EvilHangman implements WGame {
    /** Crea un {@link hw2.game.WGame} per il gioco dell'Impiccato Diabolico. Si
     * assume che il file di percorso p contenga una parola per linea. Le parole che
     * saranno usate per giocare sono quelle nel file di lunghezza almeno minWL.
     * Il gioco ha due paramteri il massimo numero di errori consentiti con valori
     * [1,2,3,4,5,6,7] e default 6 e la lunghezza della parola con valori possibili
     * le lunghezze delle parole nel file (di lunghezza almeno minWL) e valore di
     * default la lunghezza minima.
     * @param p  percorso di un file di parole
     * @param cs  codifica dei caratteri del file di parole
     * @param minWL  lunghezza minima delle parole
     * @throws java.io.IOException se si verifica un errore leggendo il file di parole */
    public EvilHangman(Path p, Charset cs, int minWL) throws IOException {
        Wordlist = Utils.lines(p, cs, s -> s.length() < minWL);

        String prompt="Massimo numero di errori";
        int index=5;
        Integer[]values=new Integer[]{1,2,3,4,5,6,7};
        Param<?>par = new SimpleParam<Object>(prompt,index,(Object[])values);
        paramList.add(par);

        String prompt1="Lunghezza parola";
        String[]values1=new String[]{"a","b","c"};
        int index1=1;
        Param<?>par1 = new SimpleParam<Object>(prompt1,index1,(Object[])values1);
        paramList.add(par1);
    }

    /** @return il nome del gioco, cioè "L'IMPICCATO DIABOLICO" */
    @Override
    public String name() { return name; }

    /** @return le informazioni sul gioco, cioè
    <pre>
    Trova la parola indovinando una lettera alla volta
    Se credi di aver indovinato immetti l'intera parola
    </pre> */
    @Override
    public String info() { return info; }

    /** Ritorna la lista dei parametri che per EvilHangman sono due parametri
     * con le seguenti caratteristiche
     <pre>
     prompt: "Massimo numero di errori"
     values: [1,2,3,4,5,6,7]
     valore di default: 6

     prompt: "Lunghezza parola"
     values: le lunghezze delle parole specificate nel costruttore
     valore di dafault: la minima lunghezza
     </pre>
     * @return la lista dei parametri */
    @Override
    public List<Param<?>> params() { return paramList; }

    /** Inizia un nuovo gioco con una lunghezza specificata dal valore del
     * corrispondente parametro. La stringa dello stato iniziale deve consistere
     * nel nome del gioco, la stringa di info e poi
     * <pre>
     Parola: _ _ _ _ _ _ _
     Mancanti:
     Indovina:
     * </pre>
     * Dove il numero di underscore '_' è uguale alla lunghezza della parola da
     * indovinare. Ovviamente l'azione del WGState deve essere
     * {@link hw2.game.Action#CONTINUE}. Il numero di errori è inizializzato con il
     * valore del corrispondente parametro.
     * @return lo stato iniziale di un nuovo gioco di EvilHangman.
     * @throws java.lang.IllegalStateException se c'è già un gioco attivo */
    @Override
    public WGState newGame() {
        throw new UnsupportedOperationException();
    }

    /** Accetta la stringa s del giocatore. Prima di tutto riduce s in minuscolo.
     * Se la stringa è vuota o contiene caratteri non alfabetici, la ignora e
     * ritorna lo stesso stato precedente. Se consiste di un solo carattere, esegue
     * la procedura spiegata nel javdoc della classe per aggiornare PW. Se è il
     * carattere è presente nelle parole di PW aggiornato, evidenzia le posizioni in
     * cui appare sostituendo gli underscore (come nell'esempio mostrato sopra). Se
     * non è presente e non era già stato digitato, lo aggiunge ai caratteri mancanti
     * e decrementa gli errori consentiti. Se gli errori consentiti sono diventati
     * zero, lo comunica nella stringa di stato (come nell'esempio mostrato sopra).
     * Se gli errori consentiti erano zero già prima di commettere l'ultimo errore il
     * gioco termina comunicando l'azione {@link hw2.game.Action#END}. Se consiste di
     * 2 o più caratteri, controlla se la parola è in PW se è l'unica parola in PW il
     * giovcatore ha indovinato, altrimenti o non è in PW o PW ha almeno due parole,
     * il giocatore ha perso e il computer comunica che la parola era quella in PW
     * eccettuata quela tentata dal giocatore che viene prima nell'ordinamento
     * naturale delle stringhe. In entrambi i casi comunica l'esito del gioco nella
     * stringa di stato (come nell'esempio mostrato sopra), l'azione è
     * {@link hw2.game.Action#END} e il gioco è terminato. In tutti gli altri casi
     * l'azione è {@link hw2.game.Action#CONTINUE}.
     * @param s  la mossa del giocatore
     * @return il nuovo stato di gioco
     * @throws java.lang.IllegalStateException se non c'è un gioco attivo */
    @Override
    public WGState player(String s) {
        throw new UnsupportedOperationException();
    }

    /** Termina prematuramente l'eventuale gioco attivo */
    @Override
    public void abort() { active=false; }

    private static boolean active = false;

    private ArrayList<Param<?>> paramList=new ArrayList<>(); //lista parametri
    private List<String> Wordlist= new ArrayList<>();//lista parole filtrate dal costruttore

    final private static String name = "L'IMPICCATO DIABOLICO";
    final private static String info = "Trova la parola indovinando una lettera alla volta\n" +
            "Se credi di aver indovinato immetti l'intera parola";


}

