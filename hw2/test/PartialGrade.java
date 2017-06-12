package hw2.test;

import hw2.game.ui.TextUIWGames;
import hw2.game.util.Utils;
import hw2.games.EvilHangman;
import hw2.games.Hangman;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiPredicate;

/** Per calcolare il punteggio parziale dell'homework eseguire il main di questa
 * classe. Il punteggio è limitato ai primi 25 punti, i rimanenti 15 saranno
 * assegnati da un'altro grade eseguito dopo la consegna dal docente.
 * <br>
 * ATTENZIONE: I file di testo per il grade (e il test) devono essere in una
 * directory di nome files contenuta nella working directory.
 * <br>
 * <b>ATTENZIONE: NON MODIFICARE IN ALCUN MODO QUESTA CLASSE.</b> */
public class PartialGrade {
    public static void main(String[] args) {
        test();
    }









    public static String choose(Collection<String> coll) {
        if (testing && testWord != null) {
            return testWord;
        } else {
            List<String> list = new ArrayList<>(coll);
            return list.get(RND.nextInt(list.size()));
        }
    }

    private static void test() {
        testing = true;
        testWord = null;
        totalScore = 0;
        boolean ok = test_isAlphaLowercase(0.5f, 2000);
        if (ok) ok = test_lines(1f, 8000);
        if (ok) ok = test_lengths(1f, 3000);
        if (ok) ok = test_stringsOfLength(0.5f, 5000);
        if (ok) ok = test_partition(2f, 15000);
        if (ok) ok = test_ui_H("parole.txt", SESSION_H1, TW_H1, 3, 2000);
        if (ok) ok = test_ui_H("parole.txt", SESSION_H2, TW_H2, 3, 2000);
        if (ok) ok = test_ui_H("parole2.txt", SESSION_H3, TW_H3, 3, 2000);
        if (ok) ok = test_ui_E("parole.txt", SESSION_E1, 3, 2000);
        if (ok) ok = test_ui_E("parole2.txt", SESSION_E2, 4, 2000);
        if (ok) test_ui_HE("parole.txt", "parole2.txt", SESSION_HE, TW_H4, 4, 2000);
        System.out.println("Punteggio totale: "+totalScore);
        testing = false;
    }

