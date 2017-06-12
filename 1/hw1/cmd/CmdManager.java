package hw1.cmd;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Un oggetto {@code CmdManager} è un manager di un insieme di comandi (di tipo
 * {@link hw1.cmd.Command}) che permette di ottenere la documentazione di ognuno e di
 * eseguire stringhe di comando relative ai comandi gestiti. Un manager garantisce
 * sempre che non ci siano due comandi gestiti che hanno un nome uguale. */
public class CmdManager {
    /** Crea un manager di comandi con i comandi specificati. Il manager creato ha
     * sempre un comando di help per ottenere informazioni sui comandi gestiti. Tale
     * comando è così specificato:
     * <pre>
     * Nomi: "help","h"
     * Doc:  "h cmd  --> documentazione del comando cmd"
     * Esempi:
     *     "h"  --> "h,help\npi,Pi,PI\n+,sum\n^,**,pow\nfactorize\n"
     *     "help"  --> "h,help\npi,Pi,PI\n+,sum\n^,**,pow\nfactorize\n"
     *     "h sum"  --> "+ x y ... z  --> somma x+y+...+z"
     *     "help +"  --> "+ x y ... z  --> somma x+y+...+z"
     *     "h nocmd"  --> IllegalArgumentException
     * </pre>
     *
     * @param cc i comandi gestiti dal manager
     * @throws NullPointerException     se uno dei comandi è null
     * @throws IllegalArgumentException se due comandi hanno un nome in comune
     *                                  (compreso il comando di help). */
    public CmdManager(Command... cc) {
        throw new UnsupportedOperationException();
    }

    /** Aggiunge un comando al manager.
     *
     * @param c il comando da aggiungere
     * @throws NullPointerException     se il comando è null
     * @throws IllegalArgumentException se il comando ha un nome che è di uno dei
     *                                  comandi già presenti (compreso il comando di help). */
    public void add(Command c) {
        throw new UnsupportedOperationException();
    }

    /** Ritorna una stringa con tante linee quanti sono i comandi del manager e in
     * ogni linea elenca i nomi di uno dei comandi. Esempio
     * <pre>
     * "h,help\npi,Pi,PI\n+,sum\n^,**,pow\nfactorize\n"
     * </pre>
     * L'ordine è arbitrario.
     *
     * @return una stringa con i nomi dei comandi */
    public String commands() {
        throw new UnsupportedOperationException();
    }

    /** Esegue la stringa di comando e ritorna il risultato. Esempi
     * <pre>
     * "h"  --> "h,help\npi,Pi,PI\n+,sum\n^,**,pow\nfactorize\n"
     * null  --> NullPointerException
     * "hh"  --> IllegalArgumentException
     * "sum 1 2"  --> "3"
     * "sum 1 2 x"  --> IllegalArgumentException
     * </pre>
     *
     * @param cmd un comando da eseguire
     * @return il risultato dell'esecuzione della stringa di comando
     * @throws NullPointerException     se la stringa di comando è null
     * @throws IllegalArgumentException se non c'è un comando con il nome dato o se
     *                                  la stringa di comando non è valida, cioè il
     *                                  metodo execute del relativo comando lancia l'eccezione */
    public String execute(String cmd) {
        throw new UnsupportedOperationException();
    }
}
