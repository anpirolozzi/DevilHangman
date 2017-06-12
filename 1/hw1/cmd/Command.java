package hw1.cmd;

import java.util.List;

/** Rappresenta un comando capace di eseguire stringhe di comando. La sintassi di una
 * stringa di comando è
 * <pre>
 *     cmd args
 * </pre>
 * Cioè una stringa che inizia con uno dei nomi {@code cmd} del comando seguito
 * dagli eventuali argomenti separati tra loro da uno o più spazi. Ad esempio un
 * comando per effettuare somme potrebbe avere i seguenti quattro nomi:
 * <pre>
 *     + sum Sum SUM
 * </pre>
 * e accettare stringhe di comando come:
 * <pre>
 *     + 2 4 2.5
 *     sum 4.5 4
 *     Sum 8.5
 *     SUM 1 2 4 1.5
 * </pre>
 * In tutti i casi il risultato sarebbe la stringa {@code "8.5"}. */
public interface Command {
    /** Ritorna la lista dei nomi di questo comando. La lista deve essere non
     * modificabile come {@link java.util.Collections#unmodifiableList}. L'ordine
     * è arbitrario.
     * @return la lista dei nomi di questo comando */
    List<String> getNames();

    /** Esegue la stringa di comando e ritorna il risultato. Se la stringa di comando
     * non è valida, lancia IllegalArgumentException.
     * @param cmd  la stringa di comando da eseguire
     * @return il risultato dell'esecuzione della stringa di comando
     * @throws IllegalArgumentException  se la stringa di comando non è valida */
    String execute(String cmd);

    /** @return la documentazione di questo comando */
    String doc();
}
