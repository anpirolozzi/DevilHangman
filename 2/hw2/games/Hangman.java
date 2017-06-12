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
import java.util.ArrayList;
import java.util.List;


/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Un oggetto Hangman realizza un gioco conosciuto come L'Impiccato (in inglese
 * Hangman). Il computer sceglie una parola segreta che il giocatore deve
 * indovinare e mostra al giocatore solamente la sua lunghezza. Il giocatore
 * può chiedere al computer se la parola contiene una lettera. Se è presente nella
 * parola, il computer mostra le posizioni in cui appare, se invece non è presente,
 * il giocatore ha commesso un errore. Il gioco termina o quando il giocatore
 * indovina la parola o quando commette un errore in più rispetto a quelli
 * ammessi. Ad esempio ecco una sessione di gioco come potrebbe essere prodotta
 * dal gestore della UI testuale {@link hw2.game.ui.TextUIWGames} fornendogli un
 * oggetto Hangman:
 <pre>
 1. L'IMPICCATO
 2. Quit
 Digita un intero da 1 a 2: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 2
 Per interrompere il gioco digita $stop
 L'IMPICCATO
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
 Parola: _ _ _ _ _ _ e
 Mancanti: a
 Indovina:
 i
 Parola: _ _ _ _ i _ e
 Mancanti: a
 Indovina:
 o
 Parola: _ o _ _ i _ e
 Mancanti: a
 Indovina:
 r
 Parola: _ o r _ i r e
 Mancanti: a
 Indovina:
 n
 Parola: _ o r n i r e
 Mancanti: a
 Indovina:
 t
 Parola: _ o r n i r e
 Mancanti: a, t
 Indovina:
 f
 HAI INDOVINATO: fornire

 1. L'IMPICCATO
 2. Quit
 Digita un intero da 1 a 2: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 1
 Massimo numero di errori: 2
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (2)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 2
 Per interrompere il gioco digita $stop
 L'IMPICCATO
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
 Parola: _  e _ _ e _ e
 Mancanti: a
 Indovina:
 r
 Parola: _ e _ _ e _ e
 Mancanti: a, r (Hai esaurito il numero di errori)
 Indovina:
 t
 Non hai indovinato, la parola era: sebbene

 1. L'IMPICCATO
 2. Quit
 Digita un intero da 1 a 2: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (2)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 2
 Per interrompere il gioco digita $stop
 L'IMPICCATO
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
 Mancanti: a, e (Hai esaurito il numero di errori)
 Indovina:
 i
 Parola: _ _ _ _ i _ i
 Mancanti: a, e (Hai esaurito il numero di errori)
 Indovina:
 o
 Parola: _ o _ _ i _ i
 Mancanti: a, e (Hai esaurito il numero di errori)
 Indovina:
 fossili
 Non hai indovinato, la parola era: bottini

 1. L'IMPICCATO
 2. Quit
 Digita un intero da 1 a 2: 2
 Fine
 </pre>
 */
public class Hangman implements WGame {
    /**
     * Crea un {@link hw2.game.WGame} per il gioco dell'Impiccato. Si assume che
     * il file di percorso p contenga una parola per linea. Le parole che saranno
     * usate per giocare sono quelle nel file di lunghezza almeno minWL.
     * Il gioco ha un solo paramtero che è il massimo numero di errori consentiti.
     * I valori del parametro sono [1,2,3,4,5,6,7], quello di default è 6.
     *
     * @param p     percorso di un file di parole
     * @param cs    codifica dei caratteri del file di parole
     * @param minWL lunghezza minima delle parole
     * @throws IOException se si verifica un errore leggendo il file di parole
     */
    public Hangman(Path p, Charset cs, int minWL) throws IOException {
        Wordlist = Utils.lines(p, cs, s -> s.length() < minWL);

        String prompt="Massimo numero di errori";
        int index=5;
        Integer[]values=new Integer[]{1,2,3,4,5,6,7};
        Param<?>par = new SimpleParam<Object>(prompt,index,(Object[])values);
        paramList.add(par);
    }

