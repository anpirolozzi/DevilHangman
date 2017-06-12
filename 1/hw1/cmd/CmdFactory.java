package hw1.cmd;

import java.util.ArrayList;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Una classe con factory methods per creare alcuni comandi. Ogni factory method
 * ritorna uno specifico comando di tipo {@link hw1.cmd.Command}. Sarebbe bene che
 * ognuno di questi ritorni ad ogni invocazione sempre lo stesso oggetto. */
public class CmdFactory {

    private static ArrayList<Command> lstCmd;

    public CmdFactory()
    {
        //Mi assicuro l'inizializzazione di lstCmd
        lstCmd.clear();
        //Aggiunta delle istanze di comandi
        lstCmd.add(PiGreco.getInstance());
        lstCmd.add(Somma.getInstance());
        lstCmd.add(Potenza.getInstance());
        lstCmd.add(Fattorizzazione.getInstance());
    }

    /** Ritorna un comando per ottenere il valore di pigreco. Il metodo
     * {@link hw1.cmd.Command#execute(String)} del comando ritornato lancia
     * {@link java.lang.IllegalArgumentException} se la stringa di comando non è
     * esattamente uno dei nomi del comando.
     * <pre>
     * Nomi: "pi", "Pi", "PI"
     * Doc:  "pi  --> valore di Pi greco"
     * Esempi:
     *     execute("pi") ritorna "3.141592653589793"
     *     execute("pi g") lancia IllegalArgumentException
     * </pre>
     * @return un comando per ottenere il valore di pigreco */
    public static Command getPI() {
        return PiGreco.getInstance();
    }

    /** Ritorna un comando per sommare una serie di numeri. Il metodo
     * {@link hw1.cmd.Command#execute(String)} del comando ritornato lancia
     * {@link java.lang.IllegalArgumentException} se nella stringa di comando ciò
     * che segue il nome del comando non è interpretabile come una sequenza di
     * numeri, interi o con virgola, separati da whitespaces.
     * <pre>
     * Nomi: "+", "sum"
     * Doc:  "+ x y ... z  --> somma x+y+...+z"
     * Esempi:
     *     execute("+ 1   2.5 -3") ritorna "0.5"
     *     execute("sum 1 2.5    -3") ritorna "0.5"
     *     execute("+1   2.5 -3") ritorna "0.5"
     *     execute("+") ritorna "0.0"
     *     execute("+ 1 2 3 4") ritorna "10.0"
     *     execute("+ 1 2 a") lancia IllegalArgumentException
     * </pre>
     * @return un comando per sommare una serie di numeri */
    public static Command getSum() {
        return Somma.getInstance();
    }

    /** Ritorna un comando per l'elevazione a potenza. Il metodo
     * {@link hw1.cmd.Command#execute(String)} del comando ritornato lancia
     * {@link java.lang.IllegalArgumentException} se nella stringa di comando ciò
     * che segue il nome del comando non è interpretabile come due numeri,
     * interi o con virgola, separati da whitespaces.
     * <pre>
     * Nomi: "^", "**", "pow"
     * Doc:  "pow x y  --> x elevato a y"
     * Esempi:
     *     execute("pow2 5") ritorna "32.0"
     *     execute("^ 1.2 -0.6") ritorna "0.8963781307771418"
     *     execute("** -1 0.5") ritorna "NaN"
     *     execute("pow 2") lancia IllegalArgumentException
     * </pre>
     * @return un comando per l'elevazione a potenza */
    public static Command getPow() {
        return Potenza.getInstance();
    }

    /** Ritorna un comando per fattorizzare un intero. Il metodo
     * {@link hw1.cmd.Command#execute(String)} del comando ritornato lancia
     * {@link java.lang.IllegalArgumentException} se nella stringa di comando ciò
     * che segue il nome del comando non è interpretabile come un numero intero
     * maggiore od uguale a 2. Nell'output i fattori sono elencati in ordine crescente.
     * <pre>
     * Nomi: "factorize"
     * Doc:  "factorize n  --> fattorizzazione di n"
     * Esempi:
     *     execute("factorize 1504") ritorna "2(5) 47"
     *     execute("factorize 123400272626252672") ritorna "2(7) 29(2) 1097 1044969287"
     *     execute("factorize1000") ritorna "2(3) 5(3)"
     *     execute("factorize 15 s") lancia IllegalArgumentException
     * </pre>
     * @return un comando per fattorizzare un intero */
    public static Command getFactorize() {
        return Fattorizzazione.getInstance();
    }


    /** @return un array con tutti i comandi ritornati dai metodi di questa classe */
    public static Command[] getAll() {
        //definisco allcomand per contenere tutti i comandi
        Command[] allcommand = new Command[lstCmd.size()];
        //e riempo scorrendo lstCmd
        int x=0;
        while (x<lstCmd.size()){
            allcommand[x]=lstCmd.get(x);
            x++;
        }
        return allcommand;
    }
}