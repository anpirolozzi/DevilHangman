package hw1.test;

import java.util.*;
import static java.lang.System.out;

import static hw1.cmd.CmdFactory.*;
import hw1.cmd.CmdManager;

/** Classe per fare il testing dell'homework. Il metodo
 * {@link hw1.test.Test#test_console()} permette di effettuare test da tastiera.
 * La classe può essere liberamente modificata aggiungendo ad esempio altri test. */
public class Test {
    public static void main(String[] args) {
        test_console();
    }

    /** Esegue un test da tastiera di un CmdManager che è creato con tutti i comandi
     * ritornati dal metodo {@link hw1.cmd.CmdFactory#getAll()}. Quindi assicurarsi
     * che tale matodo ritorni solamente i comandi che sono stati implementati. */
    public static void test_console() {
        out.println("Test tramite console");
        CmdManager cm = null;
        try {
            cm = new CmdManager(getAll());
        } catch (Exception ex) {
            out.println(ex);
            out.println("Fine test");
            return;
        }
        Scanner input = new Scanner(System.in);
        while (true) {
            try {
                out.print("Digita una stringa di comando (q per finire): ");
                String cmd = input.nextLine();
                if (cmd.equals("q")) break;
                out.println(cm.execute(cmd));
            } catch (Exception ex) {
                out.println(ex);
            }
        }
        out.println("Fine test");
    }
}
