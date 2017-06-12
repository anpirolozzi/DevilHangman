package hw2.game.ui;

import hw2.game.Action;
import hw2.game.Param;
import hw2.game.WGame;
import hw2.game.WGState;
import hw2.games.Hangman;
import hw2.games.EvilHangman;

import java.util.ArrayList;
import java.util.Scanner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import java.io.InputStream;
import java.util.Scanner;


/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Gestore per la UI testuale (Interfaccia Utente Testuale) per giochi di tipo
 * {@link hw2.game.WGame}.
 * C'Ã¨ un unico metodo statico start che fa partire l'esecuzione della UI. Questa
 * presenta all'utente (giocatore) un menu con i possibili giochi e la possibilitÃ  
 di
 * uscire. Quando il giocatore sceglie uno dei giochi digitando il numero della
 * corrispondente voce di menu (con il nome del gioco), se il gioco ha dei parametri
 * viene data la possibilitÃ  di modificarne i valori. Altrimenti viene iniziata una
 * nuova partita del gioco scelto. Ecco un esempio di sessione di gioco con due
 * giochi Hangman e EvilHangman:
 <pre>
 1. L'IMPICCATO
 2. L'IMPICCATO DIABOLICO
 3. Quit
 Digita un intero da 1 a 3: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 1
 Massimo numero di errori: 3
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (3)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 2
 Per interrompere il gioco digita $stop
 L'IMPICCATO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: a _ _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 e
 Parola: a _ _ e _ e _ _ _
 Mancanti:
 Indovina:
 o
 Parola: a _ _ e _ e _ _ _
 Mancanti: o
 Indovina:
 i
 Parola: a _ _ e _ e _ _ i
 Mancanti: o
 Indovina:
 r
 Parola: a _ _ e r e _ _ i
 Mancanti: o
 Indovina:
 t
 Parola: a _ _ e r e _ t i
 Mancanti: o
 Indovina:
 s
 Parola: a _ _ e r e s t i
 Mancanti: o
 Indovina:
 b
 Parola: a _ _ e r e s t i
 Mancanti: o, b
 Indovina:
 f
 Parola: a _ _ e r e s t i
 Mancanti: o, b, f (Hai esaurito il numero di errori)
 Indovina:
 v
 Non hai indovinato, la parola era: alzeresti

 1. L'IMPICCATO
 2. L'IMPICCATO DIABOLICO
 3. Quit
 Digita un intero da 1 a 3: 2
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Lunghezza parola (5)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 2
 Lunghezza parola: 10
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Lunghezza parola (10)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 3
 Per interrompere il gioco digita $stop
 L'IMPICCATO DIABOLICO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: _ _ _ _ _ _ _ _ _ _
 Mancanti: a
 Indovina:
 e
 Parola: _ _ _ _ _ _ _ _ _ _
 Mancanti: a, e
 Indovina:
 i
 Parola: _ _ _ _ _ _ _ i _ _
 Mancanti: a, e
 Indovina:
 o
 Parola: _ o _ _ o _ _ i _ o
 Mancanti: a, e
 Indovina:
 r
 Parola: _ o _ _ o _ _ i _ o
 Mancanti: a, e, r
 Indovina:
 t
 Parola: _ o _ _ o _ _ i _ o
 Mancanti: a, e, r, t
 Indovina:
 l
 Parola: _ o _ _ o _ _ i _ o
 Mancanti: a, e, r, t, l
 Indovina:
 p
 Parola: _ o _ _ o _ _ i _ o
 Mancanti: a, e, r, t, l, p (Hai esaurito il numero di errori)
 Indovina:
 c
 Parola: _ o _ _ o c _ i _ o
 Mancanti: a, e, r, t, l, p (Hai esaurito il numero di errori)
 Indovina:
 h
 Parola: _ o _ _ o c h i _ o
 Mancanti: a, e, r, t, l, p (Hai esaurito il numero di errori)
 Indovina:
 n
 Parola: _ o _ _ o c h i n o
 Mancanti: a, e, r, t, l, p (Hai esaurito il numero di errori)
 Indovina:
 m
 Non hai indovinato, la parola era: soffochino

 1. L'IMPICCATO
 2. L'IMPICCATO DIABOLICO
 3. Quit
 Digita un intero da 1 a 3: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (3)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 2
 Per interrompere il gioco digita $stop
 L'IMPICCATO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: _ _ _ _ _ _ _ _ _ a
 Mancanti:
 Indovina:
 e
 Parola: _ _ _ _ _ _ e _ _ a
 Mancanti:
 Indovina:
 i
 Parola: _ i _ i _ _ e _ _ a
 Mancanti:
 Indovina:
 r
 Parola: _ i _ i _ _ e _ _ a
 Mancanti: r
 Indovina:
 t
 Parola: _ i _ i _ _ e t t a
 Mancanti: r
 Indovina:
 c
 Parola: _ i c i c _ e t t a
 Mancanti: r
 Indovina:
 bicicletta
 HAI INDOVINATO: bicicletta

 1. L'IMPICCATO
 2. L'IMPICCATO DIABOLICO
 3. Quit
 Digita un intero da 1 a 3: 3
 Fine
 </pre>
 */