    private static boolean test_isAlphaLowercase(float sc, int ms) {
        return runTest("Test isAlphaLowercase", sc, ms, () -> {
            try {
                for (String[] p : ALPHA)
                    if (Utils.isAlphaLowercase(p[0]) != p[1].isEmpty())
                        return new Result("ERRORE "+p[0]);
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_lines(float sc, int ms) {
        return runTest("Test lines", sc, ms, () -> {
            try {
                Path p = Paths.get("files", "parole.txt");
                List<String> list = Utils.lines(p, StandardCharsets.UTF_8, s -> s.length() > 5);
                if (list.size() != 3914) return new Result("ERRORE "+list.size() + " != " + 3914);
                list = Utils.lines(p, StandardCharsets.UTF_8, s -> s.length() > 10);
                if (list.size() != 59571) return new Result("ERRORE "+list.size() + " != " + 59571);
                p = Paths.get("files", "parole2.txt");
                list = Utils.lines(p, StandardCharsets.UTF_8, s -> s.length() > 6);
                if (list.size() != 21348) return new Result("ERRORE "+list.size() + " != " + 21348);
                list = Utils.lines(p, StandardCharsets.UTF_8, s -> s.length() > 11);
                if (list.size() != 188273) return new Result("ERRORE "+list.size() + " != " + 188273);
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_choose(float sc, int ms) {
        return runTest("Test choose", sc, ms, () -> {
            try {
                Map<String,Integer> map = new HashMap<>();
                STRLIST.forEach(s -> map.put(s, 0));
                int n = 100*STRLIST.size();
                for (int i = 0 ; i < n ; i++) {
                    String r = Utils.choose(STRLIST);
                    if (!map.containsKey(r))
                        return new Result("ERRORE stringa ritornata non appartiene alla collezione");
                    map.merge(r, 1, Integer::sum);
                }
                for (String k : map.keySet())
                    if (map.get(k) < 50)
                        return new Result("ERRORE scelta non uniformemente random");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_lengths(float sc, int ms) {
        return runTest("Test lengths", sc, ms, () -> {
            try {
                Integer[] lens = Utils.lengths(STRLIST);
                Integer[] cl = {1,2,3,5,9};
                if (!Arrays.equals(lens, cl))
                    return new Result("ERRORE");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_stringsOfLength(float sc, int ms) {
        return runTest("Test stringsOfLength", sc, ms, () -> {
            try {
                Set<String> set = Utils.stringsOfLength(STRLIST, 1);
                if (!(set != null && eqLen(set, 1) && included(set, STRLIST) && set.size() == 11))
                    return new Result("ERRORE");
                set = Utils.stringsOfLength(STRLIST, 2);
                if (!(set != null && eqLen(set, 2) && included(set, STRLIST) && set.size() == 5))
                    return new Result("ERRORE");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_partition(float sc, int ms) {
        return runTest("Test partition", sc, ms, () -> {
            try {
                BiPredicate<String,String> pred = (s1,s2) -> s1.charAt(0) == s2.charAt(0);
                List<Set<String>> list = Utils.partition(STRLIST, pred);
                int n = 0;
                for (Set<String> s : list) {
                    if (!included(s, STRLIST)) return new Result("ERRORE");
                    for (String s1 : s)
                        for (String s2 : s)
                            if (!pred.test(s1, s2)) return new Result("ERRORE");
                    n += s.size();
                }
                if (n != STRLIST.size()) return new Result("ERRORE");
                for (String s : STRLIST) {
                    boolean found = false;
                    for (Set<String> ss : list)
                        if (ss.contains(s)) found = true;
                    if (!found) return new Result("ERRORE");
                }
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_ui_H(String fname, String[][] session, String tw, float sc, int ms) {
        return runTest("Test TextUIWGames con Hangman", sc, ms, () -> {
            try {
                testWord = tw;
                GetOut testOut = new GetOut();
                System.setOut(new PrintStream(testOut));
                System.setIn(toInput(session));
                TextUIWGames.start(new Hangman(Paths.get("files", fname), StandardCharsets.UTF_8, 5));
                String outputs = testOut.getBuffer();
                String corrOut = "";
                for (String[] t : session)
                    corrOut += t[0]+"\n";
                if (!eqOut(corrOut, outputs))
                    return new Result("ERRORE output: "+corrOut+"\n   !=\n"+outputs);
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_ui_HE(String fnameH, String fnameE, String[][] session, String tw, float sc, int ms) {
        return runTest("Test TextUIWGames con Hangman e EvilHangman", sc, ms, () -> {
            try {
                testWord = tw;
                GetOut testOut = new GetOut();
                System.setOut(new PrintStream(testOut));
                System.setIn(toInput(session));
                TextUIWGames.start(new Hangman(Paths.get("files", fnameH), StandardCharsets.UTF_8, 5),
                        new EvilHangman(Paths.get("files", fnameE), StandardCharsets.UTF_8, 5));
                String outputs = testOut.getBuffer();
                String corrOut = "";
                for (String[] t : session)
                    corrOut += t[0]+"\n";
                if (!eqOut(corrOut, outputs))
                    return new Result("ERRORE output: "+corrOut+"\n   !=\n"+outputs);
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_ui_E(String fname, String[][] session, float sc, int ms) {
        return runTest("Test TextUIWGames con EvilHangman", sc, ms, () -> {
            try {
                GetOut testOut = new GetOut();
                System.setOut(new PrintStream(testOut));
                System.setIn(toInput(session));
                TextUIWGames.start(new EvilHangman(Paths.get("files", fname), StandardCharsets.UTF_8, 5));
                String outputs = testOut.getBuffer();
                String corrOut = "";
                for (String[] t : session)
                    corrOut += t[0]+"\n";
                if (!eqOut(corrOut, outputs))
                    return new Result("ERRORE output: "+corrOut+"\n   !=\n"+outputs);
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }



    private static boolean eqOut(String out1, String out2) {
        out1 = out1.trim().toLowerCase();
        out2 = out2.trim().toLowerCase();
        String[] ow1 = out1.split("\\s+");
        String[] ow2 = out2.split("\\s+");
        return Arrays.equals(ow1, ow2);
    }


    private static boolean eqLen(Collection<String> coll, int len) {
        for (String s : coll)
            if (s == null || s.length() != len)
                return false;
        return true;
    }

    private static boolean included(Collection<String> c1, Collection<String> c2) {
        for (String s : c1)
            if (!c2.contains(s))
                return false;
        return true;
    }

    private static final String[][] ALPHA = {{"abchkl",""},{"dfj",""},{"w",""},{"abdf5g","0"},
            {"sdfwgbdgbd6jk","0"},{"",""},{"a abd","0"},{"abe nm","0"}};
    private static final List<String> STRLIST = Arrays.asList("a","b","c","d","e","f","g",
            "h","aa","bb","cc","dd","m","n","o","abs","abcdf","ab","abcdfsfsf","bbb");
    private static final String TW_H1 = "malvisto", TW_H2 = "opineremo", TW_H3 = "tramestii",
    TW_H4 = "divagata";
    private static final String[][] SESSION_H1 = {{"1. L'IMPICCATO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ a _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","e"},
            {"Parola: _ a _ _ _ _ _ _\n" +
                    "Mancanti: e\n" +
                    "Indovina:","i"},
            {"Parola: _ a _ _ i _ _ _\n" +
                    "Mancanti: e\n" +
                    "Indovina:","o"},
            {"Parola: _ a _ _ i _ _ o\n" +
                    "Mancanti: e\n" +
                    "Indovina:","r"},
            {"Parola: _ a _ _ i _ _ o\n" +
                    "Mancanti: e, r\n" +
                    "Indovina:","m"},
            {"Parola: m a _ _ i _ _ o\n" +
                    "Mancanti: e, r\n" +
                    "Indovina:","s"},
            {"Parola: m a _ _ i s _ o\n" +
                    "Mancanti: e, r\n" +
                    "Indovina:","n"},
            {"Parola: m a _ _ i s _ o\n" +
                    "Mancanti: e, r, n\n" +
                    "Indovina:","malvisto"},
            {"HAI INDOVINATO: malvisto\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Fine",""}};
    private static final String[][] SESSION_H2 = {{"1. L'IMPICCATO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Massimo numero di errori:","3"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ e _ e _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ _ e _ e _ _\n" +
                    "Mancanti: a\n" +
                    "Indovina:","o"},
            {"Parola: o _ _ _ e _ e _ o\n" +
                    "Mancanti: a\n" +
                    "Indovina:","a"},
            {"Parola: o _ _ _ e _ e _ o\n" +
                    "Mancanti: a\n" +
                    "Indovina:","t"},
            {"Parola: o _ _ _ e _ e _ o\n" +
                    "Mancanti: a, t\n" +
                    "Indovina:","r"},
            {"Parola: o _ _ _ e r e _ o\n" +
                    "Mancanti: a, t\n" +
                    "Indovina:","s"},
            {"Parola: o _ _ _ e r e _ o\n" +
                    "Mancanti: a, t, s (Hai esaurito il numero di errori)\n" +
                    "Indovina:","m"},
            {"Parola: o _ _ _ e r e m o\n" +
                    "Mancanti: a, t, s (Hai esaurito il numero di errori)\n" +
                    "Indovina:","c"},
            {"Non hai indovinato, la parola era: opineremo\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Fine",""}};
    private static final String[][] SESSION_H3 = {{"1. L'IMPICCATO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Massimo numero di errori:","2"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (2)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ e _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ a _ e _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","i"},
            {"Parola: _ _ a _ e _ _ i i\n" +
                    "Mancanti: \n" +
                    "Indovina:","t"},
            {"Parola: t _ a _ e _ t i i\n" +
                    "Mancanti: \n" +
                    "Indovina:","r"},
            {"Parola: t r a _ e _ t i i\n" +
                    "Mancanti: \n" +
                    "Indovina:","n"},
            {"Parola: t r a _ e _ t i i\n" +
                    "Mancanti: n\n" +
                    "Indovina:","c"},
            {"Parola: t r a _ e _ t i i\n" +
                    "Mancanti: n, c (Hai esaurito il numero di errori)\n" +
                    "Indovina:","m"},
            {"Parola: t r a m e _ t i i\n" +
                    "Mancanti: n, c (Hai esaurito il numero di errori)\n" +
                    "Indovina:","s"},
            {"HAI INDOVINATO: tramestii\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Fine",""},};
    private static final String[][] SESSION_E1 = {{"1. L'IMPICCATO DIABOLICO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:", "1"},{"Modifica i parametri di gioco o inizia a giocare:\n" +
            "1. Massimo numero di errori (6)\n" +
            "2. Lunghezza parola (5)\n" +
            "3. Inizia a giocare\n" +
            "Digita un intero da 1 a 3:","3"},{"Per interrompere il gioco digita $stop\n" +
            "L'IMPICCATO DIABOLICO\n" +
            "Trova la parola indovinando una lettera alla volta\n" +
            "Se credi di aver indovinato immetti l'intera parola\n" +
            "Parola: _ _ _ _ _\n" +
            "Mancanti: \n" +
            "Indovina:","a"},{"Parola: _ _ _ _ _\n" +
            "Mancanti: a\n" +
            "Indovina:","e"},{"Parola: _ _ _ _ _\n" +
            "Mancanti: a, e\n" +
            "Indovina:","i"},{"Parola: _ _ _ _ _\n" +
            "Mancanti: a, e, i\n" +
            "Indovina:","o"},{"Parola: _ o _ _ o\n" +
            "Mancanti: a, e, i\n" +
            "Indovina:","t"},{"Parola: _ o _ _ o\n" +
            "Mancanti: a, e, i, t\n" +
            "Indovina:","r"},{"Parola: _ o _ _ o\n" +
            "Mancanti: a, e, i, t, r\n" +
            "Indovina:","s"},{"Parola: _ o _ _ o\n" +
            "Mancanti: a, e, i, t, r, s (Hai esaurito il numero di errori)\n" +
            "Indovina:","c"},{"Non hai indovinato, la parola era: bollo\n" +
            "\n" +
            "1. L'IMPICCATO DIABOLICO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:","2"},{"Fine",""}};
    private static String[][] SESSION_E2 = {{"1. L'IMPICCATO DIABOLICO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Lunghezza parola (5)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","1"},
            {"Massimo numero di errori:","3"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (5)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","2"},
            {"Lunghezza parola:","8"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (8)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ _ _ _ _ _\n" +
                    "Mancanti: a\n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ _ _ _ _\n" +
                    "Mancanti: a, e\n" +
                    "Indovina:","i"},
            {"Parola: _ _ _ _ i _ _ i\n" +
                    "Mancanti: a, e\n" +
                    "Indovina:","o"},
            {"Parola: _ o _ _ i _ _ i\n" +
                    "Mancanti: a, e\n" +
                    "Indovina:","r"},
            {"Parola: _ o _ _ i _ _ i\n" +
                    "Mancanti: a, e, r (Hai esaurito il numero di errori)\n" +
                    "Indovina:","s"},
            {"Parola: _ o _ _ i s _ i\n" +
                    "Mancanti: a, e, r (Hai esaurito il numero di errori)\n" +
                    "Indovina:","t"},
            {"Parola: _ o _ _ i s t i\n" +
                    "Mancanti: a, e, r (Hai esaurito il numero di errori)\n" +
                    "Indovina:","m"},
            {"Non hai indovinato, la parola era: bobbisti\n" +
                    "\n" +
                    "1. L'IMPICCATO DIABOLICO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (8)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","2"},
            {"Lunghezza parola:","9"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (9)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: e\n" +
                    "Indovina:","i"},
            {"Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: e, i\n" +
                    "Indovina:","o"},
            {"Parola: _ _ _ _ _ _ _ _ o\n" +
                    "Mancanti: e, i\n" +
                    "Indovina:","a"},
            {"Parola: _ _ a _ _ a _ _ o\n" +
                    "Mancanti: e, i\n" +
                    "Indovina:","r"},
            {"Parola: _ _ a _ _ a _ _ o\n" +
                    "Mancanti: e, i, r (Hai esaurito il numero di errori)\n" +
                    "Indovina:","s"},
            {"Parola: s _ a _ _ a _ _ o\n" +
                    "Mancanti: e, i, r (Hai esaurito il numero di errori)\n" +
                    "Indovina:","t"},
            {"Non hai indovinato, la parola era: sbaffando\n" +
                    "\n" +
                    "1. L'IMPICCATO DIABOLICO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:",""},
            {"Errore, input ammessi: [1, 2]\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (9)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","$stop"},
            {"Gioco interrotto\n" +
                    "1. L'IMPICCATO DIABOLICO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Fine",""}};
    private static String[][] SESSION_HE = {{"1. L'IMPICCATO\n" +
            "2. L'IMPICCATO DIABOLICO\n" +
            "3. Quit\n" +
            "Digita un intero da 1 a 3:","2"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Lunghezza parola (5)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","1"},
            {"Massimo numero di errori:","3"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (5)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a\n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a, e\n" +
                    "Indovina:","i"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a, e, i (Hai esaurito il numero di errori)\n" +
                    "Indovina:","o"},
            {"Parola: _ o _ _ o\n" +
                    "Mancanti: a, e, i (Hai esaurito il numero di errori)\n" +
                    "Indovina:","t"},
            {"Non hai indovinato, la parola era: bollo\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Massimo numero di errori:","4"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (4)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ a _ a _ a\n" +
                    "Mancanti: \n" +
                    "Indovina:","r"},
            {"Parola: _ _ _ a _ a _ a\n" +
                    "Mancanti: r\n" +
                    "Indovina:","t"},
            {"Parola: _ _ _ a _ a t a\n" +
                    "Mancanti: r\n" +
                    "Indovina:","g"},
            {"Parola: _ _ _ a g a t a\n" +
                    "Mancanti: r\n" +
                    "Indovina:","d"},
            {"Parola: d _ _ a g a t a\n" +
                    "Mancanti: r\n" +
                    "Indovina:","divagata"},
            {"HAI INDOVINATO: divagata\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:","2"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (5)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a\n" +
                    "Indovina:","u"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a, u\n" +
                    "Indovina:","i"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a, u, i (Hai esaurito il numero di errori)\n" +
                    "Indovina:","o"},
            {"Non hai indovinato, la parola era: beffe\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Fine",""}};


    private static void print(Result r) {
        System.out.println(!r.fatal && r.err == null ? "OK" : r.err);
    }
    private static void print(String m) { System.out.print(m); }
    private static void println(String m) { print(m+"\n"); }

    private static Result handleThrowable(Throwable t) {
        String msg = "";
        boolean fatal = false;
        if (t instanceof Exception)
            msg += "Eccezione inattesa: ";
        else {
            msg += "ERRORE GRAVE, impossibile continuare il test: ";
            fatal = true;
        }
        return new Result(msg+t, fatal);
    }

    private static boolean runTest(String msg, float score, int ms, Callable<Result> test) {
        FutureTask<Result> future = new FutureTask<>(test);
        Thread t = new Thread(future);
        t.setDaemon(true);
        print(msg+" ");
        PrintStream stdOut = System.out;
        InputStream stdIn = System.in;
        System.setOut(FAKEOUT);
        t.start();
        Result res = null;
        try {
            res = future.get(ms, TimeUnit.MILLISECONDS);
        } catch (CancellationException | InterruptedException | TimeoutException | ExecutionException e) {}
        future.cancel(true);
        System.setOut(stdOut);
        System.setIn(stdIn);
        if (res == null)
            println("ERRORE: limite di tempo superato ("+ms+" ms)");
        else if (res.fatal) {
            println(res.err);
            return false;
        } else if (res.err != null) {
            println(res.err);
        } else {
            println(" score "+score);
            totalScore += score;
        }
        return true;
    }

    private static class Result {
        public final String err;
        public final boolean fatal;

        public Result(String e, boolean f) {
            err = e;
            fatal = f;
        }

        public Result() { this(null, false); }
        public Result(String e) { this(e, false); }
    }

    private static InputStream toInput(String[][] session) {
        String s = "";
        for (String[] t : session)
            s += t[1]+"\n";
        return new SetIn(s);
    }

    private static class SetIn extends ByteArrayInputStream {
        public SetIn(String s) {
            super(s.getBytes());
        }
    }

    private static class GetOut extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            buffer.add(b);
        }

        public String getBuffer() {
            if (buffer.size() == 0) return "";
            int[] cps = new int[buffer.size()];
            for (int i = 0 ; i < cps.length ; i++)
                cps[i] = buffer.get(i);
            String s = new String(cps, 0, cps.length);
            buffer.clear();
            return s;
        }

        private final List<Integer> buffer = new ArrayList<>();
    }

    private static class FakeOut extends OutputStream {
        @Override
        public void write(int b) throws IOException {}
    }

    private static double totalScore;
    private static volatile boolean testing = false;
    private static volatile String testWord = null;
    private static final PrintStream FAKEOUT = new PrintStream(new FakeOut());
    private static final Random RND = new Random();
}