    /**
     * @return il nome del gioco, cioè "L'IMPICCATO"
     */
    @Override
    public String name() {return name;}

    /**
     * @return le informazioni sul gioco, cioè
     * <pre>
     * Trova la parola indovinando una lettera alla volta
     * Se credi di aver indovinato immetti l'intera parola
     * </pre>
     */
    @Override
    public String info() {return info;}

    /**
     * Ritorna la lista dei parametri che per Hangman ha un solo parametro con le
     * seguenti caratteristiche:
     * <pre>
     * prompt: "Massimo numero di errori"
     * values: [1,2,3,4,5,6,7]
     * valore di default: 6
     * </pre>
     *
     * @return la lista dei parametri (con un solo parametro)
     */
    @Override
    public List<Param<?>> params() {return paramList;}

    /**
     * Inizia un nuovo gioco scegliendo una parola random dalla lista di parole
     * specificate nel costruttore.
     * ATTENZIONE La scelta della parola deve essere effettuata tramite il metodo
     * {@link hw2.game.util.Utils#choose(java.util.Collection)}.
     * La stringa dello stato iniziale deve consistere nel nome del gioco, la
     * stringa di info e poi
     * <pre>
     * Parola: _ _ _ _ _ _ _
     * Mancanti:
     * Indovina:
     * </pre>
     * Dove il numero di underscore '_' è uguale alla lunghezza della parola da
     * indovinare. Ovviamente l'azione del WGState deve essere
     * {@link hw2.game.Action#CONTINUE}. Il numero di errori consentito è
     * inizializzato al valore del corrispondente parametro.
     *
     * @return lo stato iniziale di un nuovo gioco di Hangman.
     * @throws java.lang.IllegalStateException se c'è già un gioco attivo
     */
    @Override
    public WGState newGame() {
        if (active) {throw new IllegalStateException();}
        active = true;//il gioco ora e attivo
        found = false;//dobbiamo quindi trovare ancora la parola
        start = true;//stiamo quindi allo stato iniziale di gioco
        GuessedLett.clear();//azzero lettere indovinate
        WrongLett.clear();//azzero lettere sbagliate
        soluzione = Utils.choose(Wordlist);//scelgo la parola
        parola="";
        for(int x=0;x<soluzione.length();x++){parola+="_";}
        life=Integer.parseInt(String.valueOf(params().get(0).get()));
        return (state());
    }

    /**
     * Accetta la stringa s del giocatore. Prima di tutto riduce s in minuscolo.
     * Se la stringa è vuota o contiene caratteri non alfabetici, la ignora e
     * ritorna lo stesso stato precedente. Se consiste di un solo carattere,
     * controlla se è presente nella parola segreta. Se è presente evidenzia le
     * posizioni in cui appare sostituendo gli underscore (come nell'esempio
     * mostrato sopra). Se non è presente e non era già stato digitato, lo aggiunge
     * ai caratteri mancanti e decrementa gli errori consentiti. Se gli errori
     * consentiti sono diventati zero, lo comunica nella stringa di stato (come
     * nell'esempio mostrato sopra). Se gli errori consentiti erano zero già prima di
     * commettere l'ultimo errore il gioco termina comunicando l'azione
     * {@link hw2.game.Action#END}. Se consiste di 2 o più caratteri, confronta la
     * stringa fornita con la parola segreta e comunica l'esito del gioco nella
     * stringa di stato (come nell'esempio mostrato sopra), l'azione è
     * {@link hw2.game.Action#END} e il gioco è terminato. In tutti gli altri casi
     * l'azione è {@link hw2.game.Action#CONTINUE}.
     *
     * @param s la mossa del giocatore
     * @return il nuovo stato di gioco
     * @throws java.lang.IllegalStateException se non c'è un gioco attivo
     */
    @Override
    public WGState player(String s) {
        if (!active) {throw new IllegalStateException();}
        s = s.toLowerCase();
        if (!Utils.isAlphaLowercase(s)) {return state();}//ignoro i caratteri non idonei
        if (s.length() == 1) {//cerca lettera
            /*se presente in indovinate o sbagliate non fai nulla*/
            for (int x=0;x<GuessedLett.size();x++){if (GuessedLett.get(x).equals(s)){return state();}}
            for (int x=0;x<WrongLett.size();x++){if (WrongLett.get(x).equals(s)){return state();}}
            /*se presente in soluzione aggiorni indovinate*/
            if(soluzione.indexOf(s)>=0){
                GuessedLett.add(s);
                aggiornaparola(s);
                if(parola.equals(soluzione)){
                    active=false;
                    found=true;}
            }
            /*altrimenti aggiorni sbagliate e diminiusci vita*/
            if(soluzione.indexOf(s)<0){
                WrongLett.add(s);
                life--;
            }
        }
        if (s.length() > 1) {/*cerca soluzione*/
            if(s.equals(soluzione)){found=true;}
            active=false;
        }
        return state();
    }