public class TextUIWGames {
    /**
     * Inzia una sessione di gioco tramite interfaccia testuale che permette di
     * giocare con i giochi specificati in games. Quando una partita Ã¨ in corso, il
     * giocatore puÃ² interromperla digitando $stop. In tal caso il gestore 
     invocherÃ 
     * il metodo {@link hw2.game.WGame#abort()} del gioco.
     * <p>
     * La UI deve gestire il menu per la scelta del gioco e quello (o quelli) per la
     * modifica dei valori degli eventuali parametri di gioco. Inoltre deve prendere
     * l'input digitato dal giocatore e comunicarlo al gioco e visualizzare lo stato
     * della partita ritornato dal gioco. Come nell'esempio mostrato sopra.
     * <p>
     * Si consiglia di leggere l'input sempre come intera linea.
     *
     * @param games i giochi disponibili
     */
    public static void start(WGame... games) {
        GamesList = new ArrayList<>();
        input = new Scanner(System.in);
        selgame=0;
        selpar=0;
        for (WGame g : games) {GamesList.add(g);}
        menu();
    }

    private static void menu() {
        boolean open=true;
        while (open) {
            menugame();
            selectgame();
            if (selgame == (Integer.valueOf(GamesList.size())+1) ) {open=false;}
            if (open){
                menuparameter();
                play(GamesList.get((selgame) - 1));
            }
        }
        System.out.println("Fine");
    }

    private static void menugame(){
        String menu = "";
        for (WGame g : GamesList) {
            menu += (Integer.valueOf(GamesList.indexOf(g) + 1)) + ". " + g.name() + "\n";
        }
        menu += (Integer.valueOf(GamesList.size() + 1)) + ". " + "Quit" + "\n";
        System.out.println(menu);
    }

    private static void selectgame() {
        try {
            String menu = "Digita un intero da 1 a " + Integer.valueOf(GamesList.size() + 1) + ":";
            System.out.println(menu);
            selgame = Integer.parseInt(input.nextLine());
            if((selgame<=0)||(selgame>Integer.valueOf(GamesList.size() + 1))){throw new IllegalArgumentException();}
            }
        catch (IllegalArgumentException ex) {
            System.out.println("Errore, input ammessi: ["+IOparam(GamesList.size())+"]");
            selectgame();}
    }

    private static void menuparameter() {
        String menu = "Modifica i parametri di gioco o inizia a giocare:\n";
        if (!GamesList.get((selgame) - 1).params().isEmpty()) {
            int paramindex = 1;
            for (Param<?> g : GamesList.get((selgame) - 1).params()) {
                menu += paramindex + ". " + g.prompt() + " (" + g.get() + ")\n";
                paramindex++;
            }
            menu += (Integer.valueOf(GamesList.get((selgame) - 1).params().size() + 1)) + ". " + "Inizia a giocare" + "\n";
        }
        System.out.println(menu);
        selectparameter();
    }

    private static void selectparameter() {
        try {
            String menu = "Digita un intero da 1 a " + Integer.valueOf(GamesList.get((selgame) - 1).params().size() + 1) + ":";
            System.out.println(menu);
            selpar = Integer.parseInt(input.nextLine());
            if ((selpar <= 0)||(selpar > Integer.valueOf(GamesList.get((selgame) - 1).params().size() + 1))) {
                throw new IllegalArgumentException();
            }
            if(selpar<=Integer.valueOf(GamesList.get((selgame) - 1).params().size())){menusetparameter();}
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Errore, input ammessi: ["+IOparam(GamesList.get((selgame) - 1).params().size())+"]");
            selectparameter();}
    }

    private static void menusetparameter(){
        String menu=GamesList.get((selgame) - 1).params().get(selpar-1).prompt()+": ";
        System.out.println(menu);
        setparameter();
    }
    private static void setparameter() {
        try {
            Integer setpar = Integer.parseInt(input.nextLine());
            if ((setpar <= 0)||(setpar > Integer.valueOf(GamesList.get((selgame) - 1).params().get(selpar-1).values().size() ))) {
                throw new IllegalArgumentException();
            }
            GamesList.get((selgame) - 1).params().get(selpar - 1).set(setpar - 1);
            menuparameter();
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Errore, input ammessi: ["+IOsetparam()+"]");
            menusetparameter();
        }
    }

    private static void play(WGame g) {
        System.out.println("Per interrompere il gioco digita " + STOP);
        WGState state = g.newGame();
        System.out.println(state.state());
        while (state.action() == Action.CONTINUE) {
            String s = input.nextLine();
            if (s.equals(STOP)) {
                g.abort();
                System.out.println("Gioco interrotto");
                break;
            }
            state = g.player(s);
            System.out.println(state.state());
        }
    }

    private static String IOparam(int size){
        String parameter="";
        for(int x=0;x<size;x++){
            parameter+=Integer.valueOf(x+1)+", ";
        }
        parameter+=Integer.valueOf(size+1);
        return parameter;
    }

    private static String IOsetparam(){
        int size=GamesList.get((selgame) - 1).params().get(selpar-1).values().size();
        String parameter="";
        for(int x=0;x<size-1;x++){
            parameter+=GamesList.get((selgame) - 1).params().get(selpar-1).values().get(x)+", ";
        }
        parameter+=GamesList.get((selgame) - 1).params().get(selpar-1).values().get(size-1);
        return parameter;
    }

    private static final String STOP = "$stop";
    private static ArrayList<WGame> GamesList;
    private static Scanner input;
    private static int selgame;
    private static int selpar;
}