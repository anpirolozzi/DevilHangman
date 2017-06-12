package hw2.game.util;
import hw2.test.PartialGrade;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Classe con metodi di utilità per implementare giochi.*/
public class Utils {
    /** Ritorna true se la stringa s non contiene caratteri che non sono alfabetici
     * minuscoli, cioè i caratteri abcdefghijklmnopqrstuvwxyz. Se la stringa è vuota,
     * ritorna true.
     * @param s una stringa
     * @return true se s non contiene caratteri che non sono alfabetici minuscoli */
    public static boolean isAlphaLowercase(String s) {
        for(int i=0;i<s.length();i++) {
            if (seqminusc.indexOf(s.charAt(i)) < 0) {return false;}
        }
        return true;
        }


    /** Ritorna la lista delle linee del file di percorso p che non soddisfano il
     * predicato filter. Ad esempio, se filter è true se la linea ha lunghezza
     * minore di 5, allora le linee ritornate sono solamente quelle di lunghezza
     * maggiore od uguale a 5.
     * @param p  percorso di un file (di testo)
     * @param filter  filtro per le linee non volute
     * @return lista delle linee del file filtrate
     * @throws java.io.IOException se si verifica un errore leggendo il file */
    public static List<String> lines(Path p, Charset cs, Predicate<String> filter)throws IOException {
        List<String> lines = Files.readAllLines(p,cs);
        lines.removeIf(filter);
        return lines;
      }

    /** ATTENZIONE: questo metodo non deve essere implementato e deve essere usato
     * nei giochi in cui si deve scegliere una stringa random, come ad esempio
     * nel gioco Hangman.
     *
     * Ritorna una stringa scelta in modo random uniforme dalle stringhe in coll.
     * @param coll  una collezione di stringhe
     * @return  una stringa random scelta da coll */
    public static String choose(Collection<String> coll) {
        return PartialGrade.choose(coll);
    }

    /** Ritorna l'array ordinato in senso crescente e senza ripetizioni delle
     * lunghezze delle stringhe in coll.
     * @param coll  collezione di stringhe
     * @return array ordinato e senza ripetizioni delle lunghezze delle
     * stringhe in coll. */
    public static Integer[] lengths(Collection<String> coll) {
        String [] arrcoll = new String[coll.size()];
        coll.toArray(arrcoll);
        Set<Integer> subSet = new HashSet<>();
        for (int i = 0 ; i <arrcoll.length ; i++) {
            subSet.add(arrcoll[i].length());
        }
        Integer [] length = new Integer[subSet.size()];
        subSet.toArray(length);
        Arrays.sort(length);
        return length;
    }

    /** Ritorna l'insieme di stringhe in coll che hanno lunghezza len.
     * @param coll  una collezione di stringhe
     * @param len  la lunghezza voluta
     * @return l'insieme di stringhe in coll che hanno lunghezza len */
    public static Set<String> stringsOfLength(Collection<String> coll, int len) {
        String [] arrcoll = new String[coll.size()];
        coll.toArray(arrcoll);
        Set<String> subSet = new HashSet<>();
        for (int i = 0 ; i <arrcoll.length ; i++) {
            if(arrcoll[i].length()==len){subSet.add(arrcoll[i]);}
        }
        return subSet;
    }

    /** Ritorna la lista di insiemi che partizionano le stringhe in coll secondo
     * la relazione di equivalenza equiv. Più precisamente gli insiemi ritornati
     * sono disgiunti, la loro unione è uguale all'insieme di stringhe in coll e
     * due stringhe x e y appartengono allo stesso insieme se e solo se equiv
     * applicato a x e y ritorna true. Ad esempio, supponendo che equiv applicata
     * a x, y sia true se e solo se le stringhe x e y hanno la lettera 'a' nelle
     * stesse posizioni,
     * <pre>
     *     coll = ["pizza","torre","porta","pazza","perle","torte"]
     *     partition(coll, equiv)  ritorna
     *     [["perle","torte","torre"],["pizza","porta"],["pazza"]]
     * </pre>
     * Si assume che equiv sia una relazione di equivalenza sull'insieme di
     * stringhe in coll. Cioè equiv deve essere riflessiva, simmetrica e
     * transitiva.
     * @param coll  una collezione di stringhe
     * @param equiv  una relazione di equivalenza su coll
     * @return la lista degli insiemi che partizionano coll rispetto a equiv */
    public static List<Set<String>> partition(Collection<String> coll,
                                              BiPredicate<String,String> equiv) {

        LinkedList<Set<String>>lista=new LinkedList<>();
        lstcoll.clear();
        lstcoll.addAll(coll);
        while(!(lstcoll.isEmpty())) {lista.add(partizionize(equiv));}
        return lista;
    }

    private static Set<String> partizionize(BiPredicate<String,String> equiv){
        //ritorna un set da inserire nella lista di partition usando rimanenti di lstcoll
        Set<String>setmp = new HashSet<>();
        setmp.clear();
        setmp.add(lstcoll.get(0));
        int i=1;
                    while (i<lstcoll.size()){
                        if(equiv.test(lstcoll.get(0),lstcoll.get(i))){
                            setmp.add(lstcoll.get(i));
                            lstcoll.remove(i);
                        }
                        i++;
                    }
        lstcoll.remove(0);
        return setmp;

    }

    private final static String seqminusc="abcdefghijklmnopqrstuvwxyz";
    private static LinkedList<String>lstcoll=new LinkedList<>();
}