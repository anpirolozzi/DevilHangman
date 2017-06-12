package hw2.test;

import hw2.game.ui.TextUIWGames;
import hw2.game.Action;
import hw2.game.Param;
import hw2.game.WGame;
import hw2.game.WGState;
import hw2.games.Hangman;
import hw2.games.EvilHangman;

import java.util.Scanner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/** Classe per fare il testing dell'homework. La classe può essere liberamente
 * modificata aggiungendo ad esempio altri test. */
public class Test {
    public static void main(String[] args) throws IOException {
        TextUIWGames.start(new Hangman(Paths.get("files", "parole.txt"), StandardCharsets.UTF_8, 5));
        //Test.play(new Hangman(Paths.get("files", "parole.txt"), StandardCharsets.UTF_8, 5));
        //TextUIWGames.start(new Hangman(Paths.get("files", "parole.txt"), StandardCharsets.UTF_8, 5),
        //       new EvilHangman(Paths.get("files", "parole2.txt"), StandardCharsets.UTF_8, 5));
    }


   private static void play(WGame g) {
        System.out.println("Per interrompere il gioco digita " + STOP);
        WGState state = g.newGame();
       System.out.println(state.state());
        while (state.action() == Action.CONTINUE){
            String s = input.nextLine();
            if (s.equals(STOP)) {
                g.abort();
                System.out.println("Gioco interrotto");
                break;
            }
            state=g.player(s);
            System.out.println(state.state());
            }
        }
    private static final String STOP = "$stop";
    private static Scanner input=new Scanner(System.in);

}