    private WGState state() {
        String s = "";
        if (start) {
            s = name + "\n" + info + "\n"+"Parola:"+formatparola(parola)+"\n"+"Mancanti: "+formatWrongLett(WrongLett)+"\n"+"Indovina:";
            active=true;
        }
        if(!start && found){
            active=false;
            s="HAI INDOVINATO: "+soluzione+"\n";}
        if(!start && life>0 && active && !found){
            s="Parola:"+formatparola(parola)+"\n"+"Mancanti: "+formatWrongLett(WrongLett)+"\n"+"Indovina:";
        }
        if(!start && life==0 && !found){
            s="Parola:"+formatparola(parola)+"\n"+"Mancanti: "+formatWrongLett(WrongLett)+" (Hai esaurito il numero di errori)\n"+"Indovina:";
        }
        if((!found && !active)||(life<0)){
            active=false;
            s="Non hai indovinato, la parola era: "+soluzione+"\n";
        }
        start = false;
        return new SimpleWGState(active ? Action.CONTINUE : Action.END, s);
    }

    /**
     * Termina prematuramente l'eventuale gioco attivo
     */
    @Override
    public void abort() {active = false;}

    private String formatparola(String parola){
    /*aggiunge uno spazio prima di ogni char*/
        String s="";
        for(int i=0;i<parola.length();i++){s+=(" "+parola.charAt(i));}
        return s;
    }
    private String formatWrongLett(List<String> WrongLett){
    /*aggiunge virgolaspazio dopo ogni lettera della lista*/
        String s="";
        for(int i=0;i<WrongLett.size();i++) {
            s += (WrongLett.get(i));
            if (i != WrongLett.size()-1) {
                s += ", ";
            }
        }
        return s;
    }

    private String aggiornaparola(String s){
        String newparola="";
        for(int x=0;x<soluzione.length();x++){
            if(soluzione.charAt(x)==s.charAt(0)){newparola+=s;}
            else {newparola+=parola.charAt(x);}
        }
        parola=newparola;
        return parola;
    }

    private static String parola = "";
    private static String soluzione ="";
    private static int life=0;

    private static boolean active = false;
    private static boolean found = false;
    private static boolean start = true;

    private ArrayList<Param<?>> paramList=new ArrayList<>(); //lista parametri
    private List<String> Wordlist= new ArrayList<>();//lista parole filtrate dal costruttore
    private static ArrayList<String> GuessedLett= new ArrayList<>();//lista lettere indovinate
    private static ArrayList<String> WrongLett= new ArrayList<>();//lista lettere sbagliate

    final private static String name = "L'IMPICCATO";
    final private static String info = "Trova la parola indovinando una lettera alla volta\n" +
            "Se credi di aver indovinato immetti l'intera parola";
}