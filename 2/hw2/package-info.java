/** <b>HOMEWORK 2</b>
 * <br>
 * L'homework ha come obiettivo la realizzazione di un sistema per costruire giochi.
 * Essendo questo un obiettivo troppo ambizioso per un homework, ci limiteremo a
 * giochi di parole, cioè word games e word puzzles. Inoltre per l'interfaccia utente
 * (UI) ci limiteremo a quella testuale. Tuttavia la struttura del sistema è pensata
 * per poter essere agevolmente estesa anche a giochi di altro tipo e a interfacce
 * grafiche. Perciò la struttura è stata progettata per separare l'implementazione
 * dei giochi, cioè il loro comportamento di interazione con i giocatori, dalla loro
 * resa tramite la UI che potrebbe essere testuale o grafica. La separazione è
 * veicolata dall'interfaccia {@link hw2.game.WGame} che serve proprio a fornire
 * l'interfaccia di un word game verso un gestore di UI (grafica o testuale). Tale
 * interfaccia è coadiuvata da due altre interfacce. L'interfaccia
 * {@link hw2.game.WGState} che serve a rappresentare l'informazione che un gestore
 * di un gioco (cioè un WGame) deve passare al gestore di UI per comunicare
 * l'aggiornamento dello stato di una partita a seguito dell'interazione con il
 * giocatore, e l'interfaccia {@link hw2.game.Param} che serve a rappresentare
 * eventuali parametri da cui può dipendere il gioco e che possono essere impostati
 * dal giocatore tramite la UI.
 * <br>
 * <br>
 * Nel package {@link hw2.games} ci sono due classi relative ad altrettanti giochi
 * che devono essere implementati rispettando l'interfaccia {@link hw2.game.WGame}.
 * Nel package {@link hw2.game.util} ci sono classi e metodi statici che devono
 * essere implementati e che sono utili sia per implementare i giochi in
 * {@link hw2.games} che eventualmente altri word games. Infine, la classe
 * {@link hw2.game.ui.TextUIWGames} è il gestore della UI per word games che deve
 * essere implementato.
 * <br>
 * <br>
 * Seguire le specifiche date nei Javadoc delle classi e delle nterfacce. Si possono
 * aggiungere metodi e campi alle classi, però le intestazioni dei metodi da
 * implementare non devono essere modificate. Si possono anche aggiungere altre
 * classi ma la struttura dei package forniti non deve essere modificata.
 * <br>
 * <br>
 * Il package {@link hw2.test} è per il testing. La classe {@link hw2.test.Test} può
 * essere usata per effettuare test a piacere delle classi e dei metodi da
 * implementare. Invece la classe {@link hw2.test.PartialGrade} è riservata per
 * effettuare il grade parziale dell'homework. */
package hw2;